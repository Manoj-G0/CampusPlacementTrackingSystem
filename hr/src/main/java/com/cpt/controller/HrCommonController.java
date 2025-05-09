package com.cpt.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cpt.model.HrApplication;
import com.cpt.model.HrStudent;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/CPT/hr")
public class HrCommonController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/downloadApplications")
    public void downloadApplications(@RequestParam int pldId, HttpSession session, HttpServletResponse response) {
        String usrId = (String) session.getAttribute("usr_id");
        if (usrId == null) {
            return;
        }

        // Verify drive access
        String hrSql = "SELECT cmp_id, clg_id FROM hr WHERE hr_id = ?";
        Map<String, Object> hr = jdbcTemplate.queryForMap(hrSql, usrId);
        String driveSql = "SELECT pld_id FROM placement_drives WHERE pld_id = ? AND pld_cmp_id = ? AND pld_clg_id = ?";
        List<Map<String, Object>> driveCheck = jdbcTemplate.queryForList(driveSql, pldId, hr.get("cmp_id"), hr.get("clg_id"));
        if (driveCheck.isEmpty()) {
            return;
        }

        // Fetch applications
        String appSql = "SELECT a.app_id, a.app_date, a.app_status, s.rol_no, s.full_name, s.college_email " +
                       "FROM applications a JOIN students s ON a.app_usr_id = s.rol_no WHERE a.app_pld_id = ?";
        List<HrApplication> applications = jdbcTemplate.query(appSql, new Object[]{pldId}, (rs, rowNum) -> {
            HrApplication a = new HrApplication();
            a.setAppId(rs.getInt("app_id"));
            a.setAppDate(rs.getDate("app_date"));
            a.setAppStatus(rs.getString("app_status"));
            HrStudent s = new HrStudent();
            s.setRolNo(rs.getString("rol_no"));
            s.setFullName(rs.getString("full_name"));
            s.setCollegeEmail(rs.getString("college_email"));
            a.setStudent(s); // Assuming a new HrStudent field in HrApplication
            return a;
        });

        // Generate CSV
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=applications_" + pldId + ".csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.write("Application ID,Date,Status,Roll No,Full Name,Email\n");
            for (HrApplication app : applications) {
                writer.write(String.format("%d,%s,%s,%s,%s,%s\n",
                        app.getAppId(), app.getAppDate(), app.getAppStatus(),
                        app.getStudent().getRolNo(), app.getStudent().getFullName(), app.getStudent().getCollegeEmail()));
            }
        } catch (IOException e) {
            // Log error
        }
    }
}