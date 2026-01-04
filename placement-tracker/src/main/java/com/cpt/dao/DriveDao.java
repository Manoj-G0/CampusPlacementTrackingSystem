package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.Drive;

@Repository
public class DriveDao implements DriveDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Drive> getDrives(LocalDate firstDateOfCurrentMonth) {
		// SQL query to fetch the drives starting from the given first date of the
		// current month
		String query = "SELECT pld_name, pld_start_date, pld_end_date FROM placement_drives WHERE pld_start_date >= ?";

		// Using JdbcTemplate to execute the query and map the results
		List<Drive> dlist = jdbcTemplate.query(query, new Object[] { firstDateOfCurrentMonth }, new DriveRowMapper());
		return dlist;
	}

	// RowMapper to map the ResultSet to Drive object
	private static class DriveRowMapper implements RowMapper<Drive> {
		@Override
		public Drive mapRow(ResultSet rs, int rowNum) throws SQLException {
			Drive drive = new Drive();
			drive.setPldName(rs.getString("pld_name"));
			drive.setPldStartDate(rs.getDate("pld_start_date").toLocalDate());
			drive.setPldEndDate(rs.getDate("pld_end_date").toLocalDate());
			return drive;
		}
	}
}
