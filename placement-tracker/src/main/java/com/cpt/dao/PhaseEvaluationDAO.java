package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.PhaseEvaluation;

@Repository
public class PhaseEvaluationDAO implements PhaseEvaluationDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<PhaseEvaluation> findByAppId(Integer appId) {
		String sql = "SELECT * FROM phase_evaluations WHERE pev_app_id = ?";
		return jdbcTemplate.query(sql, new Object[] { appId }, new PhaseEvaluationRowMapper());
	}

	private static class PhaseEvaluationRowMapper implements RowMapper<PhaseEvaluation> {
		@Override
		public PhaseEvaluation mapRow(ResultSet rs, int rowNum) throws SQLException {
			PhaseEvaluation evaluation = new PhaseEvaluation();
			evaluation.setPevId(rs.getInt("pev_id"));
			evaluation.setPevAppId(rs.getInt("pev_app_id"));
			evaluation.setPevHphId(rs.getInt("pev_hph_id"));
			evaluation.setPevScore(rs.getDouble("pev_score"));
			return evaluation;
		}
	}
}