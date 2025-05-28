package com.cpt.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpt.dao.BranchDAOInt;
import com.cpt.dao.PlacementDriveDAOInt;
import com.cpt.model.ApplicationDTO;
import com.cpt.model.Branch;
import com.cpt.model.Company;
import com.cpt.model.CompanyTeam;
import com.cpt.model.HiringPhase;
import com.cpt.model.Phase;
import com.cpt.model.PhaseData;
import com.cpt.model.PlacementDrive;
import com.cpt.model.Resume;
import com.cpt.model.RoundParameter;
import com.cpt.model.RoundStats;
import com.cpt.model.RoundWiseShortlisted;
import com.cpt.model.RoundWrapper;
import com.cpt.model.ScreeningCriteria;
import com.cpt.model.Shortlisted;
import com.cpt.model.StudentRoundStatus;
import com.cpt.service.HRService;
import com.cpt.util.MessagesDTO;
import com.cpt.util.NotificationUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/hr")
public class HRController {

	@Autowired
	private HRService hrService;
	@Autowired
	private PlacementDriveDAOInt driveDAO;
	@Autowired
	private BranchDAOInt branchDAO;
	@Autowired
	private NotificationUtil notificationutil;

	@Autowired
	private MessagesDTO msgDTO;

	boolean flag = false;

	@GetMapping("/dashboard")
	public String getDashboard(HttpSession session, Model model) {
		String userId = (String) session.getAttribute("userId");
		Company company = hrService.getCompanyByHrId(userId);
		model.addAttribute("applicationCount", hrService.getApplicationCount(userId));
		model.addAttribute("ongoingDrives", hrService.getOngoingDrives(userId));
		model.addAttribute("completedDrives", hrService.getCompletedDrivesByCompany(company.getCmpId()));
		model.addAttribute("upcomingDrives", hrService.getUpcomingDrivesByCompany(company.getCmpId()));
		session.setAttribute("notificationCount", notificationutil.getNotificationCount(userId));
		model.addAttribute("notificationCount", notificationutil.getNotificationCount(userId));
		model.addAttribute("hrName", hrService.getHRProfile(userId).getHrName());
		return "hr_dashboard";
	}

	@GetMapping("/applications")
	public String getApplications(@RequestParam("pldId") Integer pldId, HttpSession session, Model model) {
		String userId = (String) session.getAttribute("userId");
		model.addAttribute("applications", hrService.getApplicationsForDrive(pldId, userId));
		return "hr_applications";
	}

	// charan integrate
	@GetMapping("/manageTeams")
	public String manageTeams(HttpSession session, Model m) {
		String userId = (String) session.getAttribute("userId");
		Company company = hrService.getCompanyByHrId(userId);
		session.setAttribute("company", company);
		m.addAttribute("companyTeams", hrService.getTeamsByCompany(company.getCmpId()));
		return "manageTeams";

	}

	// to add the company team
	@PostMapping("/addCompanyTeam")
	public String addCompanyTeam(@Validated CompanyTeam team) {

		hrService.addCompanyTeam(team);
		return "redirect:manageTeams";
	}

	// To get the ongoing drives
	@GetMapping("/ongoing-drives")
	public String ongoingDrives(Model model, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		Company company = hrService.getCompanyByHrId(userId);
		model.addAttribute("drives", hrService.getOngoingDrivesByCompany(company.getCmpId()));
		return "ongoingdrives";
	}

	// To get the completed drives
	@GetMapping("/completed-drives")
	public String completedDrives(Model model, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		Company company = hrService.getCompanyByHrId(userId);
		model.addAttribute("drives", hrService.getCompletedDrivesByCompany(company.getCmpId()));
		return "completeddrives";
	}

	@GetMapping("/upcoming-drives")
	public String upcomingDrives(Model model, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		Company company = hrService.getCompanyByHrId(userId);
		model.addAttribute("company", company);
		model.addAttribute("drives", hrService.getUpcomingDrivesByCompany(company.getCmpId()));
		return "upcomingdrives";
	}

