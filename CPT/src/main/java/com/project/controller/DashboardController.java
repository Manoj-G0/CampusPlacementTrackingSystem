package com.project.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.model.User;
import com.project.service.PlacementService;

@Controller
public class DashboardController {
	private final PlacementService placementService;

	public DashboardController(PlacementService placementService) {
		this.placementService = placementService;
	}

	@GetMapping("/dashboard")
	public String showDashboard(Model model, Principal principal) {
		User user = placementService.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		String role = user.getRole();
		if ("STUDENT".equals(role)) {
			model.addAttribute("drives", placementService.getAllPlacementDrives());
			model.addAttribute("eligibleDrives", placementService.getEligibleDrives(user.getId()));
			model.addAttribute("applications", placementService.getApplicationsByUserId(user.getId()));
			model.addAttribute("notifications", placementService.getNotificationsByUserId(user.getId()));
			model.addAttribute("contentPage", "student-dashboard.jsp");
		} else if ("TPO".equals(role)) {
			model.addAttribute("drives", placementService.getAllPlacementDrives());
			model.addAttribute("notifications", placementService.getNotificationsByUserId(user.getId()));
			model.addAttribute("contentPage", "tpo-dashboard.jsp");
		} else if ("HR".equals(role)) {
			model.addAttribute("drives", placementService.getAllPlacementDrives());
			model.addAttribute("notifications", placementService.getNotificationsByUserId(user.getId()));
			model.addAttribute("contentPage", "hr-dashboard.jsp");
		}
		return "layout";
	}

	@GetMapping("/login")
	public String showLogin(Model model) {
		model.addAttribute("contentPage", "login.jsp");
		System.out.println("login");
		return "layout";
	}
}