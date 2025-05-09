package com.cpt.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cpt.model.HrHiringPhase;
import com.cpt.model.HrStudent;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/CPT/hr")
public class HrRoundWiseStudentsController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/roundWiseStudents")
    public String roundWiseStudents(@RequestParam int pldId, HttpSession session, Model model) throws IOException {
        String usrId = (String) session.getAttribute("usr_id");
        if (usrId == null) {
            return "redirect:/CPT/login";
        }

        // Load messages
        ClassPathResource resource = new ClassPathResource("message.json");
        Map<String, String> messages = objectMapper.readValue(resource.getInputStream(), Map.class);
        model.addAttribute("messages", messages);

        // Fetch HR details
        String hrSql = "SELECT hr_name, cmp_id, clg_id FROM hr WHERE hr_id = ?";
        Map<String, Object> hr = jdbcTemplate.queryForMap(hrSql, usrId);

        // Verify drive belongs to HR's company and college
        String driveSql = "SELECT pld_id FROM placement_drives WHERE pld_id = ? AND pld_cmp_id = ? AND pld_clg_id = ?";
        List<Map<String, Object>> driveCheck = jdbcTemplate.queryForList(driveSql, pldId, hr.get("cmp_id"), hr.get("clg_id"));
        if (driveCheck.isEmpty()) {
            model.addAttribute("errorMessage", messages.get("drive.access.error"));
            return "hr_dashboard";
        }

        // Fetch hiring phases
        String phaseSql = "SELECT hph_id, hph_name, hph_sequence FROM hiring_phases WHERE hph_pld_id = ? ORDER BY hph_sequence";
        List<HrHiringPhase> phases = jdbcTemplate.query(phaseSql, new Object[]{pldId}, (rs, rowNum) -> {
            HrHiringPhase p = new HrHiringPhase();
            p.setHphId(rs.getInt("hph_id"));
            p.setHphName(rs.getString("hph_name"));
            p.setHphSequence(rs.getInt("hph_sequence"));
            return p;
        });

        // Fetch shortlisted students per phase
        for (HrHiringPhase phase : phases) {
            String studentSql = "SELECT s.rol_no, s.full_name, s.college_email " +
                               "FROM round_wise_shortlisted rws " +
                               "JOIN applications a ON rws.app_id = a.app_id " +
                               "JOIN students s ON a.app_usr_id = s.rol_no " +
                               "WHERE rws.phase_id = ? AND rws.pld_id = ?";
            List<HrStudent> students = jdbcTemplate.query(studentSql, new Object[]{phase.getHphId(), pldId}, (rs, rowNum) -> {
                HrStudent s = new HrStudent();
                s.setRolNo(rs.getString("rol_no"));
                s.setFullName(rs.getString("full_name"));
                s.setCollegeEmail(rs.getString("college_email"));
                return s;
            });
            phase.setStudents(students); // Assuming a new List<HrStudent> field in HrHiringPhase
        }

        model.addAttribute("phases", phases);
        model.addAttribute("pldId", pldId);
        model.addAttribute("userRole", "HR");
        model.addAttribute("userName", hr.get("hr_name"));
        model.addAttribute("userInitials", ((String) hr.get("hr_name")).substring(0, 2).toUpperCase());
        model.addAttribute("activePage", "roundWiseStudents");

        return "round_wise_students";
    }
}