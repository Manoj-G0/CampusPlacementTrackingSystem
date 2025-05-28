package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.College;

@Repository
public class CollegeDAO implements CollegeDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void save(College college) {
		String sql = "INSERT INTO colleges (clg_name, clg_address, clg_contact) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, college.getClgName(), college.getClgAddress(), college.getClgContact());
	}

	@Override
	public List<College> findAll() {
		String sql = "SELECT * FROM colleges";
		return jdbcTemplate.query(sql, new CollegeRowMapper());
	}

	@Override
	public void updateCollege(College college) {
		jdbcTemplate.update("update colleges set clg_name=?,clg_address=?,clg_contact=? where clg_id =? ",
				college.getClgName(), college.getClgAddress(), college.getClgContact(), college.getClgId());
	}

	@Override
	public void removeCollege(int id) {
		jdbcTemplate.update("delete from colleges where clg_id = ?", id);
	}

	@Override
	public College getCollegeById(int id) {
		String query = "SELECT * FROM colleges WHERE clg_id = ?";
		return jdbcTemplate.queryForObject(query, new Object[] { id }, new CollegeRowMapper());
	}

	private static class CollegeRowMapper implements RowMapper<College> {
		@Override
		public College mapRow(ResultSet rs, int rowNum) throws SQLException {
			College college = new College();
			college.setClgId(rs.getInt("clg_id"));
			college.setClgName(rs.getString("clg_name"));
			college.setClgAddress(rs.getString("clg_address"));
			college.setClgContact(rs.getString("clg_contact"));
			return college;
		}
	}
}
