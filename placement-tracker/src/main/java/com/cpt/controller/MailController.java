package com.cpt.controller;
//
// import java.io.IOException;
// import java.util.HashMap;
//
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.cpt.service.MailService;
// import com.cpt.utility.EmailTemplate;
//
// @RestController
// public class MailController {
//
// private final MailService emailService;
// private final EmailTemplate emailTemplate;
//
// public MailController(MailService emailService, EmailTemplate emailTemplate) {
// this.emailService = emailService;
// this.emailTemplate = emailTemplate;
// }
//
// @PostMapping("/send-student-email")
// public ResponseEntity<String> sendStudentEmail(@RequestParam String from, @RequestParam String to,
// @RequestParam String subject, @RequestParam String studentName, @RequestParam String company,
// @RequestParam String driveDate, @RequestParam String driveTime, @RequestParam String venue) {
// try {
// HashMap<String, String> content = new HashMap<>();
// content.put("studentName", studentName);
// content.put("company", company);
// content.put("driveDate", driveDate);
// content.put("driveTime", driveTime);
// content.put("venue", venue);
// String htmlContent = emailTemplate.getStudentDriveTemplate(content);
// boolean success = emailService.sendEmail(from, to, subject, htmlContent, content);
// return success ? ResponseEntity.ok("Email sent successfully")
// : ResponseEntity.status(500).body("Email sending failed");
// } catch (IOException e) {
// return ResponseEntity.status(500).body("Template loading failed: " + e.getMessage());
// }
// }
//
// @PostMapping("/send-hr-email")
// public ResponseEntity<String> sendHrEmail(@RequestParam String from, @RequestParam String to,
// @RequestParam String subject, @RequestParam String jobDescription, @RequestParam String criteria,
// @RequestParam String driveDate, @RequestParam String driveTime, @RequestParam String orgNote) {
// try {
// HashMap<String, String> content = new HashMap<>();
// content.put("jobDescription", jobDescription);
// content.put("criteria", criteria);
// content.put("driveDate", driveDate);
// content.put("driveTime", driveTime);
// content.put("orgNote", orgNote);
// String htmlContent = emailTemplate.getHrJobTemplate(content);
// boolean success = emailService.sendEmail(from, to, subject, htmlContent, content);
// return success ? ResponseEntity.ok("Email sent successfully")
// : ResponseEntity.status(500).body("Email sending failed");
// } catch (IOException e) {
// return ResponseEntity.status(500).body("Template loading failed: " + e.getMessage());
// }
// }
//
// @PostMapping("/send-admin-email")
// public ResponseEntity<String> sendAdminEmail(@RequestParam String from, @RequestParam String to,
// @RequestParam String subject, @RequestParam String company, @RequestParam String selectionList) {
// try {
// HashMap<String, String> content = new HashMap<>();
// content.put("company", company);
// content.put("selectionList", selectionList);
// String htmlContent = emailTemplate.getAdminSelectionTemplate(content);
// boolean success = emailService.sendEmail(from, to, subject, htmlContent, content);
// return success ? ResponseEntity.ok("Email sent successfully")
// : ResponseEntity.status(500).body("Email sending failed");
// } catch (IOException e) {
// return ResponseEntity.status(500).body("Template loading failed: " + e.getMessage());
// }
// }
// }

import java.io.IOException;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cpt.service.MailService;
import com.cpt.util.EmailTemplate;

@Controller
public class MailController {

	private final MailService emailService;
	private final EmailTemplate emailTemplate;

	public MailController(MailService emailService, EmailTemplate emailTemplate) {
		this.emailService = emailService;
		this.emailTemplate = emailTemplate;
	}

	@GetMapping("/drive-email-form")
	public String showDriveEmailForm() {
		return "send_drive_email";
	}

