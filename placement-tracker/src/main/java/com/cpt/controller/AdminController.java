package com.cpt.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cpt.dao.CompanyDAOInt;
import com.cpt.dao.StudentDAOInt;
import com.cpt.model.Admin;
import com.cpt.model.Branch;
import com.cpt.model.College;
import com.cpt.model.Company;
import com.cpt.model.CompanyStatsDTO;
import com.cpt.model.Drive;
import com.cpt.model.HR;
import com.cpt.model.HiringPhase;
import com.cpt.model.Notification;
import com.cpt.model.PlacementDrive;
import com.cpt.model.RoundParameter;
import com.cpt.model.RoundWiseShortlisted;
import com.cpt.model.ScreeningCriteria;
import com.cpt.model.Shortlisted;
import com.cpt.model.Student;
import com.cpt.model.StudentStatsDTO;
import com.cpt.service.AdminService;
import com.cpt.service.DriveService;
import com.cpt.service.MailService;
import com.cpt.util.MessagesDTO;
import com.cpt.util.NotificationUtil;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private DriveService driveService;
	@Autowired
	private NotificationUtil notificationutil;
	@Autowired
	private MessagesDTO messageDTO;

	@GetMapping("/dashboard")
	public String getDashboard(HttpSession session, Model model) {
		String userId = (String) session.getAttribute("userId");
		model.addAttribute("companyCount", adminService.getCompanyCount());
		model.addAttribute("studentCount", adminService.getStudentCount());
		model.addAttribute("activeDriveCount", adminService.getActiveDriveCount());
		model.addAttribute("placementCount", adminService.getPlacementCount());
		model.addAttribute("placementDrives", adminService.getAllPlacementDrives());

		session.setAttribute("notificationCount", notificationutil.getNotificationCount(userId));
		model.addAttribute("notificationCount", notificationutil.getNotificationCount(userId));
		// New statistics
		model.addAttribute("placementConversionRate", adminService.getPlacementConversionRate());
		model.addAttribute("companyWisePlacements", adminService.getCompanyWisePlacements());
		model.addAttribute("branchGenderWisePlacements", adminService.getBranchGenderWisePlacements());
		model.addAttribute("roundWiseSuccessRate", adminService.getRoundWiseSuccessRate());
		model.addAttribute("driveStatusCounts", adminService.getDriveStatusCounts()); // Added for pie chart

		return "admin_dashboard-v2";
	}

	// Get all the drives
	@GetMapping(value = "/drives", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Map<String, Object>>> getAllDrives() {
		try {
			List<Map<String, Object>> drives = adminService.getAllDrives();
			if (drives == null || drives.isEmpty()) {
				return ResponseEntity.status(404).body(null);
			}
			return ResponseEntity.ok(drives);
		} catch (Exception e) {
			System.err.println("Error fetching drives: " + e.getMessage());
			return ResponseEntity.status(500).body(null);
		}
	}

	// Other existing methods (unchanged)
	@Autowired
	MailService mailService;

	@GetMapping("/add-placement-drive")
	public String showAddPlacementDriveForm(Model model) {
		model.addAttribute("placementDrive", new PlacementDrive());
		model.addAttribute("colleges", adminService.getAllColleges());
		model.addAttribute("companies", adminService.getAllCompanies());
		model.addAttribute("messageDTO", messageDTO);

		return "add_placement_drive";
	}

	@GetMapping("/placement-drives")
	public String showAddPlacementDrives(Model model) {
		List<PlacementDrive> pdlist = adminService.getAllPlacementDrives();
		for (PlacementDrive pd : pdlist) {
			pd.setCollege(adminService.getCollegeById(pd.getCollegeId()));
			pd.setCompany(adminService.getCompanyById(pd.getCompanyId()));
		}
		model.addAttribute("placementDrives", pdlist);
		return "viewPlacementDrives";
	}

	// Drive details using the Id
	@GetMapping(value = "/drive-details/{pldId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getDriveDetails(@PathVariable("pldId") int pldId) {
		Map<String, Object> pd = adminService.getDriveDetails(pldId);
		return ResponseEntity.ok(pd);

	}

	// To edit the drives based on the Id
	@GetMapping("/edit-placement-drives/{pldId}")
	public String editPlacementDrives(@PathVariable("pldId") int pldId, Model model) {
		model.addAttribute("placementDrive", adminService.getPlacementById(pldId));
		System.out.println(adminService.getPlacementById(pldId).getPldRole());
		model.addAttribute("colleges", adminService.getAllColleges());
		model.addAttribute("companies", adminService.getAllCompanies());
		return "editPlacementDrives";
	}

	// Update the drive
	@PostMapping("/update-placement-drive")
	public String updatePlacementDrives(@ModelAttribute PlacementDrive placementDrive, Model model) {
		adminService.updatePlacementDrive(placementDrive);
		return "redirect:/admin/placement-drives";
	}

	// Delete the drive
	@GetMapping("/delete-placement-drives/{pldId}")
	public String deletePlacementDrives(@PathVariable("pldId") int pldId, Model model) {
		adminService.removePlacementDrive(pldId);
		return "redirect:/admin/placement-drives";
	}

	@Autowired
	Environment env;

	@Autowired
	StudentDAOInt studentDAO;
	@Autowired
	MessagesDTO msgDTO;
	@Autowired
	NotificationUtil notificationUtil;

	@PostMapping("/add-placement-drive")
	public String createPlacementDrive(@ModelAttribute PlacementDrive drive, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		adminService.createPlacementDrive(drive, userId);
		List<Student> li = studentDAO.getAllStudents();
		for (Student st : li) {
			// mailService.sendEmail(env.getProperty("USER"), st.getCollegeEmail(),
			// env.getProperty("Drive_Create_Subject"), env.getProperty("Dr_Create_Msg"), new HashMap<>());
			Notification notification = new Notification();
			notification.setNtfUsrId(st.getRollNo());
			notification.setNtfMessage(msgDTO.getAddPD() + drive.getName());
			notification.setNtfDate(LocalDate.now());
			notification.setNtfRead(false);
			notificationUtil.save(notification);
		}

		return "redirect:/admin/dashboard";
	}

	@GetMapping("/add-screening-criteria")
	public String showAddScreeningCriteriaForm(@RequestParam("pldId") Integer pldId, Model model) {
		model.addAttribute("screeningCriteria", new ScreeningCriteria());
		model.addAttribute("pldId", pldId);
		model.addAttribute("branches", adminService.getAllBranches());
		return "add_screening_criteria";
	}

	// To view the colleges
	@GetMapping("/colleges")
	public String viewCollege(Model model) {
		model.addAttribute("colleges", adminService.getAllColleges());
		return "viewColleges";
	}

	@GetMapping("/editcollege/{clgId}")
	public String editCollege(@PathVariable("clgId") int clgId, Model model) {
		model.addAttribute("college", adminService.getCollegeById(clgId));
		return "editCollegeinfo";
	}

	// to update the colleges
	@PostMapping("/updatecollege")
	public String updateCollege(@ModelAttribute College college, Model model) {
		adminService.updateCollege(college);
		return "redirect:/admin/colleges";
	}

	@GetMapping("/deletecollege/{cldId}")
	public String deleteCollege(@PathVariable("clgId") int clgId, Model model) {
		adminService.removeCollege(clgId);
		return "redirect:/admin/colleges";
	}

	@GetMapping("/add-college")
	public String showAddCollegeForm(Model model) {
		model.addAttribute("college", new College());
		return "add_college";
	}

	@PostMapping("/add-college")
	public String addCollege(@ModelAttribute College college, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		adminService.addCollege(college, userId);
		return "redirect:/admin/dashboard";
	}

	@GetMapping("/add-branch")
	public String showAddBranchForm(Model model) {
		model.addAttribute("branch", new Branch());
		model.addAttribute("colleges", adminService.getAllColleges());
		return "add_branch";
	}

	@GetMapping("/editbranch/{brnId}")
	public String editBranch(@PathVariable("brnId") int brnId, Model model) {
		model.addAttribute("branch", adminService.getBranchById(brnId));
		model.addAttribute("colleges", adminService.getAllColleges());
		return "editBranchinfo";
	}

	@PostMapping("/update-branch")
	public String updateBranch(@ModelAttribute Branch branch, Model model) {
		adminService.updateBranch(branch);
		return "redirect:/admin/branches";
	}

	@GetMapping("/branches")
	public String showBranches(Model model) {
		List<Branch> brns = adminService.getAllBranches();
		for (Branch brn : brns) {
			brn.setCollege(adminService.getCollegeById(brn.getBrnClgId()));
		}
		model.addAttribute("branches", brns);
		return "thymeleaf/viewBranchinfo";
	}

	@PostMapping("/add-branch")
	public String addBranch(@ModelAttribute Branch branch, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		adminService.addBranch(branch, userId);
		return "redirect:/admin/dashboard";
	}

	@GetMapping("/add-company")
	public String showAddCompanyForm(Model model) {
		model.addAttribute("company", new Company());
		model.addAttribute("categories", adminService.getCategories());
		return "add_company";
	}

	@PostMapping("/add-company")
	public String addCompany(@ModelAttribute Company company, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		adminService.addCompany(company, userId);
		return "redirect:/admin/dashboard";
	}

	@GetMapping("/companies")
	public String showCompanies(Model model) {
		model.addAttribute("companies", adminService.getAllCompanies());
		return "viewCompanyinfo";
	}

	@GetMapping("/edit-company/{cmpId}")
	public String editCompany(@PathVariable("cmpId") int cmpId, Model model) {
		model.addAttribute("company", adminService.getCompanyById(cmpId));
		model.addAttribute("categories", adminService.getCategories());
		return "editCompanyinfo";
	}

	@PostMapping("/update-company")
	public String updateCompany(@ModelAttribute Company company, Model model) {
		adminService.updateCompany(company);
		return "redirect:/admin/companies";
	}

	@GetMapping("/delete-company/{cmpId}")
	public String deleteCompany(@PathVariable("cmpId") int cmpId, Model model) {
		adminService.deleteCompany(cmpId);
		return "redirect:/admin/companies";
	}

	@GetMapping("/assign-faculty")
	public String showAssignFacultyForm(@RequestParam("pldId") Integer pldId, Model model) {
		model.addAttribute("pldId", pldId);
		model.addAttribute("faculties", adminService.getAvailableFaculties());
		return "assign_faculty";
	}

	@PostMapping("/assign-faculty")
	public String assignFaculty(@RequestParam("facultyId") Integer facultyId, @RequestParam("pldId") Integer pldId,
			HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		adminService.assignFaculty(facultyId, pldId, userId);
		return "redirect:/admin/dashboard";
	}

	// Report Generation (unchanged)
	@GetMapping("companypdf")
	@ResponseBody
	public ResponseEntity<byte[]> generateCompanyReport() throws Exception {
		return adminService.getCompanyReport();
	}

	// To generate the drive pdf
	@GetMapping("drivepdf")
	@ResponseBody
	public ResponseEntity<byte[]> generateDriveReport() throws Exception {
		return adminService.getDriveReport();
	}

	// To get the branch pdf
	@GetMapping("branchpdf")
	@ResponseBody
	public ResponseEntity<byte[]> generateBranchDriveReport() throws Exception {
		return adminService.getBranchDriveReport();
	}

	// To get the student pdf
	@GetMapping("studentpdf")
	@ResponseBody
	public ResponseEntity<byte[]> generateStudentDriveReport() throws Exception {
		return adminService.getStudentDriveReport();
	}

	@ResponseBody
	@GetMapping("driveslist")
	public List<PlacementDrive> getDriveList(PlacementDrive pd) {
		List<PlacementDrive> drives = adminService.getAllPlacements();
		return drives;
	}

	// To get the phase wise pdf
	@GetMapping("phasewisepdf")
	@ResponseBody
	public ResponseEntity<byte[]> getPhaseWiseReport(@RequestParam("pld_id") int pld_id,
			@RequestParam("phase_id") int phase_id, @RequestParam("phase_sequence") int phase_sequence)
			throws Exception {
		System.out.println(phase_sequence);
		return adminService.getPhaseWiseReport(pld_id, phase_id, phase_sequence);
	}

	@GetMapping("report-generation")
	public String showPdfView() {
		return "pdfview";
	}

	// New endpoint to fetch phases for a placement drive
	@GetMapping("phases")
	@ResponseBody
	public List<Map<String, Object>> getPhasesByDrive(@RequestParam("pld_id") int pldId) {
		List<Map<String, Object>> phases = adminService.getPhasesByDrive(pldId);
		return phases;
	}

	@GetMapping("round-wise")
	public String getRoundData(Model model) {
		Map<String, Map<String, String>> studentRoundStatus = adminService.getStudentRoundStatus();
		Set<String> roundNames = adminService.getRoundNames();
		model.addAttribute("studentRoundStatus", studentRoundStatus);
		model.addAttribute("roundNames", roundNames);
		return "roundData";
	}

	@GetMapping("/roundData/byDrive")
	@ResponseBody
	public Map<String, Object> getStudentRoundStatus(@RequestParam("pldId") int pldId) {
		return adminService.getStudentRoundWiseStatus(pldId);

	}

	@GetMapping("/student/drilldown")
	@ResponseBody
	public List<RoundParameter> getStudentDetails(@RequestParam("studentId") String studentId,
			@RequestParam("pldId") int pldId) {
		// Call the service to get drilldown data for the given studentId and pldId
		return adminService.getStudentDrillDown(studentId, pldId);
	}

	@GetMapping("/studentsindrives")
	@ResponseBody
	public List<Map<String, Object>> getStudentsInDrives() {
		List<Map<String, Object>> students = adminService.getStudentsInDrives();
		return students;
	}

	// sarika integration

	private List<List<String>> studentList = new ArrayList<>();
	private List<List<String>> screeningRounds = new ArrayList<>();
	private List<List<String>> shortlistedStudents = new ArrayList<>();
	private double cutoffScore;
	private String selectedphase;
	private String selectedCompany;
	private int selectedCompanyId;
	List<HiringPhase> scores = new ArrayList<>();
	private int d = 0;
	private Map<String, Integer> hmap = new HashMap<>();

	@GetMapping("/phase-evaluation")
	public String companyDetails(Model model) {

		List<String> companies = adminService.getCompanies();// companyDAO
		System.out.println("###" + " " + companies.get(0));
		model.addAttribute("companies", companies);
		return "TemplateDownload";
	}

	// To get the data based on the company
	@PostMapping("/filter")
	public String showScreeningPage(@RequestParam("selectedCompany") String selectedCompany, Model model) {
		this.selectedCompany = selectedCompany;

		List<String> companies = adminService.getCompanies();

		selectedCompanyId = adminService.getCompanyId(selectedCompany);
		scores = adminService.getAll(selectedCompanyId);// hriringphaseDAO

		model.addAttribute("companies", companies); // Important to repopulate
		model.addAttribute("screeningRounds", scores);
		model.addAttribute("selectedCompany", selectedCompany); // For re-selection in JSP

		return "TemplateDownload";
	}

	// To get the criteria wise score
	@GetMapping("/getCriteria")
	@ResponseBody
	public Map<String, Integer> getCriteria(@RequestParam("phaseName") String pname) {
		hmap = adminService.getCriteria(pname);// hriringphaseDAO
		return hmap;
	}

	@GetMapping("/getStudentData")
	@ResponseBody
	public List<List<String>> getStudentData() {

		return studentList;
	}

	// to upload the student data
	@PostMapping("/upload")
	@ResponseBody
	public List<List<String>> uploadFile(@RequestParam("file") MultipartFile file, HttpSession session, Model model) {

		System.out.println("uploading data into file");
		studentList = adminService.studentsList(file, hmap.size());// ScreeningService

		session.setAttribute("upload", d + 1);
		model.addAttribute("screeningRounds", scores);
		model.addAttribute("students", studentList);
		model.addAttribute("uploadedFileName", file.getOriginalFilename());

		return studentList;
	}

	// To evaluate the scores
	@PostMapping("/evaluate")
	@ResponseBody
	public List<List<String>> evaluateStudents(@RequestParam("phase") String phase, HttpSession session) {
		selectedphase = phase;
		shortlistedStudents = adminService.evaluateStudents(phase, hmap, session);
		System.out.println(shortlistedStudents);
		return shortlistedStudents; // Return JSON list directly
	}

	@PostMapping("/saveTemp")
	public String saveTemporarily(HttpSession session, RedirectAttributes redirectAttributes) {
		List<Integer> scoresList = (List<Integer>) session.getAttribute("scoresList");
		adminService.tempStorage(selectedphase, scores, shortlistedStudents, scoresList);

		redirectAttributes.addFlashAttribute("successMessage", "Saved successfully!");
		return "redirect:/admin/sending";
	}

	@PostMapping("/send")
	public String sendShortlisted(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		System.out.println("Hello" + selectedphase + " phase");
		model.addAttribute("sentStudents", shortlistedStudents);
		model.addAttribute("screeningRounds", scores);

		List<Integer> scoresList = (List<Integer>) session.getAttribute("scoresList");
		System.out.println("scores " + scoresList);
		adminService.getPhaseScores(selectedphase, scores, shortlistedStudents, scoresList, selectedCompanyId);

		redirectAttributes.addFlashAttribute("successMessage", "Sent successfully!");
		return "redirect:/admin/sending";
	}

	@GetMapping("/sending")
	public String shortlisted(Model model) {
		List<Shortlisted> finalList = adminService.getShortlisted();// shortlistedDAO
		List<RoundWiseShortlisted> list = adminService.getAllShortlisted();// roundwiseshortlistedDAO
		model.addAttribute("finalList", finalList);
		model.addAttribute("roundWiseList", list);
		return "redirect:/admin/phase-evaluation";

	}

	@GetMapping("/getDrives")
	@ResponseBody
	public List<Drive> getDrives() {
		// Get the first date of the current month in Java
		LocalDate firstDateOfCurrentMonth = getFirstDateOfCurrentMonth();

		// Call the service to get the drives from the DAO
		return driveService.getDrives(firstDateOfCurrentMonth);
	}

	private LocalDate getFirstDateOfCurrentMonth() {
		// Get today's date
		LocalDate now = LocalDate.now();

		// Get the first day of the current month
		return now.withDayOfMonth(1);
	}

	@GetMapping("/resource-allocations")
	public String getCalendar(HttpSession session) {
		return "calendar";
	}

	@GetMapping("/addstudents")
	public String showStudentManagement(Model model) {
		model.addAttribute("students", new ArrayList<Student>());
		System.out.println(messageDTO.getStudentFail());
		model.addAttribute("messageDTO", messageDTO);
		return "student_management";
	}

	@GetMapping("/downloadTemplate")
	public ResponseEntity<byte[]> downloadTemplate() {
		byte[] template = adminService.generateCsvTemplate();
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student_template.csv")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(template);
	}

	// To upload the students
	@PostMapping("/uploadStudents")
	public String uploadStudents(@RequestParam("file") MultipartFile file, Model model) {
		List<Student> students = new ArrayList<>();
		try {
			if (!file.isEmpty()) {
				students = adminService.parseFile(file);
			} else {
				model.addAttribute("error", "Please upload a valid file.");
			}
		} catch (Exception e) {
			model.addAttribute("error", "Error processing file: " + e.getMessage());
		}
		model.addAttribute("students", students);
		model.addAttribute("messageDTO", messageDTO);
		return "student_management";
	}

	@GetMapping("/view-students")
	public String viewStudents(Model model) {
		List<StudentStatsDTO> studentStatsList = studentDAO.getAllStudentsWithStats();
		model.addAttribute("studentStatsList", studentStatsList);
		return "view_students";
	}

	@Autowired
	CompanyDAOInt companyDAO;

	@GetMapping("/view-companies")
	public String viewCompanies(Model model) {
		List<CompanyStatsDTO> companyStatsList = companyDAO.getAllCompaniesWithStats();
		model.addAttribute("companyStatsList", companyStatsList);
		return "view_companies";
	}

	// To save the students
	@PostMapping("/saveStudents")
	@ResponseBody
	public ResponseEntity<Map<String, String>> saveStudents(@RequestBody List<Student> students) {
		System.out.println(students);
		if (students == null || students.isEmpty()) {
			Map<String, String> response = new HashMap<>();
			response.put("status", "error");
			response.put("message", messageDTO.getNostudents());
			return ResponseEntity.badRequest().body(response);
		}

		String result = adminService.saveStudents(students);
		Map<String, String> response = new HashMap<>();
		if (result.contains("All")) {
			response.put("status", "success");
		} else {
			response.put("status", "error");
		}
		response.put("message", result);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/addAdmin")
	public String showAddAdminForm(Model model) {
		model.addAttribute("colleges", adminService.getAllColleges());
		return "add_admin";
	}

	// To add the admin
	@PostMapping("/addAdmin")
	public String addAdmin(@RequestParam("adminId") String adminId, @RequestParam("adminName") String adminName,
			@RequestParam("designation") String designation, @RequestParam("collegeId") int collegeId,
			@RequestParam("email") String email, Model model) {
		try {
			Admin admin = new Admin(adminId, adminName, designation, email, collegeId);
			System.out.println(adminId + " " + adminName + " " + designation + " " + collegeId);
			adminService.addAdmin(admin);
			model.addAttribute("message", "Admin added successfully");
		} catch (Exception e) {
			model.addAttribute("error", "Error adding admin: " + e.getMessage());
		}
		return showAddAdminForm(model);
	}

	// To add the hr details
	@GetMapping("/add-hr-details")
	public String addHRDetails(Model model) {
		model.addAttribute("hr", new HR());
		model.addAttribute("companies", adminService.getAllCompanies());
		model.addAttribute("colleges", adminService.getAllColleges());
		return "add-hr-details";
	}

	@PostMapping("/save-hr-details")
	public String addHR(@ModelAttribute HR hr, Model model) {

		// Save HR using service/DAO
		adminService.saveHR(hr); // Make sure this method handles bytea correctly

		return "redirect:/admin/dashboard"; // Or wherever you want to redirect
	}
}
