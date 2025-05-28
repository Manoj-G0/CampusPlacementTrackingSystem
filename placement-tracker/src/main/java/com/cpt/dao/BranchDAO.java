package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.Branch;

@Repository
public class BranchDAO implements BranchDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void save(Branch branch) {
		String sql = "INSERT INTO branches (brn_clg_id, brn_name, brn_desc) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, branch.getBrnClgId(), branch.getBrnName(), branch.getBrnDesc());
	}

	@Override
	public List<Branch> findAll() {
		String sql = "SELECT * FROM branches";
		return jdbcTemplate.query(sql, new BranchRowMapper());
	}

	private static class BranchRowMapper implements RowMapper<Branch> {
		@Override
		public Branch mapRow(ResultSet rs, int rowNum) throws SQLException {
			Branch branch = new Branch();
			branch.setBrnId(rs.getInt("brn_id"));
			branch.setBrnClgId(rs.getInt("brn_clg_id"));
			branch.setBrnName(rs.getString("brn_name"));
			branch.setBrnDesc(rs.getString("brn_desc"));
			return branch;
		}
	}

	@Override
	public Branch findById(int brnId) {
		String sql = "SELECT * FROM branches where brn_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[] { brnId }, new BranchRowMapper());

	}

	@Override
	public void updateBranch(Branch branch) {
		jdbcTemplate.update("update branches set brn_clg_id=?,brn_name=?,brn_desc=? where brn_id =? ",
				branch.getBrnClgId(), branch.getBrnName(), branch.getBrnDesc(), branch.getBrnId());

	}

	@Override
	public String getBranchName(int brn_id) {
		String sql = "SELECT brn_name FROM branches WHERE brn_id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { brn_id }, String.class);

	}
}
