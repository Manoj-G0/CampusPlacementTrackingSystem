package com.cpt.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TempShortlistedDAO implements TempShortlistedDAOInt {
	JdbcTemplate jdbcTemplate;

	@Autowired
	public TempShortlistedDAO(DataSource dataSource) {

		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void saveList(int pldId, int phase_id, String stdId, String stdName, String ph_name, int score) {
		String sql = "INSERT INTO temp_Shortlisted(pld_id,ph_id,std_Id,std_name,ph_name,score) VALUES (?, ?,?,?,?,?)";
		jdbcTemplate.update(sql, pldId, phase_id, stdId, stdName, ph_name, score);
	}

}
