package com.project.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.model.Notification;

@Repository
public class NotificationDAO {
	private final JdbcTemplate jdbcTemplate;

	public NotificationDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Notification> findByUserId(Short userId) {
		String sql = "SELECT * FROM notifications WHERE ntf_usr_id = ?";
		return jdbcTemplate.query(sql, this::mapRowToNotification, userId);
	}

	public void save(Notification notification) {
		String sql = "INSERT INTO notifications (ntf_usr_id, ntf_message, ntf_date, ntf_read) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, notification.getUserId(), notification.getMessage(), notification.getDate(),
				notification.getRead());
	}

	public void updateReadStatus(Short id, Boolean read) {
		String sql = "UPDATE notifications SET ntf_read = ? WHERE ntf_id = ?";
		jdbcTemplate.update(sql, read, id);
	}

	private Notification mapRowToNotification(ResultSet rs, int rowNum) throws SQLException {
		Notification notification = new Notification();
		notification.setId(rs.getShort("ntf_id"));
		notification.setUserId(rs.getShort("ntf_usr_id"));
		notification.setMessage(rs.getString("ntf_message"));
		notification.setDate(rs.getDate("ntf_date") != null ? rs.getDate("ntf_date").toLocalDate() : null);
		notification.setRead(rs.getBoolean("ntf_read"));
		return notification;
	}
}