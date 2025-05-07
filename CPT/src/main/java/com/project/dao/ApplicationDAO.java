package com.project.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.model.Application;

@Repository
public class ApplicationDAO {
	private final JdbcTemplate jdbcTemplate;

	public ApplicationDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Application> findByUserId(Short userId) {
		String sql = "SELECT * FROM applications WHERE app_usr_id = ?";
		return jdbcTemplate.query(sql, this::mapRowToApplication, userId);
	}

	public List<Application> findByPlacementDriveId(Short placementDriveId) {
		String sql = "SELECT * FROM applications WHERE app_pld_id = ?";
		return jdbcTemplate.query(sql, this::mapRowToApplication, placementDriveId);
	}

	public void save(Application application) {
		String sql = "INSERT INTO applications (app_usr_id, app_pld_id, app_cmp_id, app_date, app_status) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, application.getUserId(), application.getPlacementDriveId(), application.getCompanyId(),
				application.getApplicationDate(), application.getStatus());
	}

	public void updateStatus(Short id, String status) {
		String sql = "UPDATE applications SET app_status = ? WHERE app_id = ?";
		jdbcTemplate.update(sql, status, id);
	}

	private Application mapRowToApplication(ResultSet rs, int rowNum) throws SQLException {
		Application app = new Application();
		app.setId(rs.getShort("app_id"));
		app.setUserId(rs.getShort("app_usr_id"));
		app.setPlacementDriveId(rs.getShort("app_pld_id"));
		app.setCompanyId(rs.getShort("app_cmp_id"));
		app.setApplicationDate(rs.getDate("app_date") != null ? rs.getDate("app_date").toLocalDate() : null);
		app.setStatus(rs.getString("app_status"));
		return app;
	}
}