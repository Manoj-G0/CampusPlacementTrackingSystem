package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.JobDescription;
import com.project.service.PlacementService;

@Controller
@RequestMapping("/hr/job")
public class JobDescriptionController {
	private final PlacementService placementService;

	public JobDescriptionController(PlacementService placementService) {
		this.placementService = placementService;
	}

	@GetMapping("/manage")
	public String showJobManagement(Model model) {
		model.addAttribute("drives", placementService.getAllPlacementDrives());
		model.addAttribute("job", new JobDescription());
		model.addAttribute("contentPage", "job-management.jsp");
		return "layout";
	}

	@PostMapping("/save")
	public String saveJobDescription(@ModelAttribute JobDescription job) {
		placementService.saveJobDescription(job);
		return "redirect:/hr/job/manage";
	}
}