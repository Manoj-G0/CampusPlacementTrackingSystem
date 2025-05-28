package com.cpt.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpt.service.UserService;
import com.cpt.util.MessagesDTO;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	MessagesDTO msgDto;

	@GetMapping("/login")
	public String login(Model m) {
		m.addAttribute("messages", msgDto);
		return "login";
	}

	@GetMapping("/changePassword")
	public String showChangePassword(HttpSession session, Model model) {
		return "changePassword";
	}

	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("currPasswd") String currentPassword,
			@RequestParam("newPasswd") String newPassword, HttpSession session, Model model) {
		String usrId = (String) session.getAttribute("userId");
		String role = (String) session.getAttribute("role");

		try {
			boolean success = userService.changePassword(usrId, currentPassword, newPassword);
			if (!success) {
				return "redirect:/changePassword?error=invalid_current_password";
			}
		} catch (Exception e) {
			return "redirect:/changePassword?error=password_change_failed";
		}
		if (role.equals("STUD"))
			return "redirect:/student/dashboard";
		else if (role.equals("ADMN"))
			return "redirect:/admin/dashboard";
		else if (role.equals("HR"))
			return "redirect:/hr/dashboard";
		else
			return "redirect:/login";
	}

	// To verify the login to users
	@PostMapping("/login")
	public String verifyLogin(@RequestParam("username") String usr, @RequestParam("passwd") String passwd,
			HttpSession session, Model m) {
		String role = userService.getRole(usr, passwd);
		if (role != null)
			role = role.trim();
		if (role == null) {
			m.addAttribute("error", "Invalid Credentials");
			return "login";
		}
		session.setAttribute("userId", usr);
		session.setAttribute("role", role);
		System.out.println(usr + "---" + passwd + "---" + role);
		m.addAttribute("userId", usr);
		if (role.equals("STUD")) {
			if (passwd.equals("student@123")) {
				session.setAttribute("userId", usr);
				return "changePassword";
			} else {
				return "redirect:/student/dashboard";
			}
		} else if (role.equals("ADMN")) {
			if (passwd.equals("admin@123")) {
				session.setAttribute("userId", usr);
				return "changePassword";
			} else {
				return "redirect:/admin/dashboard";
			}
		} else if (role.equals("HR")) {
			if (passwd.equals("hr@123")) {
				session.setAttribute("userId", usr);
				return "changePassword";
			} else {
				return "redirect:/hr/dashboard";
			}
		} else {
			m.addAttribute("error", "Profile Not Found");
			return "redirect:login";
		}
	}

	// To get the notifications
	@GetMapping("/getNotifications")
	@ResponseBody
	public List<Map<String, Object>> getNotifications(@RequestParam("userId") String userId) {
		return userService.getNotifications(userId);
	}

	// when we logout redirecting to the login
	@GetMapping("/logout")
	public String logout(HttpSession session, Model m) {
		session.invalidate();
		m.addAttribute("message", "Logged Out Successfully");
		return "redirect:/login";
	}

}
