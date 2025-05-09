package com.cpt.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class HrJobDescriptionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JavaMailSender mailSender;

    public Map<String, Object> getHrDetails(String usrId) {
        String sql = "SELECT cmp_id, clg_id, hr_name FROM hr WHERE hr_id = ?";
        return jdbcTemplate.queryForMap(sql, usrId);
    }

    public List<Map<String, Object>> getBranches(int clgId) {
        String sql = "SELECT brn_id, brn_name FROM branches WHERE brn_clg_id = ?";
        return jdbcTemplate.queryForList(sql, clgId);
    }

    public int addPlacementDrive(String pldName, String pldRole, BigDecimal pldPackage, Date pldStartDate, Date pldEndDate, int cmpId, int clgId) {
        String sql = "INSERT INTO placement_drives (pld_clg_id, pld_cmp_id, pld_name, pld_role, pld_package, pld_start_date, pld_end_date, pld_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'NOT ASSIGNED') RETURNING pld_id";
        return jdbcTemplate.queryForObject(sql, new Object[]{clgId, cmpId, pldName, pldRole, pldPackage, pldStartDate, pldEndDate}, Integer.class);
    }

    public void addScreeningCriteria(int pldId, BigDecimal scrMinGpa, int scrMinBacklogs, List<Integer> scrBrnId, String scrGender) {
        String sql = "INSERT INTO screening_criteria (scr_pld_id, scr_min_gpa, scr_min_backlogs, scr_brn_id, scr_gender) VALUES (?, ?, ?, ?, ?)";
        for (int brnId : scrBrnId) {
            jdbcTemplate.update(sql, pldId, scrMinGpa, scrMinBacklogs, brnId, scrGender);
        }
    }

    public void addHiringPhases(int pldId, List<String> hphName, List<BigDecimal> cutoffScore) {
        String sql = "INSERT INTO hiring_phases (hph_pld_id, hph_name, hph_sequence, cutoff_score) VALUES (?, ?, ?, ?)";
        for (int i = 0; i < hphName.size(); i++) {
            jdbcTemplate.update(sql, pldId, hphName.get(i), i + 1, cutoffScore.get(i));
        }
    }

    public void notifyAdmins(int clgId, String pldName, String notificationMessage) {
        String adminSql = "SELECT po_id FROM admins WHERE clg_id = ?";
        List<String> adminIds = jdbcTemplate.queryForList(adminSql, new Object[]{clgId}, String.class);
        String notifySql = "INSERT INTO notifications (ntf_usr_id, ntf_message, ntf_date, ntf_read) VALUES (?, ?, CURRENT_DATE, FALSE)";
        for (String adminId : adminIds) {
            jdbcTemplate.update(notifySql, adminId, notificationMessage + ": " + pldName);
        }
    }

    public void sendAdminEmails(int clgId, String pldName, String pldRole, BigDecimal pldPackage, String emailSubject, String emailBodyPrefix) {
        String adminSql = "SELECT po_id FROM admins WHERE clg_id = ?";
        List<String> adminIds = jdbcTemplate.queryForList(adminSql, new Object[]{clgId}, String.class);
        String emailSql = "SELECT hr_email FROM hr WHERE hr_id = ?";
        for (String adminId : adminIds) {
            String adminEmail = jdbcTemplate.queryForObject(emailSql, new Object[]{adminId}, String.class);
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(adminEmail);
            mail.setSubject(emailSubject);
            mail.setText(emailBodyPrefix + "\nName: " + pldName + "\nRole: " + pldRole + "\nPackage: " + pldPackage);
            mailSender.send(mail);
        }
    }
}