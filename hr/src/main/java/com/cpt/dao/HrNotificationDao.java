package com.cpt.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HrNotificationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveNotification(String usrId, String message) {
        String sql = "INSERT INTO notifications (ntf_usr_id, ntf_message, ntf_date, ntf_read) VALUES (?, ?, CURRENT_DATE, FALSE)";
        jdbcTemplate.update(sql, usrId, message);
    }

    public List<String> findAdminIdsByCollege(int clgId) {
        String sql = "SELECT po_id FROM admins WHERE clg_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{clgId}, String.class);
    }
}