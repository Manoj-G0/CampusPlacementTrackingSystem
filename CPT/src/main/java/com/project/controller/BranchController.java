package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.Branch;
import com.project.service.PlacementService;

@Controller
@RequestMapping("/tpo/branch")
public class BranchController {
	private final PlacementService placementService;

	public BranchController(PlacementService placementService) {
		this.placementService = placementService;
	}

	@GetMapping("/manage")
	public String showBranchManagement(Model model) {
		model.addAttribute("branches", placementService.getAllBranches());
		model.addAttribute("branch", new Branch());
		model.addAttribute("contentPage", "branch-management.jsp");
		return "layout";
	}

	@PostMapping("/save")
	public String saveBranch(@ModelAttribute Branch branch) {
		placementService.saveBranch(branch);
		return "redirect:/tpo/branch/manage";
	}
}