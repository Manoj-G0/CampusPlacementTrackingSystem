package com.cpt.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cpt.model.HrHiringPhase;

@Repository
public class HrHiringPhaseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveHiringPhase(HrHiringPhase phase) {
        String sql = "INSERT INTO hiring_phases (hph_pld_id, hph_name, hph_sequence, cutoff_score) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, phase.getHphPldId(), phase.getHphName(), phase.getHphSequence(), phase.getCutoffScore());
    }

    public List<HrHiringPhase> findHiringPhases(int pldId) {
        String sql = "SELECT hph_id, hph_name, hph_sequence, cutoff_score FROM hiring_phases WHERE hph_pld_id = ? ORDER BY hph_sequence";
        return jdbcTemplate.query(sql, new Object[]{pldId}, (rs, rowNum) -> {
            HrHiringPhase phase = new HrHiringPhase();
            phase.setHphId(rs.getInt("hph_id"));
            phase.setHphName(rs.getString("hph_name"));
            phase.setHphSequence(rs.getInt("hph_sequence"));
            phase.setCutoffScore(rs.getBigDecimal("cutoff_score"));
            return phase;
        });
    }
}