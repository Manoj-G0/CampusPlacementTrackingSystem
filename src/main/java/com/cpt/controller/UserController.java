package com.cpt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cpt.service.UserService;

import jakarta.servlet.http.HttpSession;

public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}

	@PostMapping("/login")
	public String processLogin(@RequestParam String usrId, @RequestParam String usrPassword, HttpSession session) {
		if (userService.validateUser(usrId, usrPassword)) {
			session.setAttribute("usr_id", usrId);
			return "redirect:/student/dashboard";
		}
		return "redirect:/login?error=invalid_credentials";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
}
