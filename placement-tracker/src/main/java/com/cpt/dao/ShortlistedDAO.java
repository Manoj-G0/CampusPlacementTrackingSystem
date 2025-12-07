package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.Shortlisted;

@Repository
public class ShortlistedDAO implements ShortlistedDAOInt {
	JdbcTemplate jdbcTemplate;

	@Autowired
	public ShortlistedDAO(DataSource dataSource) {

		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private final RowMapper<Shortlisted> rowMapper = new RowMapper<Shortlisted>() {
		@Override
		public Shortlisted mapRow(ResultSet rs, int rowNum) throws SQLException {
			Shortlisted list = new Shortlisted();

			list.setPlacementId(rs.getInt("pld_id"));
			list.setStudentId(rs.getString("student_id"));
			list.setEmail(rs.getString("college_email"));

			return list;
		}
	};

	@Override
	public void saveList(int pldId, String stdId) {
		String sql = "INSERT INTO shortlisted (pld_id,student_id) VALUES (?, ?)";
		jdbcTemplate.update(sql, pldId, stdId);
		jdbcTemplate.update("insert into notifications(ntf_usr_id, ntf_message, ntf_date) values(?,?,CURRENT_DATE)",
				stdId, "You Are Selected");

	}

	@Override
	public List<Shortlisted> getShortlisted() {
		String sql = "SELECT FST.PLD_ID, FST.STUDENT_ID,S.COLLEGE_EMAIL FROM shortlisted FST\r\n"
				+ "JOIN STUDENTS S ON S.ROLL_NO = FST.STUDENT_ID;";
		List<Shortlisted> list = jdbcTemplate.query(sql, rowMapper);
		return list;
	}

	@Override
	public boolean check(String stdid) {
		// TODO Auto-generated method stub
		return false;
	}
}
