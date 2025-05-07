package com.project.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.Application;
import com.project.model.User;
import com.project.service.PlacementService;

@Controller
@RequestMapping("/student/application")
public class ApplicationController {
	private final PlacementService placementService;

	public ApplicationController(PlacementService placementService) {
		this.placementService = placementService;
	}

	@GetMapping("/manage")
	public String showApplicationManagement(Model model, Principal principal) {
		User user = placementService.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("eligibleDrives", placementService.getEligibleDrives(user.getId()));
		model.addAttribute("applications", placementService.getApplicationsByUserId(user.getId()));
		model.addAttribute("application", new Application());
		model.addAttribute("contentPage", "application-management.jsp");
		return "layout";
	}

	@PostMapping("/save")
	public String saveApplication(@ModelAttribute Application application, Principal principal) {
		User user = placementService.getUserByEmail(principal.getName());
		application.setUserId(user.getId());
		application.setApplicationDate(LocalDate.now());
		application.setStatus("PEND");
		placementService.saveApplication(application);
		return "redirect:/student/application/manage";
	}
}