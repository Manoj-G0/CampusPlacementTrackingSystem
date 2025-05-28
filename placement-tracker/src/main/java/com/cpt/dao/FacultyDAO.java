package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.Faculty;

@Repository
public class FacultyDAO implements FacultyDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Faculty> findAvailable() {
		String sql = "SELECT * FROM faculty WHERE fac_id NOT IN (SELECT fac_id FROM faculty_assignments)";
		return jdbcTemplate.query(sql, new FacultyRowMapper());
	}

	@Override
	public void assignFaculty(Integer facId, Integer pldId) {
		String sql = "INSERT INTO faculty_assignments (fac_id, pld_id) VALUES (?, ?)";
		jdbcTemplate.update(sql, facId, pldId);
	}

	private static class FacultyRowMapper implements RowMapper<Faculty> {
		@Override
		public Faculty mapRow(ResultSet rs, int rowNum) throws SQLException {
			Faculty faculty = new Faculty();
			faculty.setFacId(rs.getInt("fac_id"));
			faculty.setFacName(rs.getString("fac_name"));
			faculty.setFacEmail(rs.getString("fac_email"));
			return faculty;
		}
	}
}
