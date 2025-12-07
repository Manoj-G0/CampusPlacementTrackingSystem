package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.RoundWiseShortlisted;

@Repository
public class RoundWiseShortlistedDAO implements RoundWiseShortlistedDAOInt {
	JdbcTemplate jdbcTemplate;

	@Autowired
	public RoundWiseShortlistedDAO(DataSource dataSource) {

		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private final RowMapper<RoundWiseShortlisted> rowMapper = new RowMapper<>() {
		@Override
		public RoundWiseShortlisted mapRow(ResultSet rs, int rowNum) throws SQLException {
			RoundWiseShortlisted list = new RoundWiseShortlisted();

			list.setPlacementId(rs.getInt("pld_id"));
			list.setStudentId(rs.getString("student_id"));
			list.setPhaseId(rs.getInt("phase_id"));
			list.setScore(rs.getInt("score"));

			return list;
		}
	};

	@Override
	public void saveList(int phase_id, int pldId, String stdId, int score, String pname, int selectedCompanyId) {
		System.out.println(phase_id + " " + pldId + " " + stdId + " " + score + " " + pname + " " + selectedCompanyId);
		String sql = "INSERT INTO round_wise_shortlisted(phase_id,pld_id,student_id,score) VALUES (?, ?, ?,?)";
		jdbcTemplate.update(sql, phase_id, pldId, stdId, score);

		String sql1 = "select distinct c.cmp_name\r\n" + "from companies c\r\n"
				+ "join placement_drives pd on c.cmp_id = pd.pld_cmp_id\r\n"
				+ "join hiring_phases hp on hp.hph_pld_id = pd.pld_id where c.cmp_id=?";
		String company = jdbcTemplate.queryForObject(sql1, new Object[] { selectedCompanyId }, String.class);

		jdbcTemplate.update("insert into notifications(ntf_usr_id, ntf_message, ntf_date) values(?,?,CURRENT_DATE)",
				stdId, "I am Pleased to inform you that you have been Shortlisted for the " + pname + " at " + company);
	}

	@Override
	public List<RoundWiseShortlisted> getAllShortlisted() {
		String sql = "select * from round_wise_shortlisted";
		return jdbcTemplate.query(sql, rowMapper);
	}
}
