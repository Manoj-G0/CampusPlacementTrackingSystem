package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.College;
import com.project.service.PlacementService;

@Controller
@RequestMapping("/tpo/college")
public class CollegeController {
	private final PlacementService placementService;

	public CollegeController(PlacementService placementService) {
		this.placementService = placementService;
	}

	@GetMapping("/manage")
	public String showCollegeManagement(Model model) {
		model.addAttribute("colleges", placementService.getAllColleges());
		model.addAttribute("college", new College());
		model.addAttribute("contentPage", "college-management.jsp");
		return "layout";
	}

	@PostMapping("/save")
	public String saveCollege(@ModelAttribute College college) {
		placementService.saveCollege(college);
		return "redirect:/tpo/college/manage";
	}
}