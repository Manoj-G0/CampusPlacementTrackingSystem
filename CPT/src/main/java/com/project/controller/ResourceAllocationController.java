package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.ResourceAllocation;
import com.project.service.PlacementService;

@Controller
@RequestMapping("/tpo/resource")
public class ResourceAllocationController {
	private final PlacementService placementService;

	public ResourceAllocationController(PlacementService placementService) {
		this.placementService = placementService;
	}

	@GetMapping("/manage")
	public String showResourceManagement(Model model) {
		model.addAttribute("drives", placementService.getAllPlacementDrives());
		model.addAttribute("allocation", new ResourceAllocation());
		model.addAttribute("contentPage", "resource-management.jsp");
		return "layout";
	}

	@PostMapping("/save")
	public String saveResourceAllocation(@ModelAttribute ResourceAllocation allocation) {
		placementService.saveResourceAllocation(allocation);
		return "redirect:/tpo/resource/manage";
	}
}