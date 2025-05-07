package com.project.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.model.HiringPhase;

@Repository
public class HiringPhaseDAO {
	private final JdbcTemplate jdbcTemplate;

	public HiringPhaseDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<HiringPhase> findByPlacementDriveId(Short placementDriveId) {
		String sql = "SELECT * FROM hiring_phases WHERE hph_pld_id = ?";
		return jdbcTemplate.query(sql, this::mapRowToHiringPhase, placementDriveId);
	}

	public void save(HiringPhase phase) {
		String sql = "INSERT INTO hiring_phases (hph_pld_id, hph_name, hph_sequence) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, phase.getPlacementDriveId(), phase.getName(), phase.getSequence());
	}

	private HiringPhase mapRowToHiringPhase(ResultSet rs, int rowNum) throws SQLException {
		HiringPhase phase = new HiringPhase();
		phase.setId(rs.getShort("hph_id"));
		phase.setPlacementDriveId(rs.getShort("hph_pld_id"));
		phase.setName(rs.getString("hph_name"));
		phase.setSequence(rs.getInt("hph_sequence"));
		return phase;
	}
}