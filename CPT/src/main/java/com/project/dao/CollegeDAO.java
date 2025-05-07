package com.project.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.model.College;

@Repository
public class CollegeDAO {
	private final JdbcTemplate jdbcTemplate;

	public CollegeDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<College> findAll() {
		String sql = "SELECT * FROM colleges";
		return jdbcTemplate.query(sql, this::mapRowToCollege);
	}

	public void save(College college) {
		String sql = "INSERT INTO colleges (clg_name, clg_address, clg_contact) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, college.getName(), college.getAddress(), college.getContact());
	}

	public void update(College college) {
		String sql = "UPDATE colleges SET clg_name = ?, clg_address = ?, clg_contact = ? WHERE clg_id = ?";
		jdbcTemplate.update(sql, college.getName(), college.getAddress(), college.getContact(), college.getId());
	}

	private College mapRowToCollege(ResultSet rs, int rowNum) throws SQLException {
		College college = new College();
		college.setId(rs.getShort("clg_id"));
		college.setName(rs.getString("clg_name"));
		college.setAddress(rs.getString("clg_address"));
		college.setContact(rs.getString("clg_contact"));
		return college;
	}
}