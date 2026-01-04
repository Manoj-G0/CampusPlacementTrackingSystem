package com.cpt.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cpt.model.Application;
import com.cpt.model.AttendedDrive;
import com.cpt.model.Profile;
import com.cpt.model.Resume;
import com.cpt.model.Student;
import com.cpt.service.AdminService;
import com.cpt.service.StudentService;
import com.cpt.util.MessagesDTO;
import com.cpt.util.NotificationUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private NotificationUtil notificationutil;

	@Autowired
	MessagesDTO messageDTO;

	@Autowired
	Resume resume;

	@Autowired
	AdminService adminService;

	// To validate the session
	private String validateSession(HttpSession session) {
		String usrId = (String) session.getAttribute("userId");
		if (usrId == null) {
			return "redirect:/login?error=not_logged_in";
		}
		if (studentService.getStudentById(usrId) == null) {
			session.invalidate();
			return "redirect:/login?error=invalid_role";
		}
		return null;
	}

	// To get the student drives
	@GetMapping("/drives")
	@ResponseBody
	public List<Map<String, Object>> getStudentDrives(@RequestParam("userId") String usrId) {
		return studentService.getStudentDrives(usrId);
	}

	@GetMapping("/profile")
	public String updateProfile(Model m, HttpSession session) {
		String usrId = (String) session.getAttribute("userId");
		session.setAttribute("userId", usrId);
		m.addAttribute("profile", studentService.getProfile(usrId));
		session.setAttribute("userId", usrId);
		return "profile";
	}

	// Drive details using the Id
	@GetMapping(value = "/drive-details/{pldId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getDriveDetails(@PathVariable("pldId") int pldId) {
		Map<String, Object> pd = adminService.getDriveDetails(pldId);
		return ResponseEntity.ok(pd);

	}

	// To update the profile
	@PostMapping("/updateProfile")
	public String updateStudentProfile(HttpSession session, Model model, @RequestParam("prfUsrId") String prfUsrId,
			@RequestParam(value = "prfImage", required = false) MultipartFile prfImage,
			@RequestParam("collegeEmail") String collegeEmail,
			@RequestParam(value = "contact", required = false) String contact,
			@RequestParam(value = "skills", required = false) String skills,
			@RequestParam(value = "githubUrl", required = false) String githubUrl,
			@RequestParam(value = "profileImage", required = false) MultipartFile prfImg) {
		String userId = (String) session.getAttribute("userId");
		try {

			if (prfImage == null) {
				prfImage = prfImg;
			}
			if (!collegeEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
				model.addAttribute("error", "Invalid email format.");
				model.addAttribute("profile", studentService.getProfile(userId));
				return "profile";
			}

			// Validate image
			byte[] imageBytes = null;
			if (prfImage != null && !prfImage.isEmpty()) {
				String contentType = prfImage.getContentType();
				if (!contentType.startsWith("image/")) {
					model.addAttribute("error", "Invalid image file.");
					model.addAttribute("profile", studentService.getProfile(userId));
					return "profile";
				}
				imageBytes = prfImage.getBytes();
			}

			// Update profile
			Profile profile = new Profile();
			profile.setPrfUsrId(prfUsrId);
			profile.setPrfImage(imageBytes);
			profile.setPrfContact(contact);
			profile.setSkills(skills);
			profile.setGithubUrl(githubUrl);
			profile.setEmail(collegeEmail);

			studentService.updateProfile(profile); // Note: Method name aligned with service
			model.addAttribute("success", "Profile updated successfully.");
		} catch (Exception e) {
			model.addAttribute("error", "Failed to update profile: " + e.getMessage());
		}

		model.addAttribute("profile", studentService.getProfile(userId));
		return "profile";
	}

	// To save the image
	private String saveImage(MultipartFile image, String usrId) throws IOException {
		if (image.isEmpty()) {
			return null;
		}
		String uploadDir = "uploads/profiles/";
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		String fileName = usrId + "_" + System.currentTimeMillis() + ".jpg";
		Path filePath = uploadPath.resolve(fileName);
		Files.write(filePath, image.getBytes());
		return "/" + uploadDir + fileName;
	}

	@GetMapping("my-applications")
	public String getStudentApplications(HttpSession session, Model m) {
		String usr = (String) session.getAttribute("userId");
		List<Application> li = studentService.getApplicationsByUserId(usr);
		m.addAttribute("upcomingDrives", studentService.getUpcomingDrives());
		m.addAttribute("applications", li);
		return "myapplications";
	}

	@GetMapping("/dashboard")
	public String getDashboard(HttpSession session, Model model) {
		String usrId = (String) session.getAttribute("userId");

		Student student = studentService.getStudentById(usrId);
		model.addAttribute("student", student);
		session.setAttribute("student", student);
		if (student == null) {
			session.invalidate();
			return "redirect:/login?error=student_not_found";
		}

		model.addAttribute("student", student);
		model.addAttribute("upcomingDrives", studentService.getUpcomingDrives());
		model.addAttribute("eligibleDrives", studentService.getEligibleDrives(usrId));
		model.addAttribute("attendedDrives", studentService.getAttendedDrives(usrId));
		model.addAttribute("notifications", studentService.getNotifications(usrId));
		model.addAttribute("applications", studentService.getApplicationsByUserId(usrId));
		session.setAttribute("notificationCount", notificationutil.getNotificationCount(usrId));
		model.addAttribute("notificationCount", notificationutil.getNotificationCount(usrId));
		return "student_dashboard";

	}

	// To get the attended drives
	@GetMapping("/attended-drives")
	public String getAttendedDrives(HttpSession session, Model model) {
		String userId = (String) session.getAttribute("userId");
		model.addAttribute("attendedDrives", studentService.getAttendedDrives(userId));
		List<AttendedDrive> li = studentService.getAttendedDrives(userId);
		return "attendeddrives";
	}

	@GetMapping("/attendeddrives")
	@ResponseBody
	public List<Map<String, Object>> getAttendedDrivesForJson(HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		return studentService.getAttendedDrivesForJson(userId);
	}

	/**
	 * Endpoint to get drive scores (phases and scores) for a selected drive
	 */
	@GetMapping("/drive-scores")
	@ResponseBody
	public List<Map<String, Object>> getDriveScores(HttpSession session, @RequestParam("pldId") Integer pldId) {
		String userId = (String) session.getAttribute("userId");
		return studentService.getDriveScores(userId, pldId);
	}

	@GetMapping("/notifications")
	public String showNotifications(HttpSession session, Model model) {
		String sessionCheck = validateSession(session);
		if (sessionCheck != null) {
			return sessionCheck;
		}

		String usrId = (String) session.getAttribute("userId");
		model.addAttribute("notifications", studentService.getNotifications(usrId));
		return "notifications";
	}

	// To get the upcoming drives
	@GetMapping("/upcomingdrives")
	public String showUpcomingDrives(HttpSession session, Model model) {
		String sessionCheck = validateSession(session);
		if (sessionCheck != null) {
			return sessionCheck;
		}

		model.addAttribute("upcomingDrives", studentService.getUpcomingDrives());
		return "upcomingdrives";
	}

	// students who are eligible
	@GetMapping("/eligible-drives")
	public String showEligibleDrives(HttpSession session, Model model) {
		String sessionCheck = validateSession(session);
		if (sessionCheck != null) {
			return sessionCheck;
		}

		String usrId = (String) session.getAttribute("userId");
		model.addAttribute("eligibleDrives", studentService.getEligibleDrives(usrId));
		return "eligibledrives";
	}

	@GetMapping("/selected-drives")
	public String showSelectedDrives(HttpSession session, Model model) {
		String sessionCheck = validateSession(session);
		if (sessionCheck != null) {
			return sessionCheck;
		}

		String usrId = (String) session.getAttribute("userId");
		model.addAttribute("selectedDrives", studentService.getSelectedDrives(usrId));
		return "selecteddrives";
	}

	// To get the resumes
	@GetMapping("/resume")
	public String getResume(HttpSession session, Model m) {
		String sessionCheck = validateSession(session);
		if (sessionCheck != null) {
			return sessionCheck;
		}
		m.addAttribute("resume", studentService.getResumesByUserId((String) session.getAttribute("userId")));
		m.addAttribute("message", messageDTO.getResumeUploaded());
		return "resume";
	}

	// To add the resumes
	@PostMapping("/addresume")
	public String addResume(@RequestParam("resume") MultipartFile resumeFile, HttpSession session, Model model)
			throws IOException {
		String sessionCheck = validateSession(session);
		if (sessionCheck != null) {
			return sessionCheck;
		}

		String usrId = (String) session.getAttribute("userId");
		resume.setResUsrId(usrId);
		resume.setResFileName(resumeFile.getOriginalFilename());
		resume.setResType(resumeFile.getContentType());
		resume.setResumeData(resumeFile.getBytes());

		try {
			studentService.addResume(resume);
		} catch (Exception e) {

			return "redirect:/student/resume?error=Resume_add_failed";
		}
		return "redirect:/student/resume";
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

	// To view the resumes
	@GetMapping("/view/{userId}")
	public void viewResume(@PathVariable("userId") String userId, HttpServletResponse response, Model model)
			throws IOException {
		Resume resume = studentService.getResumesByUserId(userId);
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

	// To delete the resume
	@PostMapping("/deleteresume/{userId}")
	public String deleteResume(@PathVariable("userId") String usrId, HttpSession session) {
		String sessionCheck = validateSession(session);
		if (sessionCheck != null) {
			return sessionCheck;
		}

		try {
			studentService.deleteResume(usrId);
		} catch (Exception e) {
			return "redirect:/student/resume?error=resume_delete_failed";
		}

		return "redirect:/student/resume?message=resume_delete_successfully";
	}

	@GetMapping("/applicationForm")
	public String getApplicationForm(HttpSession session, Model m) {
		String usr = (String) session.getAttribute("userId");
		m.addAttribute("resume", studentService.getResumesByUserId((String) session.getAttribute("userId")));
		m.addAttribute("student", studentService.getStudentById(usr));
		return "applicationform";
	}

	@GetMapping("/apply/{pldId}")
	public String showApplicationForm(@PathVariable("pldId") Integer pldId, HttpSession session, Model model) {
		String sessionCheck = validateSession(session);
		if (sessionCheck != null) {
			return sessionCheck;
		}

		String usrId = (String) session.getAttribute("userId");
		session.setAttribute("pld_id", pldId);
		if (studentService.hasApplied(usrId, pldId)) {
			return "redirect:/student/eligible-drives?error=already_applied";
		}

		Student student = studentService.getStudentById(usrId);
		if (student == null) {
			session.invalidate();
			return "redirect:/login?error=student_not_found";
		}

		model.addAttribute("student", student);
		model.addAttribute("pldId", pldId);
		return "redirect:/student/applicationForm";
	}

	// To submit the application
	@PostMapping("/submitApplication")
	public String submitApplication(HttpSession session) {
		String usrId = (String) session.getAttribute("userId");
		int pldId = (int) session.getAttribute("pld_id");
		int cmpId = studentService.getCmpId(pldId);
		session.setAttribute("cmp_id", cmpId);
		String sessionCheck = validateSession(session);
		if (sessionCheck != null) {
			return sessionCheck;
		}
		try {
			if (studentService.resumeExists(usrId)) {
				studentService.submitApplication(usrId, pldId, cmpId);
			} else {
				return "redirect:/student/applicationForm?error=Please upload Your resume";
			}
		} catch (Exception e) {
			return "redirect:/student/apply/" + pldId + "?error=application_submit_failed";
		}

		return "redirect:/student/eligible-drives";
	}

	@GetMapping("/hasApplied/{pldId}")
	@ResponseBody
	public Map<String, Boolean> hasApplied(@PathVariable Integer pldId, HttpSession session) {
		String usrId = (String) session.getAttribute("userId");
		Map<String, Boolean> response = new HashMap<>();
		response.put("hasApplied", usrId != null && studentService.getStudentById(usrId) != null
				&& studentService.hasApplied(usrId, pldId));
		return response;
	}

	// Marks a notification as read.
	@GetMapping("/markNotificationRead/{ntfId}")
	public String markNotificationRead(@PathVariable("ntfId") Integer ntfId, HttpSession session) {
		String sessionCheck = validateSession(session);
		if (sessionCheck != null) {
			return sessionCheck;
		}

		try {
			studentService.markNotificationRead(ntfId);
		} catch (Exception e) {
			return "redirect:/student/notifications?error=invalid_action";
		}

		return "redirect:/student/notifications";
	}

	// FAQs
	@GetMapping("/faq")
	public String getFAQs() {
		return "studentFaq";
	}

	@GetMapping("/faqs")
	@ResponseBody
	public List<Map<String, String>> getFAQQuestions() {
		return studentService.getFAQQuestions();
	}

	@GetMapping("/faqs/{id}")
	@ResponseBody
	public String getFAQAnswer(@PathVariable("id") int id) {
		return studentService.getFAQAnswers(id);
	}
}