package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.HiringPhase;
import com.cpt.model.Phase;
import com.cpt.model.PhaseData;

@Repository
public class HiringPhaseDAO implements HiringPhaseDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final RowMapper<HiringPhase> rowMapper = new RowMapper<>() {
		@Override
		public HiringPhase mapRow(ResultSet rs, int rowNum) throws SQLException {
			HiringPhase hs = new HiringPhase();
			hs.setHphId(rs.getInt("hph_id"));
			hs.setHphPldId(rs.getInt("hph_pld_id"));
			hs.setHphName(rs.getString("hph_name"));
			hs.setHphSequence(rs.getInt("hph_sequence"));
			return hs;
		}
	};

	@Override
	public List<HiringPhase> getAll(int selectedCompanyId) {
		String sql = "SELECT hs.hph_id,HS.hph_pld_id, HS.hph_name, HS.hph_sequence\r\n"
				+ "FROM HIRING_phases HS JOIN PLACEMENT_DRIVES PD ON HS.hph_pld_id = PD.pld_id \r\n"
				+ "JOIN COMPANIES CO ON CO.cmp_id = PD.pld_cmp_id\r\n" + "WHERE PD.pld_cmp_id = ?";

		List<HiringPhase> phases = jdbcTemplate.query(sql, new Object[] { selectedCompanyId }, rowMapper);

		// Now for each phase, fetch the parameters and scores
		for (HiringPhase phase : phases) {
			String criteriaSql = "SELECT hsp.param_name, hsp.threshold_score FROM hiring_phase_parameters hsp\r\n"
					+ "join hiring_phases hs  on hsp.hph_id = hs.hph_id\r\n" + "WHERE  hs.hph_pld_id = ?";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(criteriaSql, phase.getHphPldId());

			Map<String, Integer> criteriaMap = new HashMap<>();
			for (Map<String, Object> row : rows) {
				String param = (String) row.get("hph_parameter");
				Integer score = (Integer) row.get("threshold_score");
				criteriaMap.put(param, score);
			}
			phase.setHm(criteriaMap);
		}

		return phases;
	}

	@Override
	public boolean existsByPldId(int pld_id) {
		String sql = "SELECT COUNT(*) FROM screening_criteria WHERE scr_pld_id =?";
		Integer count = jdbcTemplate.queryForObject(sql, new Object[] { pld_id }, Integer.class);
		System.out.println(count);
		return count != null && count > 0; // Return true if the count is greater than 0, meaning pld_id exists
	}

	@Override
	public Map<String, Integer> getCriteria(String pname) {
		// TODO Auto-generated method stub
		String sql = "select hsp.param_name, hsp.threshold_score\r\n" + "from hiring_phase_parameters hsp \r\n"
				+ "join hiring_phases hs  on hsp.hph_id = hs.hph_id\r\n" + "where hs.hph_name=?";

		return jdbcTemplate.query(sql, new Object[] { pname }, rs -> {
			Map<String, Integer> map = new HashMap<>();
			while (rs.next()) {
				String key = rs.getString("param_name");
				Integer value = rs.getInt("threshold_score");
				map.put(key, value);
			}
			return map;
		});

	}

	// Get Phases for a placement drive (pld_id)

	@Override
	public List<Phase> getPhasesByPldId(int pldId) {
		String sql = "SELECT hph_sequence, hph_name FROM hiring_phases WHERE hph_pld_id = ?";
		return jdbcTemplate.query(sql, new Object[] { pldId }, (rs, rowNum) -> {
			Phase r = new Phase();
			r.setHphId(rs.getInt("hph_sequence"));
			r.setPhaseName(rs.getString("hph_name"));
			return r;
		});
	}

	// Get shortlisted students for a Phase from temp_shortlisted table
	@Override
	public List<PhaseData> getStudentsByPhaseId(int hphId, int pldId) {
		String sql = "SELECT std_id, std_name, score FROM temp_shortlisted WHERE ph_id = ? and pld_id=?";
		return jdbcTemplate.query(sql, new Object[] { hphId, pldId }, (rs, rowNum) -> {
			PhaseData rd = new PhaseData();
			rd.setStudentId(rs.getString("std_id"));
			rd.setStudentName(rs.getString("std_name"));
			rd.setScore(rs.getInt("score"));
			return rd;
		});
	}

	// Get threshold for a hiring phase from hiring_parameters or hiringphases table
	@Override
	public Double getThresholdByPhaseId(int hphId, int pldId) {
		String sql = "SELECT threshold_score FROM hiring_phases WHERE hph_id = ? ";
		Double threshold = jdbcTemplate.queryForObject(sql, new Object[] { hphId }, Double.class);
		return threshold;
	}

	@Override
	public void updateThresholdByPhaseId(int hphid, double newThreshold) {
		String sql = "UPDATE hiring_phases SET threshold_score = ? WHERE hph_id = ?";

		System.out.println("entered update");

		jdbcTemplate.update(sql, newThreshold, hphid);
	}
}