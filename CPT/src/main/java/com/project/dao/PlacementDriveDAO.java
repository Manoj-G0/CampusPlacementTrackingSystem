package com.project.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.model.PlacementDrive;

@Repository
public class PlacementDriveDAO {
	private final JdbcTemplate jdbcTemplate;

	public PlacementDriveDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<PlacementDrive> findAll() {
		String sql = "SELECT * FROM placement_drives";
		return jdbcTemplate.query(sql, this::mapRowToPlacementDrive);
	}

	public PlacementDrive findById(Short id) {
		String sql = "SELECT * FROM placement_drives WHERE pld_id = ?";
		return jdbcTemplate.queryForObject(sql, this::mapRowToPlacementDrive, id);
	}

	public void save(PlacementDrive drive) {
		String sql = "INSERT INTO placement_drives (pld_clg_id, pld_name, pld_start_date, pld_end_date, pld_min_gpa, pld_max_backlogs, pld_allowed_branches, pld_package) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, drive.getCollegeId(), drive.getName(), drive.getStartDate(), drive.getEndDate(),
				drive.getMinGpa(), drive.getMaxBacklogs(), drive.getAllowedBranches(), drive.getPackageAmount());
	}

	private PlacementDrive mapRowToPlacementDrive(ResultSet rs, int rowNum) throws SQLException {
		PlacementDrive drive = new PlacementDrive();
		drive.setId(rs.getShort("pld_id"));
		drive.setCollegeId(rs.getShort("pld_clg_id"));
		drive.setName(rs.getString("pld_name"));
		drive.setStartDate(rs.getDate("pld_start_date") != null ? rs.getDate("pld_start_date").toLocalDate() : null);
		drive.setEndDate(rs.getDate("pld_end_date") != null ? rs.getDate("pld_end_date").toLocalDate() : null);
		drive.setMinGpa(rs.getDouble("pld_min_gpa"));
		drive.setMaxBacklogs(rs.getInt("pld_max_backlogs"));
		drive.setAllowedBranches(rs.getString("pld_allowed_branches"));
		drive.setPackageAmount(rs.getDouble("pld_package"));
		return drive;
	}
}