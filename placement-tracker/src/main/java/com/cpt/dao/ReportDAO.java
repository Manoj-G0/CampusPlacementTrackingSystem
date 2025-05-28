package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.PlacementDrive;

@Repository
public class ReportDAO implements ReportDAOInt {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> getPlacementStats() {
		String sql = "SELECT c.cmp_name AS Company, COUNT(a.app_id) AS Number_of_Students_Placed\n"
				+ "FROM companies c\n" + "JOIN placement_drives d ON c.cmp_id = d.pld_cmp_id\n"
				+ "JOIN applications a ON a.app_pld_id = d.pld_id\n" + "WHERE a.app_status = 'OFFR'\n"
				+ "GROUP BY c.cmp_name\n" + "ORDER BY Number_of_Students_Placed DESC;";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> getDriveDetails() {
		String sql = "SELECT d.pld_id AS \"PlacementID\", d.pld_name AS \"Placement Name\", "
				+ "d.pld_start_date AS \"Start Date\", d.pld_end_date AS \"End Date\", "
				+ "COUNT(a.app_id) AS \"Number of Students Placed\"\n" + "FROM placement_drives d\n"
				+ "LEFT JOIN applications a ON d.pld_id = a.app_pld_id AND a.app_status = 'OFFR'\n"
				+ "GROUP BY d.pld_id, d.pld_name, d.pld_start_date, d.pld_end_date\n" + "ORDER BY d.pld_start_date;";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> getBranchPlacementDetails() {
		String sql = "SELECT b.brn_name AS \"Branch Name\", COUNT(DISTINCT s.roll_no) AS \"Number of Students Placed\"\n"
				+ "FROM branches b\n" + "JOIN students s ON b.brn_id = s.branch_id\n"
				+ "JOIN applications a ON a.app_usr_id = s.roll_no\n" + "WHERE a.app_status = 'OFFR'\n"
				+ "GROUP BY b.brn_name\n" + "ORDER BY \"Number of Students Placed\" DESC;";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> getStudentsPlacementDetails() {
		String sql = "SELECT s.roll_no AS \"Registration Number\", s.full_name AS \"Name\", "
				+ "b.brn_name AS \"Branch\", s.gender AS \"Gender\", s.cgpa AS \"CGPA\", "
				+ "c.cmp_name AS \"Company Placed\", p.pld_name AS \"Drive\"\n" + "FROM students s\n"
				+ "JOIN branches b ON s.branch_id = b.brn_id\n"
				+ "JOIN applications a ON a.app_usr_id = s.roll_no AND a.app_status = 'OFFR'\n"
				+ "JOIN companies c ON a.app_cmp_id = c.cmp_id\n"
				+ "JOIN placement_drives p ON a.app_pld_id = p.pld_id;";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<PlacementDrive> getAllPlacements() {
		String sql = "SELECT * FROM placement_drives;";
		return jdbcTemplate.query(sql, new PlacementMapper());
	}

	@Override
	public List<Map<String, Object>> getRoundwiseShortlisted(int pld_id, int phase_id) {
		String sql = "SELECT s.roll_no AS \"Registration Number\", a.app_id AS \"Application ID\", "
				+ "s.full_name AS \"Name\", b.brn_name AS \"Branch\", s.cgpa AS \"CGPA\", "
				+ "s.gender AS \"Gender\", rws.score AS \"Score\"\n" + "FROM round_wise_shortlisted rws\n"
				+ "JOIN applications a ON a.app_usr_id = rws.student_id\n"
				+ "JOIN students s ON s.roll_no = a.app_usr_id\n" + "JOIN branches b ON b.brn_id = s.branch_id\n"
				+ "WHERE rws.pld_id = ? AND rws.phase_id = ?;";
		return jdbcTemplate.queryForList(sql, pld_id, phase_id);
	}

	@Override
	public List<Map<String, Object>> getRoundwiseAllShortlisted() {
		String sql = "SELECT rws.pld_id AS \"Placement ID\", p.pld_name AS \"Placement Name\", "
				+ "rws.phase_id AS \"Phase ID\", h.hph_name AS \"Phase Name\", "
				+ "s.roll_no AS \"Registration Number\", s.full_name AS \"Name\", "
				+ "b.brn_name AS \"Branch\", rws.score AS \"Score\"\n" + "FROM round_wise_shortlisted rws\n"
				+ "JOIN placement_drives p ON rws.pld_id = p.pld_id\n"
				+ "JOIN hiring_phases h ON rws.phase_id = h.hph_id\n"
				+ "JOIN students s ON rws.student_id = s.roll_no\n" + "JOIN branches b ON s.branch_id = b.brn_id\n"
				+ "ORDER BY rws.pld_id, rws.phase_id, rws.score DESC;";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> getPhasesByDrive(int pldId) {
		String sql = "SELECT hph_id AS phase_id, hph_name AS phase_name, hph_sequence AS phase_sequence "
				+ "FROM hiring_phases " + "WHERE hph_pld_id = ? " + "ORDER BY hph_sequence;";
		return jdbcTemplate.queryForList(sql, pldId);
	}

	public class PlacementMapper implements RowMapper<PlacementDrive> {

		@Override
		public PlacementDrive mapRow(ResultSet rs, int rowNum) throws SQLException {
			PlacementDrive placementDrive = new PlacementDrive();
			placementDrive.setPldId(rs.getInt("pld_id"));
			placementDrive.setCollegeId(rs.getInt("pld_clg_id"));
			placementDrive.setCompanyId(rs.getInt("pld_cmp_id"));
			placementDrive.setName(rs.getString("pld_name"));
			placementDrive.setStartDate(rs.getDate("pld_start_date"));
			placementDrive.setEndDate(rs.getDate("pld_end_date"));
			placementDrive.setStatus(rs.getString("pld_status"));
			placementDrive.setPldSalary(rs.getDouble("pld_package"));
			placementDrive.setPldRole(rs.getString("pld_role_name"));
			return placementDrive;
		}
	}
}