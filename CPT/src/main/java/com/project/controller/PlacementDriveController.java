package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.PlacementDrive;
import com.project.service.PlacementService;

@Controller
@RequestMapping("/tpo/drive")
public class PlacementDriveController {
	private final PlacementService placementService;

	public PlacementDriveController(PlacementService placementService) {
		this.placementService = placementService;
	}

	@GetMapping("/manage")
	public String showDriveManagement(Model model) {
		model.addAttribute("drives", placementService.getAllPlacementDrives());
		model.addAttribute("drive", new PlacementDrive());
		model.addAttribute("contentPage", "drive-management.jsp");
		return "layout";
	}

	@PostMapping("/save")
	public String saveDrive(@ModelAttribute PlacementDrive drive) {
		placementService.savePlacementDrive(drive);
		return "redirect:/tpo/drive/manage";
	}
}