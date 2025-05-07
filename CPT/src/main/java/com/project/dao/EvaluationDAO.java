package com.project.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.model.Evaluation;

@Repository
public class EvaluationDAO {
	private final JdbcTemplate jdbcTemplate;

	public EvaluationDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Evaluation> findByApplicationId(Short applicationId) {
		String sql = "SELECT * FROM evaluations WHERE evl_app_id = ?";
		return jdbcTemplate.query(sql, this::mapRowToEvaluation, applicationId);
	}

	public void save(Evaluation evaluation) {
		String sql = "INSERT INTO evaluations (evl_app_id, evl_hph_id, evl_score, evl_comments) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, evaluation.getApplicationId(), evaluation.getPhaseId(), evaluation.getScore(),
				evaluation.getComments());
	}

	private Evaluation mapRowToEvaluation(ResultSet rs, int rowNum) throws SQLException {
		Evaluation evaluation = new Evaluation();
		evaluation.setId(rs.getShort("evl_id"));
		evaluation.setApplicationId(rs.getShort("evl_app_id"));
		evaluation.setPhaseId(rs.getShort("evl_hph_id"));
		evaluation.setScore(rs.getDouble("evl_score"));
		evaluation.setComments(rs.getString("evl_comments"));
		return evaluation;
	}
}