package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.model.Company;
import com.project.service.PlacementService;

@Controller
@RequestMapping("/tpo/company")
public class CompanyController {
	private final PlacementService placementService;

	public CompanyController(PlacementService placementService) {
		this.placementService = placementService;
	}

	@GetMapping("/manage")
	public String showCompanyManagement(Model model) {
		model.addAttribute("companies", placementService.getAllCompanies());
		model.addAttribute("company", new Company());
		model.addAttribute("contentPage", "company-management.jsp");
		return "layout";
	}

	@PostMapping("/save")
	public String saveCompany(@ModelAttribute Company company) {
		placementService.saveCompany(company);
		return "redirect:/tpo/company/manage";
	}
}