	// To update the company details
	@GetMapping("/updateCompanyDetails")
	public String updateCompanyInfo(Model m, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		Company company = hrService.getCompanyByHrId(userId);
		m.addAttribute("companyCategories", hrService.getCategories());
		m.addAttribute("company", company);
		return "updateCompanyDetails";
	}

	@PostMapping("/updateCompany")
	public String updateCompany(@Validated Company company, Model m, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		Company cmp = hrService.getCompanyByHrId(userId);
		hrService.updateCompany(company, cmp.getCmpId());
		return "redirect:/hr/dashboard";
	}

	@GetMapping("/getCandidates/{pld_id}")
	public String displayCandidates(@PathVariable("pld_id") int pld_id, Model model, HttpSession session) {
		// Fetch all candidates for the given placement drive
		// Method to fetch round names for the placement drive

		// Fetch the company name associated with the placement drive
		String userId = (String) session.getAttribute("userId");
		Company cmp = hrService.getCompanyByHrId(userId);
		List<ApplicationDTO> applications = hrService.getCandidates(pld_id);
		model.addAttribute("applications", applications);
		model.addAttribute("pld_id", pld_id); // Send placement drive id to the JSP
		model.addAttribute("companyName", cmp.getCmpName()); // Send company name to the JSP

		// Return the view name
		return "displayCandidates";
	}

	// jenny ecunice integrate
	@GetMapping("/hiringForm")
	public String showForm(Model model, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		List<PlacementDrive> drives = hrService.getPlacementDrivesByCompany(userId);

		model.addAttribute("drives", drives);
		model.addAttribute("roundWrapper", new RoundWrapper());
		return "HiringForm";
	}

	@PostMapping("/submitRounds")
	public ResponseEntity<String> handleRoundSubmission(@RequestBody RoundWrapper roundWrapper,
			@RequestParam("pld_id") int pld_id, HttpSession session) {
		hrService.saveRounds(roundWrapper.getRounds(), pld_id);
		return ResponseEntity.ok("Rounds received successfully");
	}

	@GetMapping("/modify-threshold")
	public String viewPhaseDetails(@RequestParam(value = "hph_id", required = false) Integer hph_id, Model model) {
		int pld_id = hrService.getPlacementId();

		// 1. Get all phases (rounds) for this placement drive
		List<Phase> PhaseList = hrService.getPhasesById(pld_id);

		model.addAttribute("PhaseList", PhaseList);
		model.addAttribute("selectedPldid", pld_id);

		if (hph_id != null) {
			// 2. Get student details for the selected phase
			List<PhaseData> studentDetails = hrService.getStudentsByPhaseId(hph_id, pld_id);
			model.addAttribute("studentDetails", studentDetails);

			// 3. Get threshold for the selected phase
			Double threshold = hrService.getThresholdByPhaseId(hph_id, pld_id);
			model.addAttribute("threshold", threshold);

			model.addAttribute("selectedHphid", hph_id);
			model.addAttribute("studentCount", studentDetails.size());
		}

		// hiringDao.updateThresholdByPhaseId(hph_id, modifiedThreshold);

		return "ModifiedCriteria"; // your JSP page
	}

	@PostMapping("/modify-threshold")
	public String updateThreshold(@RequestParam("hph_id") int hph_id,
			@RequestParam("modifiedThreshold") double modifiedThreshold) {
		hrService.updateThresholdByPhaseId(hph_id, modifiedThreshold);
		return "redirect:/hr/modify-threshold";
	}

	@GetMapping("/ScreeningForm")
	public String ScreeningForm(Model model, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		List<PlacementDrive> drives = hrService.getPlacementDrivesByCompany(userId);
		List<Branch> branches = driveDAO.getBranchDetails();
		model.addAttribute("drives", drives);
		model.addAttribute("branches", branches);
		return "ScreeningCriteriaForm";
	}

