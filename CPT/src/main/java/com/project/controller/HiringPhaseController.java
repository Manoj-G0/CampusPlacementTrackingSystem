package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.HiringPhase;
import com.project.service.PlacementService;

@Controller
@RequestMapping("/hr/phase")
public class HiringPhaseController {
	private final PlacementService placementService;

	public HiringPhaseController(PlacementService placementService) {
		this.placementService = placementService;
	}

	@GetMapping("/manage")
	public String showPhaseManagement(Model model) {
		model.addAttribute("drives", placementService.getAllPlacementDrives());
		model.addAttribute("phase", new HiringPhase());
		model.addAttribute("contentPage", "phase-management.jsp");
		return "layout";
	}

	@PostMapping("/save")
	public String saveHiringPhase(@ModelAttribute HiringPhase phase) {
		placementService.saveHiringPhase(phase);
		return "redirect:/hr/phase/manage";
	}
}
