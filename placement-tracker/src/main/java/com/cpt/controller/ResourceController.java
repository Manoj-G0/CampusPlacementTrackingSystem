package com.cpt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpt.dao.ResourceDAOInt;
import com.cpt.model.DriveSubmit;
import com.cpt.model.Resource;
import com.cpt.model.ResourceCrudDTO;
import com.cpt.model.ResourceDTO;
import com.cpt.service.ResourceAllocation;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class ResourceController {

	@Autowired
	ResourceAllocation resAll;

	@Autowired
	private ResourceDAOInt resourceDAO;

	@GetMapping("/getcolleges")
	@ResponseBody
	public List<String> getColleges() {
		List<String> collist = new ArrayList<>();
		collist = resAll.getColleges();
		return collist;
	}

	@GetMapping("/rescrud")
	public String rescrud() {
		return "rescrud";
	}

	// Endpoint to fetch resources for a list of drives
	@GetMapping("/getResources")
	@ResponseBody
	public List<ResourceDTO> getResources(@RequestParam("drives") List<String> drives,
			@RequestHeader(value = "Authorization", required = false) String authHeader, HttpServletResponse response) {

		// Call the DAO method to fetch resources based on the drive names
		return resourceDAO.getResourcesForDrives(drives);
	}

	@PostMapping("/ResAlloc")
	@ResponseBody
	public String getRes(@Validated @RequestBody DriveSubmit ds, Model model, HttpSession session) {
		model.addAttribute("resources", resAll.assignRes(ds));
		Map<Integer, Integer> res = resAll.assignRes(ds);
		List<String> fac = resourceDAO.getFaculty(res.size());
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("resource", res);
		obj.put("faculty", fac);
		arr.put(obj);
		resAll.allocate(ds, res, fac);
		return obj.toString();
	}

	@GetMapping("/getClgId")
	@ResponseBody
	public int getClgId(@RequestParam("cname") String cname) {
		return resourceDAO.getClgId(cname);
	}

	@GetMapping("/roundStu")
	@ResponseBody
	public int getStud(@RequestParam("rounds") int round, @RequestParam("pid") int pid) {
		return resAll.getStudForRound(round, pid);

	}

	@GetMapping("/driveNames")
	@ResponseBody
	public List<String> getDrives() {
		List<String> dlist = resourceDAO.getDrives();
		return dlist;
	}

	@GetMapping("/getDriveDet")
	@ResponseBody
	public Map<String, Object> getDrDet(@RequestParam("dname") String dname) {
		return resourceDAO.getDrDet(dname);
	}

	@GetMapping("/getRounds")
	@ResponseBody
	public List<Integer> getRounds(@RequestParam("pid") int pid) {
		return resourceDAO.getRounds(pid);
	}

	@GetMapping("/resources")
	public String resCrud() {
		return "resource";
	}

	@ResponseBody
	@RequestMapping(value = "/getAllResources", method = RequestMethod.GET, produces = "application/json")
	public List<ResourceCrudDTO> getAllResources() {
		return resourceDAO.getAllResources();
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = "application/json")
	public String edit(@RequestBody ResourceCrudDTO resource) {
		resourceDAO.updateResource(resource);
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/deleteRes", method = RequestMethod.POST, consumes = "application/json")
	public String delete(@RequestBody ResourceCrudDTO resource) {
		resourceDAO.deleteResource(resource.getResourceId());
		return "deleted";
	}

	@PostMapping("/resAdd")
	public String addRes(@Validated Resource res) {
		resAll.addRes(res);
		return "redirect:/admin/resources";
	}

	@GetMapping("/branch")
	@ResponseBody
	public List<Map<String, Object>> getBranches(@RequestParam("clgId") String clgId) {
		return resourceDAO.getBranchesByCollegeId(clgId);
	}
}