	@PostMapping("/submitCriteriaForm")
	public String SubmitHiringForm(@Validated ScreeningCriteria sc, @RequestParam("pld_id") int pld_id, Model model,
			HttpSession session) {
		double cgpa = sc.getMinCgpa();
		int backlogs = sc.getBacklogs();
		String gender = sc.getGender();
		int brn_id = sc.getBranch();
		boolean exists = hrService.checkIfPldIdExists(pld_id); // Calling the service layer method to check existence
		if (exists) {
			String userId = (String) session.getAttribute("userId");
			List<PlacementDrive> drives = hrService.getPlacementDrivesByCompany(userId);
			List<Branch> branches = driveDAO.getBranchDetails();
			model.addAttribute("drives", drives);
			model.addAttribute("branches", branches);
			model.addAttribute("errorMessage", msgDTO.getError());
			return "ScreeningCriteriaForm"; // Return back to the form view
		}
		hrService.addCriteriaDetails(sc, pld_id);
		hrService.filterAndSaveStudents(cgpa, backlogs, gender, brn_id, pld_id);
		return "redirect:/hr/dashboard";
	}

	@GetMapping("/getRoundWiseShortListedCandidates/{pld_id}")
	public String viewRoundWiseStatus(@PathVariable("pld_id") int pldId,
			@RequestParam(value = "round", required = false) String round, Model model) {
		List<HiringPhase> phases = hrService.getHiringPhases(pldId);
		String pdName = hrService.getDriveName(pldId);
		Map<String, Integer> phaseSequenceMap = new HashMap<>();
		for (HiringPhase phase : phases) {
			phaseSequenceMap.put(phase.getHphName(), phase.getHphSequence());
		}

		List<RoundWiseShortlisted> roundwiseList;
		if (round != null && !round.isEmpty()) {
			roundwiseList = hrService.getRoundWiseShortlistedByRound(pldId, round);
		} else {
			roundwiseList = hrService.getRoundWiseShortlisted(pldId);
		}
		model.addAttribute("phases", phases);
		model.addAttribute("roundwiseList", roundwiseList);
		model.addAttribute("pdName", pdName);
		model.addAttribute("pldId", pldId);
		roundwiseList.sort(
				Comparator.comparingInt(r -> phaseSequenceMap.getOrDefault(r.getPhase_name(), Integer.MAX_VALUE)));
		return "viewRoundWiseStatus";
	}

	@GetMapping("/getShortListedCandidates/{pld_id}")
	public String viewStatus(@PathVariable("pld_id") int pldId,
			@RequestParam(value = "round", required = false) String round, Model model) {
		String pdName = hrService.getDriveName(pldId);
		List<Shortlisted> finalShortlisted = hrService.getFinalShortlisted(pldId);

		model.addAttribute("finalShortlisted", finalShortlisted);
		model.addAttribute("selectedRound", round);
		model.addAttribute("pldId", pldId);
		model.addAttribute("pdName", pdName);

		return "viewStatus";
	}

