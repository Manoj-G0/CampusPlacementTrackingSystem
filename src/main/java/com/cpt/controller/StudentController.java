package com.cpt.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpt.model.PlacementDrive;
import com.cpt.model.Resume;
import com.cpt.model.Student;
import com.cpt.service.StudentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	/**
	 * Displays the student dashboard.
	 */
	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session, Model model) {
		String usrId = (String) session.getAttribute("usr_id");
		usrId = "21a91a0301";

		if (usrId == null) {
			return "redirect:/login?error=not_logged_in";
		}

		if (!studentService.isStudent(usrId)) {
			session.invalidate();
			return "redirect:/login?error=invalid_role";
		}

		Student student = studentService.getStudentById(usrId);
		if (student == null) {
			session.invalidate();
			return "redirect:/login?error=student_not_found";
		}

		model.addAttribute("student", student);
		model.addAttribute("resumes", studentService.getResumesByUserId(usrId));
		model.addAttribute("upcomingDrives", studentService.getUpcomingDrives());
		model.addAttribute("eligibleDrives", studentService.getEligibleDrives(usrId));
		model.addAttribute("attendedDrives", studentService.getAttendedDrives(usrId));
		model.addAttribute("notifications", studentService.getNotifications(usrId));
		model.addAttribute("applications", studentService.getApplicationsByUserId(usrId));

		return "dashboard";
	}

	/**
	 * Displays the profile update form.
	 */
	@GetMapping("/updateProfile")
	public String showUpdateProfile(HttpSession session, Model model) {
		String usrId = (String) session.getAttribute("usr_id");
		if (usrId == null || !studentService.isStudent(usrId)) {
			return "redirect:/login?error=not_logged_in";
		}

		Student student = studentService.getStudentById(usrId);
		if (student == null) {
			session.invalidate();
			return "redirect:/login?error=student_not_found";
		}

		model.addAttribute("student", student);
		return "updateprofile";
	}

	@GetMapping("/notifications")
	public String showNotifications(HttpSession session, Model model) {
		String usrId = (String) session.getAttribute("usr_id");
		if (usrId == null || !studentService.isStudent(usrId)) {
			return "redirect:/login?error=not_logged_in";
		}

		model.addAttribute("notifications", studentService.getNotifications(usrId));
		return "notifications";
	}

	/**
	 * Updates the student's profile.
	 */
	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute Student student, HttpSession session) {
		String usrId = (String) session.getAttribute("usr_id");
		if (usrId == null || !studentService.isStudent(usrId)) {
			return "redirect:/login?error=not_logged_in";
		}

		student.setRollNo(usrId);
		studentService.updateStudent(student);

		return "redirect:/student/dashboard";
	}

	/**
	 * Displays upcoming drives.
	 */
	@GetMapping("/upcomingDrives")
	public String showUpcomingDrives(HttpSession session, Model model) {
		String usrId = (String) session.getAttribute("usr_id");
		if (usrId == null || !studentService.isStudent(usrId)) {
			return "redirect:/login?error=not_logged_in";
		}

		model.addAttribute("upcomingDrives", studentService.getUpcomingDrives());
		return "upcomingdrives";
	}

	/**
	 * Displays eligible drives.
	 */
	@GetMapping("/eligibleDrives")
	public String showEligibleDrives(HttpSession session, Model model) {
		String usrId = (String) session.getAttribute("usr_id");
		if (usrId == null || !studentService.isStudent(usrId)) {
			return "redirect:/login?error=not_logged_in";
		}

		model.addAttribute("eligibleDrives", studentService.getEligibleDrives(usrId));
		return "eligibledrives";
	}

	/**
	 * Displays attended drives.
	 */
	@GetMapping("/selectedDrives")
	public String showSelectedDrives(HttpSession session, Model model) {
		String usrId = (String) session.getAttribute("usr_id");
		if (usrId == null || !studentService.isStudent(usrId)) {
			return "redirect:/login?error=not_logged_in";
		}

		model.addAttribute("attendedDrives", studentService.getAttendedDrives(usrId));
		return "selecteddrives";
	}

	/**
	 * Adds a new resume.
	 */
	@PostMapping("/addResume")
	public String addResume(@RequestParam String resumeFile, HttpSession session) {
		String usrId = (String) session.getAttribute("usr_id");
		if (usrId == null || !studentService.isStudent(usrId)) {
			return "redirect:/login?error=not_logged_in";
		}

		Resume resume = new Resume();
		resume.setResUsrId(usrId);
		resume.setResFile(resumeFile);
		studentService.addResume(resume);

		return "redirect:/student/dashboard";
	}

	/**
	 * Deletes a resume.
	 */
	@GetMapping("/deleteResume/{resId}")
	public String deleteResume(@PathVariable Integer resId, HttpSession session) {
		String usrId = (String) session.getAttribute("usr_id");
		if (usrId == null || !studentService.isStudent(usrId)) {
			return "redirect:/login?error=not_logged_in";
		}

		studentService.deleteResume(resId);

		return "redirect:/student/dashboard";
	}

	/**
	 * Shows the application form for a placement drive.
	 */
	@GetMapping("/apply/{pldId}")
	public String showApplicationForm(@PathVariable Integer pldId, HttpSession session, Model model) {
		String usrId = (String) session.getAttribute("usr_id");
		if (usrId == null || !studentService.isStudent(usrId)) {
			return "redirect:/login?error=not_logged_in";
		}

		if (studentService.hasApplied(usrId, pldId)) {
			return "redirect:/student/dashboard?error=already_applied";
		}

		Student student = studentService.getStudentById(usrId);
		if (student == null) {
			session.invalidate();
			return "redirect:/login?error=student_not_found";
		}

		model.addAttribute("student", student);
		model.addAttribute("resumes", studentService.getResumesByUserId(usrId));
		model.addAttribute("pldId", pldId);
		model.addAttribute("cmpId", studentService.getEligibleDrives(usrId).stream()
				.filter(d -> d.getPldId().equals(pldId)).findFirst().map(PlacementDrive::getPldCmpId).orElse(0));

		return "applicationform";
	}

	/**
	 * Submits an application for a placement drive.
	 */
	@PostMapping("/submitApplication")
	public String submitApplication(@RequestParam String usrId, @RequestParam Integer pldId,
			@RequestParam Integer cmpId, @RequestParam String resumeFile, HttpSession session) {
		if (session.getAttribute("usr_id") == null || !studentService.isStudent(usrId)) {
			return "redirect:/login?error=not_logged_in";
		}

		studentService.submitApplication(usrId, pldId, cmpId, resumeFile);

		return "redirect:/student/dashboard";
	}

	/**
	 * Checks if a student has applied to a drive (for AJAX).
	 */
	@GetMapping("/hasApplied/{pldId}")
	@ResponseBody
	public Map<String, Boolean> hasApplied(@PathVariable Integer pldId, HttpSession session) {
		String usrId = (String) session.getAttribute("usr_id");
		Map<String, Boolean> response = new HashMap<>();
		response.put("hasApplied",
				usrId != null && studentService.isStudent(usrId) && studentService.hasApplied(usrId, pldId));
		return response;
	}

	/**
	 * Marks a notification as read.
	 */
	@GetMapping("/markNotificationRead/{ntfId}")
	public String markNotificationRead(@PathVariable Integer ntfId, HttpSession session) {
		String usrId = (String) session.getAttribute("usr_id");
		if (usrId == null || !studentService.isStudent(usrId)) {
			return "redirect:/login?error=not_logged_in";
		}

		studentService.markNotificationRead(ntfId);

		return "redirect:/student/dashboard";
	}
}