	// To send the mails
	@PostMapping("/send-drive-emails")
	public String sendDriveEmails(@RequestParam("from") String from, @RequestParam("to") String to,
			@RequestParam("subject") String subject, @RequestParam("studentName") String studentName,
			@RequestParam("company") String company, @RequestParam("driveDate") String driveDate,
			@RequestParam("driveTime") String driveTime, @RequestParam("venue") String venue, Model model) {
		try {
			HashMap<String, String> content = new HashMap<>();
			content.put("studentName", studentName);
			content.put("company", company);
			content.put("driveDate", driveDate);
			content.put("driveTime", driveTime);
			content.put("venue", venue);

			String htmlContent = emailTemplate.getStudentDriveTemplate(content);
			String[] recipients = to.split(",");
			System.out.println(to.split(","));
			boolean allSuccess = true;
			for (String recipient : recipients) {
				recipient = recipient.trim();
				System.out.println(recipient);
				if (!recipient.isEmpty()) {
					boolean success = emailService.sendEmail(from, recipient, subject, htmlContent, content);
					if (!success) {
						allSuccess = false;
					}
				}
			}

			model.addAttribute("message", allSuccess ? "Emails sent successfully" : "Some emails failed to send");
			model.addAttribute("messageType", allSuccess ? "success" : "error");
		} catch (IOException e) {
			model.addAttribute("message", "Template loading failed: " + e.getMessage());
			model.addAttribute("messageType", "error");
		}
		return "send_drive_email";
	}

	// To send to the students
	@PostMapping("/send-student-email")
	public ResponseEntity<String> sendStudentEmail(@RequestParam("from") String from, @RequestParam("to") String to,
			@RequestParam("subject") String subject, @RequestParam("studentName") String studentName,
			@RequestParam("company") String company, @RequestParam("driveDate") String driveDate,
			@RequestParam("driveTime") String driveTime, @RequestParam("venue") String venue, Model model) {
		try {
			HashMap<String, String> content = new HashMap<>();
			content.put("studentName", studentName);
			content.put("company", company);
			content.put("driveDate", driveDate);
			content.put("driveTime", driveTime);
			content.put("venue", venue);
			String htmlContent = emailTemplate.getStudentDriveTemplate(content);
			boolean success = emailService.sendEmail(from, to, subject, htmlContent, content);
			return success ? ResponseEntity.ok("Email sent successfully")
					: ResponseEntity.status(500).body("Email sending failed");
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Template loading failed: " + e.getMessage());
		}
	}

	// To send to the hr
	@PostMapping("/send-hr-email")
	public ResponseEntity<String> sendHrEmail(@RequestParam String from, @RequestParam String to,
			@RequestParam String subject, @RequestParam String jobDescription, @RequestParam String criteria,
			@RequestParam String driveDate, @RequestParam String driveTime, @RequestParam String orgNote) {
		try {
			HashMap<String, String> content = new HashMap<>();
			content.put("jobDescription", jobDescription);
			content.put("criteria", criteria);
			content.put("driveDate", driveDate);
			content.put("driveTime", driveTime);
			content.put("orgNote", orgNote);
			String htmlContent = emailTemplate.getHrJobTemplate(content);
			boolean success = emailService.sendEmail(from, to, subject, htmlContent, content);
			return success ? ResponseEntity.ok("Email sent successfully")
					: ResponseEntity.status(500).body("Email sending failed");
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Template loading failed: " + e.getMessage());
		}
	}

	// To send the email to admin
	@PostMapping("/send-admin-email")
	public ResponseEntity<String> sendAdminEmail(@RequestParam String from, @RequestParam String to,
			@RequestParam String subject, @RequestParam String company, @RequestParam String selectionList) {
		try {
			HashMap<String, String> content = new HashMap<>();
			content.put("company", company);
			content.put("selectionList", selectionList);
			String htmlContent = emailTemplate.getAdminSelectionTemplate(content);
			boolean success = emailService.sendEmail(from, to, subject, htmlContent, content);
			return success ? ResponseEntity.ok("Email sent successfully")
					: ResponseEntity.status(500).body("Email sending failed");
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Template loading failed: " + e.getMessage());
		}
	}
}