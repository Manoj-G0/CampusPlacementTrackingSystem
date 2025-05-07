package com.project.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.model.JobDescription;

@Repository
public class JobDescriptionDAO {
	private final JdbcTemplate jdbcTemplate;

	public JobDescriptionDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<JobDescription> findByPlacementDriveId(Short placementDriveId) {
		String sql = "SELECT * FROM job_descriptions WHERE jbd_pld_id = ?";
		return jdbcTemplate.query(sql, this::mapRowToJobDescription, placementDriveId);
	}

	public void save(JobDescription job) {
		String sql = "INSERT INTO job_descriptions (jbd_cmp_id, jbd_pld_id, jbd_role, jbd_package, jbd_min_gpa, jbd_max_backlogs, jbd_allowed_branches, jbd_skills) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, job.getCompanyId(), job.getPlacementDriveId(), job.getRole(), job.getPackageAmount(),
				job.getMinGpa(), job.getMaxBacklogs(), job.getAllowedBranches(), job.getSkills());
	}

	private JobDescription mapRowToJobDescription(ResultSet rs, int rowNum) throws SQLException {
		JobDescription job = new JobDescription();
		job.setId(rs.getShort("jbd_id"));
		job.setCompanyId(rs.getShort("jbd_cmp_id"));
		job.setPlacementDriveId(rs.getShort("jbd_pld_id"));
		job.setRole(rs.getString("jbd_role"));
		job.setPackageAmount(rs.getDouble("jbd_package"));
		job.setMinGpa(rs.getDouble("jbd_min_gpa"));
		job.setMaxBacklogs(rs.getInt("jbd_max_backlogs"));
		job.setAllowedBranches(rs.getString("jbd_allowed_branches"));
		job.setSkills(rs.getString("jbd_skills"));
		return job;
	}
}