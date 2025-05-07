package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.Evaluation;
import com.project.service.PlacementService;

@Controller
@RequestMapping("/hr/evaluation")
public class EvaluationController {
	private final PlacementService placementService;

	public EvaluationController(PlacementService placementService) {
		this.placementService = placementService;
	}

	@GetMapping("/manage")
	public String showEvaluationManagement(Model model) {
		model.addAttribute("drives", placementService.getAllPlacementDrives());
		model.addAttribute("evaluation", new Evaluation());
		model.addAttribute("contentPage", "evaluation-management.jsp");
		return "layout";
	}

	@PostMapping("/save")
	public String saveEvaluation(@ModelAttribute Evaluation evaluation) {
		placementService.saveEvaluation(evaluation);
		return "redirect:/hr/evaluation/manage";
	}
}