package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.Notification;

@Repository
public class NotificationDAO implements NotificationDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void save(Notification notification) {
		String sql = "INSERT INTO notifications (ntf_usr_id, ntf_message, ntf_date, ntf_read) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, notification.getNtfUsrId(), notification.getNtfMessage(), notification.getNtfDate(),
				notification.getNtfRead());
	}

	@Override
	public List<Notification> findByUserId(String usrId) {
		String sql = "SELECT * FROM notifications WHERE ntf_usr_id = ? ORDER BY ntf_date DESC";
		return jdbcTemplate.query(sql, new Object[] { usrId }, new NotificationRowMapper());
	}

	@Override
	public Notification findById(Long ntfId) {
		String sql = "SELECT * FROM notifications WHERE ntf_id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { ntfId }, new NotificationRowMapper());
	}

	@Override
	public void update(Notification notification) {
		String sql = "UPDATE notifications SET ntf_read = ? WHERE ntf_id = ?";
		jdbcTemplate.update(sql, notification.getNtfRead(), notification.getNtfId());
	}

	@Override
	public long countUnreadByUserId(String usrId) {
		String sql = "SELECT COUNT(*) FROM notifications WHERE ntf_usr_id = ? AND ntf_read = FALSE";
		return jdbcTemplate.queryForObject(sql, new Object[] { usrId }, Long.class);
	}

	private static class NotificationRowMapper implements RowMapper<Notification> {
		@Override
		public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
			Notification notification = new Notification();
			notification.setNtfId(rs.getLong("ntf_id"));
			notification.setNtfUsrId(rs.getString("ntf_usr_id"));
			notification.setNtfMessage(rs.getString("ntf_message"));
			notification.setNtfDate(rs.getObject("ntf_date", LocalDate.class));
			notification.setNtfRead(rs.getBoolean("ntf_read"));
			return notification;
		}
	}
}
