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

import com.cpt.model.HrHrDetails;
import com.cpt.model.HrPlacementDrive;
import com.cpt.model.HrRecruitmentStat;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/CPT/hr")
public class HrDashboardController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) throws IOException {
        String usrId = (String) session.getAttribute("usr_id");
        if (usrId == null) {
            return "redirect:/CPT/login";
        }

        // Load messages from messages.json
        ClassPathResource resource = new ClassPathResource("message.json");
        Map<String, String> messages = objectMapper.readValue(resource.getInputStream(), Map.class);
        model.addAttribute("messages", messages);

        // Fetch HR details
        String hrSql = "SELECT hr_name, designation, cmp_id, clg_id FROM hr WHERE hr_id = ?";
        HrHrDetails hr = jdbcTemplate.queryForObject(hrSql, new Object[]{usrId}, (rs, rowNum) -> {
            HrHrDetails h = new HrHrDetails();
            h.setHrName(rs.getString("hr_name"));
            h.setDesignation(rs.getString("designation"));
            h.setCmpId(rs.getInt("cmp_id"));
            h.setClgId(rs.getInt("clg_id"));
            return h;
        });

        // Fetch ongoing drives
        String driveSql = "SELECT pd.*, c.cmp_desc FROM placement_drives pd " +
                         "JOIN companies c ON pd.pld_cmp_id = c.cmp_id " +
                         "WHERE pd.pld_clg_id = ? AND pd.pld_cmp_id = ? AND pd.pld_status = 'ASSIGNED' " +
                         "AND CURRENT_DATE BETWEEN pd.pld_start_date AND pd.pld_end_date";
        List<HrPlacementDrive> ongoingDrives = jdbcTemplate.query(driveSql, new Object[]{hr.getClgId(), hr.getCmpId()}, (rs, rowNum) -> {
            HrPlacementDrive d = new HrPlacementDrive();
            d.setPldId(rs.getInt("pld_id"));
            d.setPldName(rs.getString("pld_name"));
            d.setPldRole(rs.getString("pld_role"));
            d.setPldPackage(rs.getBigDecimal("pld_package"));
            d.setPldStartDate(rs.getDate("pld_start_date"));
            d.setPldEndDate(rs.getDate("pld_end_date"));
            d.setCmpDesc(rs.getString("cmp_desc"));
            return d;
        });

        // Fetch application counts
        for (HrPlacementDrive drive : ongoingDrives) {
            String countSql = "SELECT COUNT(*) FROM applications WHERE app_pld_id = ?";
            int count = jdbcTemplate.queryForObject(countSql, new Object[]{drive.getPldId()}, Integer.class);
            drive.setApplicationCount(count); // Assuming a new field in HrPlacementDrive
        }

        // Fetch completed drives
        String completedSql = "SELECT pd.*, c.cmp_desc FROM placement_drives pd " +
                             "JOIN companies c ON pd.pld_cmp_id = c.cmp_id " +
                             "WHERE pd.pld_clg_id = ? AND pd.pld_cmp_id = ? AND pd.pld_end_date < CURRENT_DATE";
        List<HrPlacementDrive> completedDrives = jdbcTemplate.query(completedSql, new Object[]{hr.getClgId(), hr.getCmpId()}, (rs, rowNum) -> {
            HrPlacementDrive d = new HrPlacementDrive();
            d.setPldId(rs.getInt("pld_id"));
            d.setPldName(rs.getString("pld_name"));
            d.setPldRole(rs.getString("pld_role"));
            d.setPldPackage(rs.getBigDecimal("pld_package"));
            d.setPldEndDate(rs.getDate("pld_end_date"));
            d.setCmpDesc(rs.getString("cmp_desc"));
            return d;
        });

        // Fetch recruitment stats for charts
        String statsSql = "SELECT EXTRACT(YEAR FROM pd.pld_end_date) AS year, COUNT(*) AS selected_count " +
                         "FROM attended_drives ad JOIN placement_drives pd ON ad.pld_id = pd.pld_id " +
                         "WHERE pd.pld_clg_id = ? AND ad.status = 'SEL' GROUP BY year";
        List<HrRecruitmentStat> stats = jdbcTemplate.query(statsSql, new Object[]{hr.getClgId()}, (rs, rowNum) -> {
            HrRecruitmentStat s = new HrRecruitmentStat();
            s.setYear(rs.getInt("year"));
            s.setSelectedCount(rs.getInt("selected_count"));
            return s;
        });

        // Set model attributes
        model.addAttribute("hrDetails", hr);
        model.addAttribute("ongoingDrives", ongoingDrives);
        model.addAttribute("completedDrives", completedDrives);
        model.addAttribute("recruitmentStats", stats);
        model.addAttribute("userRole", "HR");
        model.addAttribute("userName", hr.getHrName());
        model.addAttribute("userInitials", hr.getHrName().substring(0, 2).toUpperCase());
        model.addAttribute("activePage", "dashboard");

        return "hr_dashboard";
    }
}