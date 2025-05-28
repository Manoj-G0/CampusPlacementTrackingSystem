package com.cpt.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StudentResultsDAO implements StudentResultsDAOInt {
	JdbcTemplate jdbcTemplate;

	@Autowired
	public StudentResultsDAO(DataSource dataSource) {

		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Integer> getParameters(int pldId) {
		String sql = "select hpp.param_id\r\n" + "from hiring_phase_parameters hpp\r\n"
				+ "join hiring_phases hp on hpp.hph_id = hp.hph_id\r\n" + "where hp.hph_pld_id =?";
		List<Integer> ls = jdbcTemplate.queryForList(sql, Integer.class, pldId);
		return ls;

	}

	@Override
	public void saveList(int phase_id, int pldId, String stdId, int param_id, int score) {
		String sql = "INSERT INTO student_results(pld_id,std_id,hph_id,param_id,score) VALUES (?, ?,?, ?,?)";
		jdbcTemplate.update(sql, pldId, stdId, phase_id, param_id, score);
	}
}
