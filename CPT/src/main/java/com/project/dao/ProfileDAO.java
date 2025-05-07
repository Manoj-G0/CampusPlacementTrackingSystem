package com.project.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.model.Profile;

@Repository
public class ProfileDAO {
	private final JdbcTemplate jdbcTemplate;

	public ProfileDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Profile findByUserId(Short userId) {
		String sql = "SELECT * FROM profiles WHERE prf_usr_id = ?";
		return jdbcTemplate.queryForObject(sql, this::mapRowToProfile, userId);
	}

	public void save(Profile profile) {
		String sql = "INSERT INTO profiles (prf_usr_id, prf_brn_id, prf_gpa, prf_backlogs, prf_updated) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, profile.getUserId(), profile.getBranchId(), profile.getGpa(), profile.getBacklogs(),
				profile.getUpdated());
	}

	private Profile mapRowToProfile(ResultSet rs, int rowNum) throws SQLException {
		Profile profile = new Profile();
		profile.setId(rs.getShort("prf_id"));
		profile.setUserId(rs.getShort("prf_usr_id"));
		profile.setBranchId(rs.getShort("prf_brn_id"));
		profile.setGpa(rs.getDouble("prf_gpa"));
		profile.setBacklogs(rs.getInt("prf_backlogs"));
		profile.setUpdated(rs.getDate("prf_updated") != null ? rs.getDate("prf_updated").toLocalDate() : null);
		return profile;
	}
}