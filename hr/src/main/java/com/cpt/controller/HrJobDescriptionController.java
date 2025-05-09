package com.cpt.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/CPT/hr")
public class HrJobDescriptionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/addJobDescription")
    public String showAddJobDescription(HttpSession session, Model model) throws IOException {
        String usrId = (String) session.getAttribute("usr_id");
        if (usrId == null) {
            return "redirect:/CPT/login";
        }

        // Load messages
        ClassPathResource resource = new ClassPathResource("message.json");
        Map<String, String> messages = objectMapper.readValue(resource.getInputStream(), Map.class);
        model.addAttribute("messages", messages);

        // Fetch HR details
        String hrSql = "SELECT cmp_id, clg_id, hr_name FROM hr WHERE hr_id = ?";
        Map<String, Object> hr = jdbcTemplate.queryForMap(hrSql, usrId);

        // Fetch branches for screening criteria
        String branchSql = "SELECT brn_id, brn_name FROM branches WHERE brn_clg_id = ?";
        List<Map<String, Object>> branches = jdbcTemplate.queryForList(branchSql, hr.get("clg_id"));

        model.addAttribute("companyId", hr.get("cmp_id"));
        model.addAttribute("collegeId", hr.get("clg_id"));
        model.addAttribute("branches", branches);
        model.addAttribute("userRole", "HR");
        model.addAttribute("userName", hr.get("hr_name"));
        model.addAttribute("userInitials", ((String) hr.get("hr_name")).substring(0, 2).toUpperCase());
        model.addAttribute("activePage", "addJobDescription");

        return "add_job_description";
    }

    @PostMapping("/addJobDescription")
    public String addJobDescription(
            HttpSession session,
            Model model,
            @RequestParam String pldName,
            @RequestParam String pldRole,
            @RequestParam BigDecimal pldPackage,
            @RequestParam Date pldStartDate,
            @RequestParam Date pldEndDate,
            @RequestParam BigDecimal scrMinGpa,
            @RequestParam int scrMinBacklogs,
            @RequestParam List<Integer> scrBrnId,
            @RequestParam(required = false) String scrGender,
            @RequestParam int phaseCount,
            @RequestParam List<String> hphName,
            @RequestParam List<BigDecimal> cutoffScore
    ) throws IOException {
        String usrId = (String) session.getAttribute("usr_id");
        if (usrId == null) {
            return "redirect:/CPT/login";
        }

        // Load messages
        ClassPathResource resource = new ClassPathResource("message.json");
        Map<String, String> messages = objectMapper.readValue(resource.getInputStream(), Map.class);
        model.addAttribute("messages", messages);

        // Fetch HR details
        String hrSql = "SELECT cmp_id, clg_id FROM hr WHERE hr_id = ?";
        Map<String, Object> hr = jdbcTemplate.queryForMap(hrSql, usrId);
        int cmpId = (int) hr.get("cmp_id");
        int clgId = (int) hr.get("clg_id");

        // Insert placement drive
        String driveSql = "INSERT INTO placement_drives (pld_clg_id, pld_cmp_id, pld_name, pld_role, pld_package, pld_start_date, pld_end_date, pld_status) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, 'NOT ASSIGNED') RETURNING pld_id";
        int pldId = jdbcTemplate.queryForObject(driveSql, new Object[]{clgId, cmpId, pldName, pldRole, pldPackage, pldStartDate, pldEndDate}, Integer.class);

        // Insert screening criteria (one per branch)
        String criteriaSql = "INSERT INTO screening_criteria (scr_pld_id, scr_min_gpa, scr_min_backlogs, scr_brn_id, scr_gender) VALUES (?, ?, ?, ?, ?)";
        for (int brnId : scrBrnId) {
            jdbcTemplate.update(criteriaSql, pldId, scrMinGpa, scrMinBacklogs, brnId, scrGender);
        }

        // Insert hiring phases
        String phaseSql = "INSERT INTO hiring_phases (hph_pld_id, hph_name, hph_sequence, cutoff_score) VALUES (?, ?, ?, ?)";
        for (int i = 0; i < phaseCount; i++) {
            jdbcTemplate.update(phaseSql, pldId, hphName.get(i), i + 1, cutoffScore.get(i));
        }

        // Insert admin notification
        String adminSql = "SELECT po_id FROM admins WHERE clg_id = ?";
        List<String> adminIds = jdbcTemplate.queryForList(adminSql, new Object[]{clgId}, String.class);
        String notifySql = "INSERT INTO notifications (ntf_usr_id, ntf_message, ntf_date, ntf_read) VALUES (?, ?, CURRENT_DATE, FALSE)";
        for (String adminId : adminIds) {
            String message = messages.get("drive.added.notification") + ": " + pldName;
            jdbcTemplate.update(notifySql, adminId, message);
        }

        // Send email to admin (pseudo-code, configure JavaMailSender)
        for (String adminId : adminIds) {
            String emailSql = "SELECT hr_email FROM hr WHERE hr_id = ?";
            String adminEmail = jdbcTemplate.queryForObject(emailSql, new Object[]{adminId}, String.class);
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(adminEmail);
            mail.setSubject(messages.get("drive.added.subject"));
            mail.setText("New drive added: " + pldName + "\nRole: " + pldRole + "\nPackage: " + pldPackage);
            mailSender.send(mail);
        }

        model.addAttribute("successMessage", messages.get("drive.added"));
        return "add_job_description";
    }
}