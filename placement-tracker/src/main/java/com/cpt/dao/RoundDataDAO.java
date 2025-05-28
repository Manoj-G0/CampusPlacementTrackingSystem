package com.cpt.dao;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cpt.model.RoundData;
import com.cpt.model.RoundParameter;
import com.cpt.model.StudentRoundStatus;

@Repository
public class RoundDataDAO implements RoundDataDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Method to fetch the round-wise selection status
	@Override
	public List<RoundData> getRoundData() {
		String query = "WITH round_data AS ("
				+ "SELECT r1.pld_id, hp.hph_name AS phase_name, r1.student_id, s.full_name AS student_name, r1.score, "
				+ "CASE WHEN EXISTS (" + "SELECT 1 FROM round_wise_shortlisted r2 " + "WHERE r2.pld_id = r1.pld_id "
				+ "AND r2.student_id = r1.student_id " + "AND r2.phase_id > r1.phase_id"
				+ ") THEN 'Selected' ELSE 'Rejected' END AS status " + "FROM round_wise_shortlisted r1 "
				+ "JOIN students s ON s.roll_no = r1.student_id " + "JOIN hiring_phases hp ON hp.hph_id = r1.phase_id "
				+ ") " + "SELECT * FROM round_data WHERE pld_id = ? ORDER BY pld_id, student_id, phase_name";

		return jdbcTemplate.query(query, new Object[] { 1 }, new BeanPropertyRowMapper<>(RoundData.class));
	}

	@Override
	public Set<String> getRoundNamesByDrive(int pldId) {
		String sql = "SELECT hp.hph_name FROM hiring_phases hp "
				+ "JOIN hiring_phase_parameters rp ON rp.hph_id = hp.hph_id " + "WHERE hp.hph_pld_id = ?";

		return new LinkedHashSet<>(jdbcTemplate.queryForList(sql, new Object[] { pldId }, String.class));
	}

	@Override
	public List<StudentRoundStatus> getStudentRoundDataByDrive(int pldId) {
		String sql = "SELECT " + "s.roll_no AS studentId, " + "s.full_name AS fullName, " + "hp.hph_name AS phaseName, "
				+ "SUM(sr.score) AS totalScore, " + "hp.threshold_score AS threshold, " + "CASE "
				+ "WHEN SUM(sr.score) >= hp.threshold_score THEN 'Selected' " + "ELSE 'Rejected' " + "END AS status "
				+ "FROM eligible_students es " + "JOIN students s ON s.roll_no = es.student_id "
				+ "LEFT JOIN student_results sr ON sr.std_id = s.roll_no AND sr.pld_id = es.pld_id "
				+ "LEFT JOIN hiring_phases hp ON sr.hph_id = hp.hph_id AND hp.hph_pld_id = es.pld_id "
				+ "WHERE es.pld_id = ? "
				+ "GROUP BY s.roll_no, s.full_name, hp.hph_name, hp.threshold_score, hp.hph_sequence "
				+ "HAVING hp.hph_name IS NOT NULL " + "ORDER BY s.roll_no, hp.hph_sequence";

		return jdbcTemplate.query(sql, new Object[] { pldId }, (rs, rowNum) -> {
			StudentRoundStatus status = new StudentRoundStatus();
			status.setStudentId(rs.getString("studentId"));
			status.setFullName(rs.getString("fullName"));
			status.setPhaseName(rs.getString("phaseName"));
			status.setTotalScore(rs.getDouble("totalScore"));
			status.setThreshold(rs.getDouble("threshold"));
			status.setStatus(rs.getString("status"));
			return status;
		});
	}

	@Override
	public int getTotalStudents(int pldId) {
		String sql = "SELECT COUNT(DISTINCT student_id) " + "FROM eligible_students " + "WHERE pld_id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { pldId }, Integer.class);
	}

	@Override
	public List<Map<String, Object>> getPhaseStudentCounts(int pldId) {
		String sql = "SELECT " + "hp.hph_id AS phase_id, " + "hp.hph_sequence AS phase_sequence, "
				+ "hp.hph_name AS phase_name, " + "COUNT(DISTINCT sr.std_id) AS student_count "
				+ "FROM hiring_phases hp "
				+ "LEFT JOIN student_results sr ON sr.hph_id = hp.hph_id AND sr.pld_id = hp.hph_pld_id "
				+ "WHERE hp.hph_pld_id = ? " + "GROUP BY hp.hph_id, hp.hph_sequence, hp.hph_name "
				+ "ORDER BY hp.hph_sequence";

		return jdbcTemplate.queryForList(sql, pldId);
	}

	@Override
	public List<RoundParameter> getStudentDrillDown(String studentId, int pldId) {
		// Query to fetch student drilldown data
		String sql = "SELECT sr.std_id AS studentId, " + "hp.hph_name AS phaseName, "
				+ "hp.threshold_score AS phaseThreshold, " + "p.param_name AS parameter, " + "sr.score, "
				+ "p.threshold_score AS paramThreshold " + "FROM student_results sr "
				+ "JOIN hiring_phases hp ON sr.hph_id = hp.hph_id "
				+ "JOIN hiring_phase_parameters p ON sr.param_id = p.param_id "
				+ "WHERE sr.std_id = ? AND sr.pld_id = ? " + "ORDER BY hp.hph_sequence, p.param_name";

		// Executing the query and mapping the results to the RoundParameter class
		return jdbcTemplate.query(sql, new Object[] { studentId, pldId }, (rs, rowNum) -> {
			// Create new RoundParameter object to hold each record
			RoundParameter param = new RoundParameter();

			// Setting values from result set to the RoundParameter object
			param.setPhaseName(rs.getString("phaseName"));
			param.setParameter(rs.getString("parameter"));
			param.setScore(rs.getInt("score"));
			param.setPhaseThreshold(rs.getDouble("phaseThreshold"));
			param.setParamThreshold(rs.getDouble("paramThreshold"));

			// Return the populated RoundParameter object
			return param;
		});
	}

	@Override
	public List<Map<String, Object>> getStudentsInDrives() {
		String sql = "SELECT DISTINCT s.roll_no AS \"Roll No\", s.full_name AS \"Full Name\", pd.pld_name AS \"Drive Name\"\n"
				+ "FROM students s\n" + "JOIN student_results sr ON s.roll_no = sr.std_id\n"
				+ "JOIN placement_drives pd ON pd.pld_id = sr.pld_id\n" + "ORDER BY s.roll_no;";
		return jdbcTemplate.queryForList(sql);
	}
}
