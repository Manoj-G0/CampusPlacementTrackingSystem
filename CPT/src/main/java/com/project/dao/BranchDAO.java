package com.project.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.model.Branch;

@Repository
public class BranchDAO {
	private final JdbcTemplate jdbcTemplate;

	public BranchDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Branch> findAll() {
		String sql = "SELECT * FROM branches";
		return jdbcTemplate.query(sql, this::mapRowToBranch);
	}

	public void save(Branch branch) {
		String sql = "INSERT INTO branches (brn_clg_id, brn_name, brn_description) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, branch.getCollegeId(), branch.getName(), branch.getDescription());
	}

	private Branch mapRowToBranch(ResultSet rs, int rowNum) throws SQLException {
		Branch branch = new Branch();
		branch.setId(rs.getShort("brn_id"));
		branch.setCollegeId(rs.getShort("brn_clg_id"));
		branch.setName(rs.getString("brn_name"));
		branch.setDescription(rs.getString("brn_description"));
		return branch;
	}
}