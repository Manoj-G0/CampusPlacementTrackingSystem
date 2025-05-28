package com.cpt.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cpt.dao.AdminDAOInt;
import com.cpt.dao.ApplicationDAOInt;
import com.cpt.dao.BranchDAOInt;
import com.cpt.dao.CollegeDAOInt;
import com.cpt.dao.CompanyDAOInt;
import com.cpt.dao.FacultyDAOInt;
import com.cpt.dao.HiringPhaseDAOInt;
import com.cpt.dao.PlacementDriveDAOInt;
import com.cpt.dao.ReportDAOInt;
import com.cpt.dao.ResourceAllocationDAOInt;
import com.cpt.dao.RoundDataDAOInt;
import com.cpt.dao.RoundWiseShortlistedDAOInt;
import com.cpt.dao.ShortlistedDAOInt;
import com.cpt.dao.StudentDAOInt;
import com.cpt.dao.StudentResultsDAOInt;
import com.cpt.dao.TempShortlistedDAOInt;
import com.cpt.model.Admin;
import com.cpt.model.Branch;
import com.cpt.model.College;
import com.cpt.model.Company;
import com.cpt.model.CompanyCategories;
import com.cpt.model.CompanyStatsDTO;
import com.cpt.model.Faculty;
import com.cpt.model.HR;
import com.cpt.model.HiringPhase;
import com.cpt.model.PlacementDrive;
import com.cpt.model.ResourceAllocation;
import com.cpt.model.RoundData;
import com.cpt.model.RoundParameter;
import com.cpt.model.RoundWiseShortlisted;
import com.cpt.model.Shortlisted;
import com.cpt.model.Student;
import com.cpt.model.StudentRoundStatus;
import com.cpt.model.StudentStatsDTO;
import com.cpt.util.PDFReportGenerator;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class AdminService {

	@Autowired
	private CollegeDAOInt collegeDAO;
	@Autowired
	private BranchDAOInt branchDAO;
	@Autowired
	private CompanyDAOInt companyDAO;
	@Autowired
	private ApplicationDAOInt applicationDAO;
	@Autowired
	private FacultyDAOInt facultyDAO;
	@Autowired
	private ResourceAllocationDAOInt resourceAllocationDAO;
	@Autowired
	private StudentDAOInt studentDAO;
	@Autowired
	private ReportDAOInt reportDAO;
	@Autowired
	private PlacementDriveDAOInt placementDriveDAO;
	@Autowired
	private ShortlistedDAOInt shortlistedDAO;
	@Autowired
	private RoundWiseShortlistedDAOInt roundWiseShortlistedDAO;
	@Autowired
	private HiringPhaseDAOInt hriringphaseDAO;
	@Autowired
	private StudentResultsDAOInt studentResultsDAO;
	@Autowired
	private TempShortlistedDAOInt tempShortlistedDAO;
	@Autowired
	private RoundDataDAOInt roundDataDAO;

	@Autowired
	private AdminDAOInt adminDAO;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final List<List<String>> studentList = new ArrayList<>();
	private final List<List<String>> shortlistedStudents = new ArrayList<>();
	private List<Integer> scoresList = new ArrayList<>();

	// Existing methods (unchanged)

	// To get college details
	public List<College> getAllColleges() {
		return collegeDAO.findAll();
	}

	// To get all branch details
	public List<Branch> getAllBranches() {
		return branchDAO.findAll();
	}

	// To get all companies
	public List<Company> getAllCompanies() {
		return companyDAO.findAll();
	}

	// to get company details based on company id
	public Company getCompanyById(int company_id) {
		return companyDAO.getCompanyById(company_id);
	}

	// get placement details by placement id
	public PlacementDrive getPlacementById(int pldId) {
		return placementDriveDAO.findById(pldId);
	}

	// to get company categories
	public List<CompanyCategories> getCategories() {
		// TODO Auto-generated method stub
		List<CompanyCategories> categories = companyDAO.findAllCategories();
		return categories;
	}

	// to get all placement drive deatils
	public List<PlacementDrive> getAllPlacementDrives() {
		return placementDriveDAO.findAll();
	}

	// to get company count
	public long getCompanyCount() {
		String sql = "SELECT COUNT(*) FROM companies";
		return jdbcTemplate.queryForObject(sql, Long.class);
	}

	// to get student count
	public long getStudentCount() {
		return studentDAO.count();
	}

	// to get active drives count
	public long getActiveDriveCount() {
		return placementDriveDAO.countActive();
	}

	// to get placements count
	public long getPlacementCount() {
		return applicationDAO.countPlaced();
	}

	// to create placement drive
	public void createPlacementDrive(PlacementDrive drive, String userId) {
		placementDriveDAO.save(drive);
	}

	// to add the company
	public void addCompany(Company company, String userId) {
		companyDAO.save(company);
	}

	// to add the college
	public void addCollege(College college, String userId) {
		collegeDAO.save(college);
	}

	// to update the college
	public void updateCollege(College college) {
		collegeDAO.updateCollege(college);
	}

	// To remove the college
	public void removeCollege(int clg_id) {
		collegeDAO.removeCollege(clg_id);
	}

	// To get the college details by Id
	public College getCollegeById(int clg_id) {
		return collegeDAO.getCollegeById(clg_id);
	}

	// To add t5he branch details
	public void addBranch(Branch branch, String userId) {
		branchDAO.save(branch);
	}

	// To get the branch by Id
	public Branch getBranchById(int brnId) {
		return branchDAO.findById(brnId);
	}

	// To update the placement drive details
	public void updatePlacementDrive(PlacementDrive placementDrive) {
		placementDriveDAO.updateDrive(placementDrive);
	}

	// To remove the placement drive details
	public void removePlacementDrive(int pldId) {
		placementDriveDAO.deleteDrive(pldId);
	}

	// To update the company details
	public int updateCompany(Company company) {
		return adminDAO.updateCompany(company);
	}

	// To delete the company details
	public void deleteCompany(int cmpId) {
		// TODO Auto-generated method stub
		adminDAO.deleteCompany(cmpId);
	}

	// To get the available faculty details
	public List<Faculty> getAvailableFaculties() {
		return facultyDAO.findAvailable();
	}

	// To assign the faculty
	public void assignFaculty(Integer facultyId, Integer pldId, String userId) {
		facultyDAO.assignFaculty(facultyId, pldId);
	}

	// To allocate the resource
	public void allocateResource(ResourceAllocation allocation, String userId) {
		resourceAllocationDAO.save(allocation);
	}

	// To get the resource allocate
	public List<ResourceAllocation> getResourceAllocations() {
		return resourceAllocationDAO.findAll();
	}

	// To update the branch details
	public void updateBranch(Branch branch) {
		branchDAO.updateBranch(branch);
	}

	// Charan Added
	// To save the Hr details
	public void saveHR(HR hr) {
		adminDAO.save(hr);
	}

	// To get the placed percentage
	public double getPlacementConversionRate() {
		long totalStudents = getStudentCount();
		long placedStudents = getPlacementCount();
		if (totalStudents == 0)
			return 0.0;
		return (double) placedStudents / totalStudents * 100;
	}

	// Get company-wise placements across branches
	// List of maps containing company name, branch name, and placement count
	public List<Map<String, Object>> getCompanyWisePlacements() {
		String sql = "SELECT c.cmp_name, b.brn_name, COUNT(a.app_id) as placement_count " + "FROM applications a "
				+ "JOIN companies c ON a.app_cmp_id = c.cmp_id " + "JOIN students s ON a.app_usr_id = s.roll_no "
				+ "JOIN branches b ON s.branch_id = b.brn_id " + "WHERE a.app_status = 'OFFR' "
				+ "GROUP BY c.cmp_name, b.brn_name";
		return jdbcTemplate.queryForList(sql);
	}

	// Get branch-wise and gender-wise placements
	// List of maps containing branch name, gender, and placement count
	public List<Map<String, Object>> getBranchGenderWisePlacements() {
		String sql = "SELECT b.brn_name, s.gender, COUNT(a.app_id) as placement_count " + "FROM applications a "
				+ "JOIN students s ON a.app_usr_id = s.roll_no " + "JOIN branches b ON s.branch_id = b.brn_id "
				+ "WHERE a.app_status = 'SHRT' " + "GROUP BY b.brn_name, s.gender";
		return jdbcTemplate.queryForList(sql);
	}

	// Get rounds-wise success rate for each placement drive
	// List of maps containing drive name, phase name, and success rate
	public List<Map<String, Object>> getRoundWiseSuccessRate() {
		String sql = "SELECT pd.pld_name, hp.hph_name, "
				+ "COUNT(DISTINCT rws.student_id) * 100.0 / NULLIF(COUNT(DISTINCT a.app_id), 0) as success_rate "
				+ "FROM placement_drives pd " + "JOIN hiring_phases hp ON hp.hph_pld_id = pd.pld_id "
				+ "LEFT JOIN round_wise_shortlisted rws ON rws.pld_id = pd.pld_id AND rws.phase_id = hp.hph_id "
				+ "LEFT JOIN applications a ON a.app_pld_id = pd.pld_id " + "GROUP BY pd.pld_name, hp.hph_name";
		return jdbcTemplate.queryForList(sql);
	}

	// Get counts of placement drives by status
	// Map with counts for upcoming, ongoing, and completed drives
	public Map<String, Long> getDriveStatusCounts() {
		// Note: pld_status is 'ASSIGNED' or 'NOT ASSIGNED'; status is determined by
		// dates
		String sql = "SELECT " + "SUM(CASE WHEN pld_start_date > CURRENT_DATE THEN 1 ELSE 0 END) as upcoming, "
				+ "SUM(CASE WHEN pld_start_date <= CURRENT_DATE AND (pld_end_date >= CURRENT_DATE OR pld_end_date IS NULL) THEN 1 ELSE 0 END) as ongoing, "
				+ "SUM(CASE WHEN pld_end_date < CURRENT_DATE THEN 1 ELSE 0 END) as completed "
				+ "FROM placement_drives";

		Map<String, Object> result = jdbcTemplate.queryForMap(sql);

		Map<String, Long> statusCounts = new HashMap<>();
		statusCounts.put("upcoming",
				result.get("upcoming") != null ? ((Number) result.get("upcoming")).longValue() : 0L);
		statusCounts.put("ongoing", result.get("ongoing") != null ? ((Number) result.get("ongoing")).longValue() : 0L);
		statusCounts.put("completed",
				result.get("completed") != null ? ((Number) result.get("completed")).longValue() : 0L);

		return statusCounts;
	}

	// Get details for a specific placement drive
	// @param pldId Placement drive ID
	// Map containing drive details
	public Map<String, Object> getDriveDetails(int pldId) {
		Map<String, Object> result = placementDriveDAO.getDriveDetails(pldId);

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		Map<String, Object> driveDetails = new HashMap<>();
		driveDetails.put("name", result.get("pld_name") != null ? result.get("pld_name") : "Unknown");
		driveDetails.put("company", result.get("cmp_name") != null ? result.get("cmp_name") : "N/A");
		driveDetails.put("startDate",
				result.get("pld_start_date") != null ? sdf.format((Date) result.get("pld_start_date")) : "N/A");
		driveDetails.put("location", result.get("clg_address") != null ? result.get("clg_address") : "Main Campus");
		driveDetails.put("applicants",
				result.get("applicant_count") != null ? ((Number) result.get("applicant_count")).longValue() : 0L);
		driveDetails.put("status", result.get("pld_status") != null ? result.get("pld_status") : "N/A");

		return driveDetails;
	}

	// Get details for all placement drives
	// List of maps containing drive details
	public List<Map<String, Object>> getAllDrives() {
		List<Map<String, Object>> results = placementDriveDAO.getAllDrives();

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		List<Map<String, Object>> drives = new ArrayList<>();
		for (Map<String, Object> result : results) {
			Map<String, Object> drive = new HashMap<>();
			drive.put("pldId", result.get("pld_id") != null ? result.get("pld_id") : 0);
			drive.put("name", result.get("pld_name") != null ? result.get("pld_name") : "Unknown");
			drive.put("company", result.get("cmp_name") != null ? result.get("cmp_name") : "N/A");
			drive.put("startDate",
					result.get("pld_start_date") != null ? sdf.format((Date) result.get("pld_start_date")) : "N/A");
			drive.put("endDate",
					result.get("pld_end_date") != null ? sdf.format((Date) result.get("pld_end_date")) : "N/A");
			drive.put("location", result.get("clg_address") != null ? result.get("clg_address") : "Main Campus");
			drive.put("applicants",
					result.get("applicant_count") != null ? ((Number) result.get("applicant_count")).longValue() : 0L);

			String status = "N/A";
			if (result.get("pld_start_date") != null) {
				LocalDate today = LocalDate.now();
				LocalDate start = ((Date) result.get("pld_start_date")).toLocalDate();
				LocalDate end = result.get("pld_end_date") != null ? ((Date) result.get("pld_end_date")).toLocalDate()
						: today;
				if (start.isAfter(today)) {
					status = "UPCOMING";
				} else if (end.isBefore(today)) {
					status = "COMPLETED";
				} else {
					status = "ONGOING";
				}
			}
			drive.put("status", status);

			drives.add(drive);
		}
		return drives;
	}

	// Existing report generation methods (unchanged)
	public ResponseEntity<byte[]> getCompanyReport() throws Exception {
		List<Map<String, Object>> stats = reportDAO.getPlacementStats();
		byte[] pdfBytes = PDFReportGenerator.generateCompanyPlacementReport(stats);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "company_placement_report.pdf");
		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

	public ResponseEntity<byte[]> getDriveReport() throws Exception {
		List<Map<String, Object>> stats = reportDAO.getDriveDetails();
		byte[] pdfBytes = PDFReportGenerator.generatePlacementDriveReport(stats);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "placement_drive_report.pdf");
		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

	public ResponseEntity<byte[]> getBranchDriveReport() throws Exception {
		List<Map<String, Object>> stats = reportDAO.getBranchPlacementDetails();
		byte[] pdfBytes = PDFReportGenerator.generateBranchPlacementDriveReport(stats);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "branch_placement_drive_report.pdf");
		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

	public ResponseEntity<byte[]> getStudentDriveReport() throws Exception {
		List<Map<String, Object>> stats = reportDAO.getStudentsPlacementDetails();
		byte[] pdfBytes = PDFReportGenerator.generateStudentPlacementDriveReport(stats);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "student_placement_drive_report.pdf");
		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

	// To get all placements
	public List<PlacementDrive> getAllPlacements() {
		return reportDAO.getAllPlacements();
	}

	// To get phase wise report
	public ResponseEntity<byte[]> getPhaseWiseReport(int pldid, int phaseid, int phase_sequence) throws Exception {
		List<Map<String, Object>> stats = reportDAO.getRoundwiseShortlisted(pldid, phaseid);
		byte[] pdfBytes = PDFReportGenerator.generateRoundwiseReport(stats, phase_sequence);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "student_placement_drive_report.pdf");
		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

	// To get phase by placement Id
	public List<Map<String, Object>> getPhasesByDrive(int pldId) {
		return reportDAO.getPhasesByDrive(pldId);
	}

	// To get the student round status
	public Map<String, Map<String, String>> getStudentRoundStatus() {
		List<RoundData> roundDataList = roundDataDAO.getRoundData();
		Map<String, Map<String, String>> studentRoundStatus = new HashMap<>();
		for (RoundData data : roundDataList) {
			String studentId = data.getStudent_id();
			String phaseName = data.getPhase_name();
			String status = data.getStatus();
			String studentName = data.getStudent_name();
			studentRoundStatus.putIfAbsent(studentId, new HashMap<>());
			Map<String, String> studentData = studentRoundStatus.get(studentId);
			if (phaseName != null) {
				studentData.put("name", studentName);
				studentData.put(phaseName, status);
			}
		}
		return studentRoundStatus;
	}

	// To get phase names
	public Set<String> getRoundNames() {
		List<RoundData> roundDataList = roundDataDAO.getRoundData();
		Set<String> roundNames = new LinkedHashSet<>();
		for (RoundData data : roundDataList) {
			String phaseName = data.getPhase_name();
			roundNames.add(phaseName);
		}
		return roundNames;
	}

	// To get the phase names by the placement Id
	public Set<String> getRoundNamesByDrive(int pldId) {
		return roundDataDAO.getRoundNamesByDrive(pldId);
	}

	// To get the student phase status
	public List<StudentRoundStatus> getStudentRoundStatus(int pldId) {
		return roundDataDAO.getStudentRoundDataByDrive(pldId);
	}

	// To get the total students
	public int getTotalStudents(int pldId) {
		return roundDataDAO.getTotalStudents(pldId);
	}

	public List<Map<String, Object>> getPhaseStudentCounts(int pldId) {
		return roundDataDAO.getPhaseStudentCounts(pldId);
	}

	public List<RoundParameter> getStudentDrillDown(String studentId, int pldId) {
		return roundDataDAO.getStudentDrillDown(studentId, pldId);
	}

	// To get the students in drive
	public List<Map<String, Object>> getStudentsInDrives() {
		return roundDataDAO.getStudentsInDrives();
	}

	// sarika implemets

	// To add the students
	public void addStudent(List<String> student) {
		studentList.add(student);
	}

	public List<List<String>> getAllStudents() {
		return studentList;
	}

	// To generate the template
	public String generateTemplate(HttpServletResponse response) {
		return null;

	}

	// To get the student list from the csv file
	public List<List<String>> studentsList(MultipartFile file, int i) {
		studentList.clear();
		shortlistedStudents.clear();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			reader.readLine();
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",");

				List<String> row = Arrays.asList(data);
				studentList.add(row);
				System.out.println(Arrays.asList(data));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return studentList;

	}

	// To evaluate the students based on the cutoff score
	public List<List<String>> evaluateStudents(String phase, Map<String, Integer> hmap, HttpSession session) {
		shortlistedStudents.clear();
		scoresList.clear();
		int savg = 0, tavg = 0;
		int j = 2;
		boolean f;
		int pldId = (int) session.getAttribute("pldId");
		int phid = adminDAO.getPhaseId(pldId, phase);
		for (List<String> student : studentList) {
			f = true;
			savg = 0;
			tavg = 0;
			j = 2;
			for (String x : hmap.keySet()) {
				int stdscore = Integer.parseInt(student.get(j));
				j++;
				int tscore = hmap.get(x);
				if (stdscore < tscore) {
					f = false;
				}
				savg += stdscore;
				tavg += tscore;
			}
			if (f == true || savg > tavg) {
				shortlistedStudents.add(student);
				scoresList.add(savg);
			} else {
				adminDAO.addtoAttended(student.get(0), pldId, phid);
			}
		}
		session.setAttribute("scoresList", scoresList);
		return shortlistedStudents;
	}

	// to get the short listed list
	public List<List<String>> getShortlistedStudents() {
		return shortlistedStudents;
	}

	// To get the phase wise score
	public void getPhaseScores(String phase, List<HiringPhase> scores, List<List<String>> shortlistedStudents,
			List<Integer> slist, int selectedCompanyId) {
		int size = scores.size();
		int pldId = scores.get(0).getHphPldId();
		int seq = scores.get(0).getHphSequence();
		int phId = adminDAO.getPhaseId(pldId, phase);
		String pname = adminDAO.getLastPhase(pldId);
		int i = 0, l = 2, k = 0;
		List<Integer> ls = studentResultsDAO.getParameters(pldId);
		for (List<String> s : shortlistedStudents) {
			String stdId = s.get(0);
			String stdName = s.get(1);
			int stdScore = slist.get(i++);
			if (phase.equals(pname)) {
				shortlistedDAO.saveList(pldId, stdId);

			}
			int score = Integer.parseInt(s.get(l));
			roundWiseShortlistedDAO.saveList(phId, pldId, stdId, score, phase, selectedCompanyId);
			l++;
			int param_id = ls.get(k++);
			studentResultsDAO.saveList(phId, pldId, stdId, param_id, stdScore);
		}
	}

	// To store in temparory storage to change the criteria
	public void tempStorage(String phase, List<HiringPhase> scores, List<List<String>> shortlistedStudents,
			List<Integer> scoresList) {

		int size = scores.size();
		int pldId = scores.get(0).getHphPldId();
		int seq = scores.get(0).getHphSequence();
		int phId = scores.get(0).getHphId();
		String pname = scores.get(size - 1).getHphName();
		int i = 0, l = 2, k = 0;
		List<Integer> ls = studentResultsDAO.getParameters(pldId);
		for (List<String> s : shortlistedStudents) {
			String stdId = s.get(0);
			String stdName = s.get(1);
			int stdScore = scoresList.get(i++);
			tempShortlistedDAO.saveList(pldId, seq, stdId, stdName, pname, stdScore);
		}

	}

	// to get the companies
	public List<String> getCompanies() {
		String sql = "select distinct cmp_name from companies;";
		List<String> ls = jdbcTemplate.queryForList(sql, String.class);
		return ls;

	}

	// To get the company id by company name
	public int getCompanyId(String selectedCompany) {
		String sql = "select cmp_id from companies\r\n" + "where cmp_name=? limit 1";
		int Id = jdbcTemplate.queryForObject(sql, new Object[] { selectedCompany }, Integer.class);
		return Id;
	}

	// To get all the company names
	public List<HiringPhase> getAll(int selectedCompanyId) {
		return hriringphaseDAO.getAll(selectedCompanyId);
	}

	// To get the criteria and its score
	public Map<String, Integer> getCriteria(String pname) {
		return hriringphaseDAO.getCriteria(pname);
	}

	// To get the shortlisted
	public List<Shortlisted> getShortlisted() {
		return shortlistedDAO.getShortlisted();
	}

	public List<RoundWiseShortlisted> getAllShortlisted() {
		return roundWiseShortlistedDAO.getAllShortlisted();
	}

	public byte[] generateCsvTemplate() {
		String header = "rollno,fullname,branch_id,college_id,gender,status,cgpa,backlogs,collegeemail\n";
		return header.getBytes();
	}

	public String saveStudents(List<Student> students) {
		StringBuilder resultMessage = new StringBuilder();
		int successCount = 0;

		for (Student student : students) {
			try {
				// Check if roll number already exists
				if (adminDAO.existsByRollNo(student.getRollNo())) {
					resultMessage.append("Student with Roll No ").append(student.getRollNo())
							.append(" already exists. ");
					continue;
				}

				// Check if college email is unique
				if (adminDAO.existsByCollegeEmail(student.getCollegeEmail())) {
					resultMessage.append("Email ").append(student.getCollegeEmail()).append(" is already in use. ");
					continue;
				}

				// Save student
				adminDAO.save(student);
				successCount++;
			} catch (Exception e) {
				resultMessage.append("Error saving student with Roll No ").append(student.getRollNo()).append(": ")
						.append(e.getMessage()).append(". ");
			}
		}

		if (successCount == students.size()) {
			return "All " + successCount + " students saved successfully.";
		} else {
			return successCount + " out of " + students.size() + " students saved. Errors: " + resultMessage.toString();
		}
	}

	// to parse the file
	public List<Student> parseFile(MultipartFile file) throws IOException {
		List<Student> students = new ArrayList<>();
		String fileName = file.getOriginalFilename().toLowerCase();

		if (fileName.endsWith(".csv") || fileName.endsWith(".txt")) {
			students = parseCsv(file.getInputStream());
		} else if (fileName.endsWith(".xls")) {
			students = parseExcel(file.getInputStream());
		}

		return students;
	}

	// To parse the csv file
	private List<Student> parseCsv(InputStream inputStream) throws IOException {
		List<Student> students = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		boolean firstLine = true;

		while ((line = reader.readLine()) != null) {
			if (firstLine) {
				firstLine = false;
				continue; // Skip header
			}
			String[] data = line.split(",");
			if (data.length == 9) {
				try {
					Student student = new Student();
					student.setRollNo(data[0].trim());
					student.setFullName(data[1].trim());
					student.setBranchId(Long.parseLong(data[2].trim()));
					student.setClgId(Long.parseLong(data[3].trim()));
					student.setGender(data[4].trim());
					student.setStatus(data[5].trim());
					student.setCgpa(Double.parseDouble(data[6].trim()));
					student.setBacklogs(Integer.parseInt(data[7].trim()));
					student.setCollegeEmail(data[8].trim());
					students.add(student);
				} catch (Exception e) {
					System.err.println("Error parsing line: " + line + ", Error: " + e.getMessage());
				}
			}
		}
		reader.close();
		return students;
	}

	// To parse the excel file
	private List<Student> parseExcel(InputStream inputStream) throws IOException {
		List<Student> students = new ArrayList<>();
		Workbook workbook = new HSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		boolean firstRow = true;

		for (Row row : sheet) {
			if (firstRow) {
				firstRow = false;
				continue; // Skip header
			}
			if (row.getPhysicalNumberOfCells() >= 9) {
				try {
					Student student = new Student();
					student.setRollNo(getCellValue(row.getCell(0)));
					student.setFullName(getCellValue(row.getCell(1)));
					student.setBranchId(Long.parseLong(getCellValue(row.getCell(2))));
					student.setClgId(Long.parseLong(getCellValue(row.getCell(3))));
					student.setGender(getCellValue(row.getCell(4)));
					student.setStatus(getCellValue(row.getCell(5)));
					student.setCgpa(Double.parseDouble(getCellValue(row.getCell(6))));
					student.setBacklogs(Integer.parseInt(getCellValue(row.getCell(7))));
					student.setCollegeEmail(getCellValue(row.getCell(8)));
					students.add(student);
				} catch (Exception e) {
					System.err.println("Error parsing row: " + row.getRowNum() + ", Error: " + e.getMessage());
				}
			}
		}
		workbook.close();
		return students;
	}

	private String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			return String.valueOf((int) cell.getNumericCellValue());
		default:
			return "";
		}
	}

	// to add the admin details
	public void addAdmin(Admin admin) throws Exception {
		if (admin.getAdminId() == null || admin.getAdminName() == null || admin.getDesignation() == null
				|| admin.getCollegeId() == 0) {
			throw new Exception("All fields are required");
		}
		adminDAO.addAdmin(admin);
	}

	public List<College> getEveryCollege() {
		return adminDAO.getEveryCollege();
	}

	public List<CompanyStatsDTO> getAllCompaniesWithStats() {
		return companyDAO.getAllCompaniesWithStats();
	}

	public List<StudentStatsDTO> getAllStudentsWithStats() {
		return adminDAO.getAllStudentsWithStats();
	}

	public Map<String, Object> getStudentRoundWiseStatus(int pldId) {
		// TODO Auto-generated method stub
		return adminDAO.getStudentRoundWiseStatus(pldId);
	}

}
