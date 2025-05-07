package com.project.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.User;
import com.project.service.PlacementService;

@Controller
@RequestMapping("/notification")
public class NotificationController {
	private final PlacementService placementService;

	public NotificationController(PlacementService placementService) {
		this.placementService = placementService;
	}

	@GetMapping("/manage")
	public String showNotifications(Model model, Principal principal) {
		User user = placementService.getUserByEmail(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("notifications", placementService.getNotificationsByUserId(user.getId()));
		model.addAttribute("contentPage", "notification-management.jsp");
		return "layout";
	}

	@GetMapping("/read/{id}")
	public String markAsRead(@PathVariable("id") Short id) {
		placementService.markNotificationAsRead(id);
		return "redirect:/notification/manage";
	}
}