	@GetMapping("/getShortlistedAjax")
	public void getShortlistedAjax(@RequestParam("pld_id") int pldId,
			@RequestParam(value = "round", required = false) String round, HttpServletResponse response)
			throws IOException {
		List<RoundWiseShortlisted> list;
		if (round != null && !round.isEmpty()) {
			list = hrService.getRoundWiseShortlistedByRound(pldId, round);
		} else {
			list = hrService.getRoundWiseShortlisted(pldId);
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		if (list.isEmpty()) {
			out.println("<tr><td colspan='4'>No students found for selected round.</td></tr>");
		} else {
			for (RoundWiseShortlisted student : list) {
				out.println("<tr>");
				out.println("<td>" + student.getPhase_name() + "</td>");
				out.println("<td>" + student.getStudentId() + "</td>");
				out.println("<td>" + student.getStudent_name() + "</td>");
				out.println("<td>" + student.getScore() + "</td>");
				out.println("</tr>");
			}
		}
	}

	@RequestMapping(value = "/updateApplicationStatus", method = RequestMethod.POST)
	public String updateApplicationStatus(@RequestParam("pld_id") int pldId,
			@RequestParam("student_id") String studentId,

			@RequestParam("status") String status, @RequestParam("remarks") String remarks) {

		hrService.updateApplicationStatus(pldId, studentId, status, remarks);
		return "redirect:/hr/getShortListedCandidates/" + pldId;
	}

	@GetMapping("/phaseWiseData")
	public String getRoundData(Model model) {
		Map<String, Map<String, String>> studentRoundStatus = hrService.getStudentRoundStatus();
		Set<String> roundNames = hrService.getRoundNames();
		model.addAttribute("studentRoundStatus", studentRoundStatus);
		model.addAttribute("roundNames", roundNames);
		return "phaseWiseData";
	}

	@GetMapping("/roundData/byDrive")
	@ResponseBody
	public Map<String, Object> getStudentRoundStatus(@RequestParam("pldId") int pldId) {
		List<StudentRoundStatus> statuses = hrService.getStudentRoundStatus(pldId);
		Map<String, Map<String, String>> studentStatusMap = new LinkedHashMap<>();
		Set<String> roundNames = new LinkedHashSet<>();

		for (StudentRoundStatus s : statuses) {
			if (s.getPhaseName() != null) { // Ensure phaseName is not null
				roundNames.add(s.getPhaseName());
				Map<String, String> studentData = studentStatusMap.computeIfAbsent(s.getStudentId(), k -> {
					Map<String, String> map = new HashMap<>();
					map.put("name", s.getFullName());
					return map;
				});
				studentData.put(s.getPhaseName(), s.getStatus());
			}
		}

		// If no round names were found from student results, fetch them directly from
		// hiring_phases
		if (roundNames.isEmpty()) {
			roundNames = hrService.getRoundNamesByDrive(pldId);
		}

		// Get total students and phase student counts from the service
		int totalStudents = hrService.getTotalStudents(pldId);
		List<Map<String, Object>> phaseDetails = hrService.getPhaseStudentCounts(pldId);

		// Create RoundStats object
		RoundStats roundStats = new RoundStats();
		roundStats.setTotalStudents(totalStudents);
		roundStats.setPhaseDetails(phaseDetails);

		Map<String, Object> response = new HashMap<>();
		response.put("roundNames", roundNames);
		response.put("studentRoundStatus", studentStatusMap);
		response.put("roundStats", roundStats);
		return response;
	}

	@ResponseBody
	@GetMapping("driveslist")
	public List<PlacementDrive> getDriveList(HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		Company company = hrService.getCompanyByHrId(userId);
		List<PlacementDrive> drives = hrService.getAllPlacementsByCmpId(company.getCmpId());
		return drives;
	}

	@GetMapping("/student/drilldown")
	@ResponseBody
	public List<RoundParameter> getStudentDetails(@RequestParam("studentId") String studentId,
			@RequestParam("pldId") int pldId) {
		// Call the service to get drilldown data for the given studentId and pldId
		return hrService.getStudentDrillDown(studentId, pldId);
	}

	public String determineContentType(String filename) {
		if (filename.endsWith(".pdf")) {
			return "application/pdf";
		} else if (filename.endsWith(".doc")) {
			return "application/msword";
		} else if (filename.endsWith(".docx")) {
			return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		}
		return "application/octet-stream";
	}

	// to get the shortlisted
	@GetMapping("getShortListedCandidates/view/{usr_id}")
	public void viewResume(@PathVariable("usr_id") String usr_id, HttpServletResponse response, Model model)
			throws IOException {
		Resume resume = hrService.getResumesByUserId(usr_id);
		if (resume != null && resume.getResumeData() != null) {
			String filename = resume.getResFileName();
			byte[] fileData = resume.getResumeData();
			String contentType = determineContentType(filename);
			response.setContentType(contentType);
			response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resume Not Found");
		}
	}

}
