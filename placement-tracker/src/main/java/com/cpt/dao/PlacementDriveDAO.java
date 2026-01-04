package com.cpt.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.cpt.model.Branch;
import com.cpt.model.PlacementDrive;
import com.cpt.model.ScreeningCriteria;
import com.cpt.model.Student;

@Repository
public class PlacementDriveDAO implements PlacementDriveDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void save(PlacementDrive drive) {
		String sql = "INSERT INTO placement_drives (pld_clg_id, pld_cmp_id, pld_name, pld_start_date, pld_end_date, pld_status,pld_role_name,pld_package) VALUES (?, ?, ?, ?, ?, ?,?,?)";
		jdbcTemplate.update(sql, drive.getCollegeId(), drive.getCompanyId(), drive.getName(), drive.getStartDate(),
				drive.getEndDate(), drive.getStatus(), drive.getPldRole(), drive.getPldSalary());
	}

	@Override
	public List<PlacementDrive> findUpcomingDrives() {
		String sql = "SELECT * FROM placement_drives WHERE pld_start_date > CURRENT_DATE";
		return jdbcTemplate.query(sql, new PlacementDriveRowMapper());
	}

	@Override
	public List<PlacementDrive> findByCompanyId(Integer cmpId) {
		String sql = "SELECT * FROM placement_drives pd " + "WHERE pd.pld_cmp_id = ? AND "
				+ "pd.pld_end_date >= CURRENT_DATE";

		return jdbcTemplate.query(sql, new PlacementDriveRowMapper(), cmpId);
	}

	@Override
	public List<PlacementDrive> findByOngoingCompanyId(Integer cmpId) {
		String sql = "SELECT * FROM placement_drives pd " + "WHERE pd.pld_cmp_id = ? AND "
				+ "pd.pld_start_date <= CURRENT_DATE and pd.pld_end_date >= CURRENT_DATE";

		return jdbcTemplate.query(sql, new PlacementDriveRowMapper(), cmpId);
	}

	@Override
	public PlacementDrive findById(Integer pldId) {
		String sql = "SELECT * FROM placement_drives WHERE pld_id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { pldId }, new PlacementDriveRowMapper());
	}

	@Override
	public List<PlacementDrive> findAll() {
		String sql = "SELECT * FROM placement_drives";
		return jdbcTemplate.query(sql, new PlacementDriveRowMapper());
	}

	@Override
	public long countActive() {
		String sql = "SELECT COUNT(*) FROM placement_drives WHERE pld_start_date <= CURRENT_DATE AND pld_end_date >= CURRENT_DATE ";
		return jdbcTemplate.queryForObject(sql, Long.class);
	}

	private static class PlacementDriveRowMapper implements RowMapper<PlacementDrive> {
		@Override
		public PlacementDrive mapRow(ResultSet rs, int rowNum) throws SQLException {
			PlacementDrive drive = new PlacementDrive();
			drive.setPldId(rs.getInt("pld_id"));
			drive.setCollegeId(rs.getInt("pld_clg_id"));
			drive.setCompanyId(rs.getInt("pld_cmp_id"));
			drive.setName(rs.getString("pld_name"));
			drive.setStartDate(rs.getDate("pld_start_date"));
			drive.setEndDate(rs.getDate("pld_end_date"));
			drive.setStatus(rs.getString("pld_status"));
			return drive;
		}
	}

	@Override
	public void updateDrive(PlacementDrive drive) {
		jdbcTemplate.update(
				"update placement_drives set pld_clg_id=?,pld_cmp_id=?,pld_name=?,pld_start_date=?,pld_end_date=?,pld_status=? where pld_id =? ",
				drive.getCollegeId(), drive.getCompanyId(), drive.getName(), drive.getStartDate(), drive.getEndDate(),
				drive.getStatus(), drive.getPldId());
	}

	@Override
	public void deleteDrive(int pldId) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM placement_drives WHERE pld_id = ?";
		jdbcTemplate.update(sql, pldId);

	}

	public List<Map<String, Object>> getCompanyPlacements() {
		String sql = "SELECT c.name AS company_name, b.name AS branch_name, "
				+ "SUM(CASE WHEN s.gender = 'M' THEN 1 ELSE 0 END) AS male_count, "
				+ "SUM(CASE WHEN s.gender = 'F' THEN 1 ELSE 0 END) AS female_count, " + "COUNT(*) AS total_count "
				+ "FROM student_applications sa " + "JOIN students s ON sa.student_id = s.student_id "
				+ "JOIN placement_drives pd ON sa.app_pld_id = pd.pld_id "
				+ "JOIN companies c ON pd.company_id = c.company_id " + "JOIN branches b ON s.branch_id = b.brn_id "
				+ "WHERE sa.status = 'Placed' " + "GROUP BY c.company_id, b.brn_id";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public Map<Integer, Map<String, Double>> getRoundSuccessRates() {
		String sql = "SELECT pd.pld_id, r.round_name, "
				+ "SUM(CASE WHEN e.status = 'Pass' THEN 1 ELSE 0 END) * 100.0 / COUNT(*) AS success_rate "
				+ "FROM evaluations e " + "JOIN rounds r ON e.round_id = r.round_id "
				+ "JOIN placement_drives pd ON r.pld_id = pd.pld_id " + "GROUP BY pd.pld_id, r.round_id";
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		Map<Integer, Map<String, Double>> successRates = new HashMap<>();
		for (Map<String, Object> row : results) {
			Integer pldId = ((Number) row.get("pld_id")).intValue();
			String roundName = (String) row.get("round_name");
			Double rate = ((Number) row.get("success_rate")).doubleValue();
			successRates.computeIfAbsent(pldId, k -> new HashMap<>()).put(roundName, rate);
		}
		return successRates;
	}

	@Override
	public List<Map<String, Object>> getAllDrives() {
		String sql = "SELECT pd.pld_id, pd.pld_name, pd.pld_start_date, pd.pld_end_date, "
				+ "c.cmp_name, cl.clg_address, " + "COUNT(a.app_id) as applicant_count " + "FROM placement_drives pd "
				+ "LEFT JOIN companies c ON pd.pld_cmp_id = c.cmp_id "
				+ "LEFT JOIN colleges cl ON pd.pld_clg_id = cl.clg_id "
				+ "LEFT JOIN applications a ON pd.pld_id = a.app_pld_id "
				+ "GROUP BY pd.pld_id, pd.pld_name, pd.pld_start_date, pd.pld_end_date, c.cmp_name, cl.clg_address";
		try {
			return jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			System.err.println("Error fetching drives in DAO: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	@Override
	public Map<String, Object> getDriveDetails(int pldId) {

		String sql = "SELECT pd.pld_name, pd.pld_start_date, pd.pld_status," + "c.cmp_name, cl.clg_address,"
				+ "COUNT(a.app_id) as applicant_count " + "FROM placement_drives pd "
				+ "LEFT JOIN companies c ON pd.pld_cmp_id = c.cmp_id  "
				+ "LEFT JOIN colleges cl ON pd.pld_clg_id = cl.clg_id  "
				+ "LEFT JOIN applications a ON pd.pld_id = a.app_pld_id " + "WHERE pd.pld_id = ? "
				+ "GROUP BY pd.pld_name, pd.pld_start_date, pd.pld_status, c.cmp_name, cl.clg_address";

		Map<String, Object> result;

		try {
			result = jdbcTemplate.queryForMap(sql, pldId);
		} catch (Exception e) {
			// Handle case where no drive is found
			result = new HashMap<>();
			result.put("pld_name", "Unknown");
			result.put("cmp_name", "N/A");
			result.put("pld_start_date", null);
			result.put("clg_location", "N/A");
			result.put("applicant_count", 0L);
			result.put("pld_status", "N/A");
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getUpcomingDrives() {
		String sql = "SELECT pd.pld_id, pd.pld_name, pd.pld_start_date, pd.pld_end_date, "
				+ "c.cmp_name, cl.clg_address, " + "COUNT(a.app_id) as applicant_count " + "FROM placement_drives pd "
				+ "LEFT JOIN companies c ON pd.pld_cmp_id = c.cmp_id "
				+ "LEFT JOIN colleges cl ON pd.pld_clg_id = cl.clg_id "
				+ "LEFT JOIN applications a ON pd.pld_id = a.app_pld_id " + "WHERE pd.pld_start_date > CURRENT_DATE "
				+ "GROUP BY pd.pld_id, pd.pld_name, pd.pld_start_date, pd.pld_end_date, c.cmp_name, cl.clg_address";
		return jdbcTemplate.queryForList(sql);
	}

	// public List<Map<String, Object>> getAttendedDrives(String usrId) {
	// String sql = "SELECT pd.pld_id, pd.pld_name, pd.pld_start_date, pd.pld_end_date, " +
	// "c.cmp_name, cl.clg_address, " +
	// "COUNT(a.app_id) as applicant_count " +
	// "FROM placement_drives pd " +
	// "LEFT JOIN companies c ON pd.pld_cmp_id = c.cmp_id " +
	// "LEFT JOIN colleges cl ON pd.pld_clg_id = cl.clg_id " +
	// "LEFT JOIN applications a ON pd.pld_id = a.app_pld_id " +
	// "JOIN attended_drives ad ON pd.pld_id = ad.pld_id " +
	// "WHERE ad.usr_id = ? " +
	// "GROUP BY pd.pld_id, pd.pld_name, pd.pld_start_date, pd.pld_end_date, c.cmp_name, cl.clg_address";
	// try {
	// return jdbcTemplate.queryForList(sql, usrId);
	// } catch (Exception e) {
	// System.err.println("Error fetching attended drives in DAO: " + e.getMessage());
	// return new ArrayList<>();
	// }
	// }
	@Override
	public List<Map<String, Object>> getAttendedDrives(String usrId) {
		String sql = "SELECT pd.pld_id, pd.pld_name " + "FROM placement_drives pd "
				+ "JOIN attended_drives ad ON pd.pld_id = ad.pld_id " + "WHERE ad.usr_id = ?";
		try {
			List<Map<String, Object>> drives = jdbcTemplate.queryForList(sql, usrId);
			return drives;
		} catch (Exception e) {
			System.err.println("DAO: Error fetching attended drives: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	@Override
	public List<Map<String, Object>> getDriveScores(String usrId, Integer pldId) {
		String sql = "SELECT hp.hph_name, hp.hph_sequence, rws.score " + "FROM round_wise_shortlisted rws "
				+ "JOIN hiring_phases hp ON rws.phase_id = hp.hph_id AND rws.pld_id = hp.hph_pld_id "
				+ "WHERE rws.student_id = ? AND rws.pld_id = ? " + "ORDER BY hp.hph_sequence";
		try {
			List<Map<String, Object>> scores = jdbcTemplate.queryForList(sql, usrId, pldId);
			return scores;
		} catch (Exception e) {
			System.err.println("DAO: Error fetching drive scores: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	@Override
	public void updateDriveSchedule(PlacementDrive drive) {
		String sql = "UPDATE placement_drives SET start_date = ?, end_date = ? WHERE pld_id = ?";
		jdbcTemplate.update(sql, drive.getStartDate(), drive.getEndDate(), drive.getPldId());
	}

	String SQL_ADD_HIRING_DETAILS = "insert into hiring_phases(hph_pld_id,hph_name,hph_sequence,hph_parameter,total_score,threshold_score) values(?,?,?,?,?,?)";

	String SQL_ADD_PARAMETERS = "insert into hiring_phase_parameters(hph_id,param_name,threshold_score,total_score) values(?,?,?,?)";

	String SQL_ADD_PHASES = "insert into hiring_phases(hph_pld_id,hph_name,hph_sequence,total_score,threshold_score) values(?,?,?,?,?)";

	public String getCompany(String usr) {

		String sql = "select c.cmp_name from hr h  inner join companies c on h.cmp_id = c.cmp_id where hr_id = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { usr }, String.class);

	}

	@Override
	public List<Branch> getBranchDetails() {
		String sql = "SELECT brn_id, brn_name FROM branches";

		return jdbcTemplate.query(sql, new RowMapper<Branch>() {
			@Override
			public Branch mapRow(ResultSet rs, int rowNum) throws SQLException {
				// Creating a simplified Branch object with only brn_id and brn_name
				Branch branch = new Branch();
				branch.setBrnId(rs.getInt("brn_id"));
				branch.setBrnName(rs.getString("brn_name"));
				return branch;
			}
		});
	}

	@Override
	public int addDetailsPrameters(int pld_id, String name, double score, double threshold) {
		return jdbcTemplate.update(SQL_ADD_PARAMETERS, pld_id, name, score, threshold);

	}

	@Override
	public int addDetailsPhases(int pld_id, String roundName, int sequence, double totalScore, double totalThreshold) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		// String sql = "INSERT INTO hiring_phases (pld_id, round_name, sequence,
		// total_score, total_threshold) "
		// + "VALUES (?, ?, ?, ?, ?)";

		String sql = "INSERT INTO hiring_phases (hph_pld_id, hph_name, hph_sequence, total_score, threshold_score) VALUES(?, ?, ?, ?, ?)";

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, pld_id);
			ps.setString(2, roundName);
			ps.setInt(3, sequence);
			ps.setDouble(4, totalScore);
			ps.setDouble(5, totalThreshold);
			return ps;
		}, keyHolder);

		// Try to get the generated hph_id
		Map<String, Object> keys = keyHolder.getKeys();
		Number hphId = null;

		if (keys != null && keys.containsKey("hph_id")) {
			hphId = (Number) keys.get("hph_id");
		} else {
			hphId = keyHolder.getKey(); // fallback
		}

		return hphId != null ? hphId.intValue() : -1;
	}

	private static final String GET_ALL_STUDENTS_SQL = "SELECT roll_no, full_name, branch_id, college_id, gender, status, cgpa, backlogs, college_email FROM students";

	String SQL_ADD_screening_DETAILS = "insert into screening_criteria (scr_brn_id,scr_min_gpa,scr_pld_id,scr_min_backlogs,scr_gender) values(?,?,?,?,?)";

	String SQL_ELIGIBLE_STUDENTS = "insert into eligible_students(student_id,pld_id) VALUES(?,?)";

	// RowMapper to map rows of ResultSet to Student object
	private RowMapper<Student> studentRowMapper = (rs, rowNum) -> {
		Student student = new Student();
		student.setRollNo(rs.getString("roll_no"));
		student.setFullName(rs.getString("full_name"));
		student.setBranchId(rs.getLong("branch_id"));
		student.setClgId(rs.getLong("college_id"));
		student.setGender(rs.getString("gender"));
		student.setStatus(rs.getString("status"));
		student.setCgpa(rs.getDouble("cgpa"));
		student.setBacklogs(rs.getInt("backlogs"));
		student.setCollegeEmail(rs.getString("college_email"));
		return student;
	};

	@Override
	public List<Student> getAllStudents() {
		return jdbcTemplate.query(GET_ALL_STUDENTS_SQL, studentRowMapper);
	}

	@Override
	public int addCriteriaDetails(ScreeningCriteria sc, int pld_id) {
		return jdbcTemplate.update(SQL_ADD_screening_DETAILS, sc.getBranch(), sc.getMinCgpa(), pld_id, sc.getBacklogs(),
				sc.getGender());

	}

	@Override
	public String getBranchName(int brn_id) {
		String sql = "SELECT brn_name FROM branches WHERE brn_id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { brn_id }, String.class);

	}

	@Override
	public int addEligibleStudents(String rollno, int pld_id) {

		return jdbcTemplate.update(SQL_ELIGIBLE_STUDENTS, rollno, pld_id);

	}

	@Override
	public List<PlacementDrive> getPlacementDrivesByCompany(String hrId) {
		String sql = "SELECT pld_id, pld_name FROM placement_drives WHERE pld_cmp_id =\r\n"
				+ "(SELECT cmp_id FROM hr WHERE hr_id = ? ) AND pld_start_date >= CURRENT_DATE;";
		return jdbcTemplate.query(sql, new Object[] { hrId },
				(rs, rowNum) -> new PlacementDrive(rs.getInt("pld_id"), rs.getString("pld_name")));

	}
}