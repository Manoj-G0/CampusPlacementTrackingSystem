package com.cpt.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cpt.model.CompanyTeam;

@Repository
public class CompanyTeamDAO implements CompanyTeamDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void save(CompanyTeam team) {
		String sql = "INSERT INTO company_teams (ctm_cmp_id, ctm_name, ctm_contact) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, team.getCtmCmpId(), team.getCtmName(), team.getCtmRole());
	}

}
