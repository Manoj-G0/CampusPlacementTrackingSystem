package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.Application;
import com.cpt.model.ApplicationDTO;
import com.cpt.model.Company;
import com.cpt.model.CompanyTeam;
import com.cpt.model.HR;
import com.cpt.model.HiringPhase;
import com.cpt.model.PlacementDrive;
import com.cpt.model.Resume;
import com.cpt.model.RoundWiseShortlisted;
import com.cpt.model.Shortlisted;

@Repository
public class HRDAO implements HRDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static class HRRowMapper implements RowMapper<HR> {
		@Override
		public HR mapRow(ResultSet rs, int rowNum) throws SQLException {
			HR hr = new HR();
			hr.setHrId(rs.getString("hr_id"));
			hr.setHrName(rs.getString("hr_name"));
			hr.setCmpId(rs.getInt("cmp_id"));
			hr.setClgId(rs.getInt("clg_id"));
			hr.setHrEmail(rs.getString("hr_email"));
			return hr;
		}
	}

	// HR Integration
	@Override
	public void updateApplicationStatus(int pldId, String studentId, String status, String remarks) {
		String sql = "UPDATE applications SET app_status = ?,remarks=? WHERE app_usr_id = ? AND app_pld_id = ?";
		jdbcTemplate.update(sql, status, remarks, studentId, pldId);
	}

	// Find HR by ID
	@Override
	public HR findById(String id) {
		String sql = "SELECT * FROM hr WHERE hr_id = ?";
		return jdbcTemplate.queryForObject(sql, new HRRowMapper(), id);
	}

	// Find Company by HR ID
	@Override
	public Company findCompanyByHrId(String hrId) {
		String sql = "SELECT c.* FROM companies c " + "JOIN hr h ON c.cmp_id = h.cmp_id " + "WHERE h.hr_id = ?";
		return jdbcTemplate.queryForObject(sql, new CompanyRowMapper(), hrId);
	}

	// Find Teams by Company ID
	@Override
	public List<CompanyTeam> findTeamsByCompany(int cmpId) {
		String sql = "SELECT * FROM company_teams WHERE ctm_cmp_id = ?";
		return jdbcTemplate.query(sql, new CompanyTeamRowMapper(), cmpId);
	}

	private static class CompanyRowMapper implements RowMapper<Company> {
		@Override
		public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
			Company company = new Company();
			company.setCmpId(rs.getInt("cmp_id"));
			company.setCmpCctId(rs.getObject("cmp_cct_id", Integer.class));
			company.setCmpName(rs.getString("cmp_name"));
			company.setCmpDesc(rs.getString("cmp_desc"));
			return company;
		}
	}

	private static class CompanyTeamRowMapper implements RowMapper<CompanyTeam> {
		@Override
		public CompanyTeam mapRow(ResultSet rs, int rowNum) throws SQLException {
			CompanyTeam team = new CompanyTeam();
			team.setCtmId(rs.getInt("ctm_id"));
			team.setCtmCmpId(rs.getInt("ctm_cmp_id"));
			team.setCtmName(rs.getString("ctm_name"));
			team.setCtmContact(rs.getString("ctm_contact"));
			return team;
		}
	}

	// Find Ongoing Placement Drives by Company ID
	@Override
	public List<PlacementDrive> findOngoingDrivesByCompany(int cmpId) {
		String sql = "SELECT * FROM placement_drives pd "
				+ "WHERE pd.pld_cmp_id = ? AND pd.pld_start_date <= CURRENT_DATE "
				+ "AND pd.pld_end_date >= CURRENT_DATE";
		return jdbcTemplate.query(sql, driveMapper, cmpId);
	}

	private final RowMapper<PlacementDrive> driveMapper = (rs, rowNum) -> {
		PlacementDrive drive = new PlacementDrive();
		drive.setPldId(rs.getInt("pld_id"));
		drive.setCollegeId(rs.getInt("pld_clg_id"));
		drive.setCompanyId(rs.getInt("pld_cmp_id"));
		drive.setName(rs.getString("pld_name"));
		drive.setStartDate(rs.getDate("pld_start_date"));
		drive.setEndDate(rs.getDate("pld_end_date"));
		drive.setStatus(rs.getString("pld_status"));
		return drive;
	};

	// Find Completed Placement Drives by Company ID
	@Override
	public List<PlacementDrive> findCompletedDrivesByCompany(int cmpId) {
		String sql = "SELECT DISTINCT pd.* FROM placement_drives pd "
				+ "WHERE pd.pld_cmp_id = ? AND pd.pld_end_date < CURRENT_DATE";
		return jdbcTemplate.query(sql, driveMapper, cmpId);
	}

	// Find Upcoming Placement Drives by Company ID
	@Override
	public List<PlacementDrive> findUpcomingDrivesByCompany(int cmpId) {
		String sql = "SELECT DISTINCT pd.* FROM placement_drives pd "
				+ "WHERE pd.pld_cmp_id = ? AND pd.pld_start_date > CURRENT_DATE";
		return jdbcTemplate.query(sql, driveMapper, cmpId);
	}

	// Get Applications by Placement Drive ID
	@Override
	public List<ApplicationDTO> getApplicationsByPldId(int pldId) {
		String sql = "SELECT a.app_id, a.app_usr_id, a.app_pld_id, a.app_status, "
				+ "s.full_name, s.branch_id, s.cgpa, s.status, s.college_email " + "FROM applications a "
				+ "JOIN students s ON a.app_usr_id = s.roll_no " + "WHERE a.app_pld_id = ?";
		return jdbcTemplate.query(sql, new ApplicationDTORowMapper(), pldId);
	}

	private static class ApplicationDTORowMapper implements RowMapper<ApplicationDTO> {
		@Override
		public ApplicationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ApplicationDTO application = new ApplicationDTO();
			application.setAppId(rs.getLong("app_id"));
			application.setAppUsrId(rs.getString("app_usr_id"));
			application.setAppPldId(rs.getInt("app_pld_id"));
			application.setApplicationStatus(rs.getString("app_status"));
			application.setFullName(rs.getString("full_name"));
			application.setBranchId(rs.getString("branch_id"));
			application.setCgpa(rs.getDouble("cgpa"));
			application.setStatus(rs.getString("status"));
			application.setCollegeEmail(rs.getString("college_email"));

			return application;
		}
	}

	// Save Company Team
	@Override
	public int save(CompanyTeam companyTeam) {
		String sql = "INSERT INTO company_teams(ctm_cmp_id, ctm_name, ctm_contact) VALUES(?, ?, ?)";
		return jdbcTemplate.update(sql, companyTeam.getCtmCmpId(), companyTeam.getCtmName(),
				companyTeam.getCtmContact());
	}

	// Find Candidates by Round
	@Override
	public List<Application> findCandidatesByRound(int pldId, String round) {
		String sql = "SELECT * FROM applications WHERE app_pld_id = ? AND round_name = ?";
		return jdbcTemplate.query(sql, new ApplicationRowMapper(), pldId, round);
	}

	private static class ApplicationRowMapper implements RowMapper<Application> {
		@Override
		public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
			Application application = new Application();
			application.setAppId(rs.getInt("app_id"));
			application.setAppUsrId(rs.getString("app_usr_id"));
			application.setAppPldId(rs.getInt("app_pld_id"));
			application.setAppDate(rs.getDate("app_date"));
			application.setAppStatus(rs.getString("app_status"));
			return application;
		}
	}

	// Find Rounds by Placement Drive ID
	@Override
	public List<String> findRoundsByPldId(int pldId) {
		String sql = "SELECT DISTINCT phase_name FROM round_wise_shortlisted WHERE pld_id = ?";
		return jdbcTemplate.queryForList(sql, String.class, pldId);
	}

	@Override
	public void save(HR hr) {
		String sql = "INSERT INTO HR VALUES(?,?,?,?,?)";
		jdbcTemplate.update(sql, hr.getHrId(), hr.getHrName(), hr.getCmpId(), hr.getClgId(), hr.getHrEmail());
	}

	@Override
	public List<String> findAllColleges() {
		return jdbcTemplate.queryForList("select clg_name  from colleges", String.class);
	}

	@Override
	public List<Company> findAllCompanies() {
		// TODO Auto-generated method stub
		return jdbcTemplate.query("select * from companies", new CompanyRowMapper());
	}

	@Override
	public int findCollegeId(String college_name) {
		return jdbcTemplate.queryForObject("select clg_id from colleges where clg_name=?", Integer.class, college_name);
	}

	// Get Hiring Phases by Placement Drive ID
	@Override
	public List<HiringPhase> getHiringPhases(int pldId) {
		String sql = "SELECT * FROM hiring_phases WHERE hph_pld_id = ? ORDER BY hph_sequence";
		return jdbcTemplate.query(sql, hiringPhaseMapper, pldId);
	}

	private final RowMapper<HiringPhase> hiringPhaseMapper = new RowMapper<>() {
		@Override
		public HiringPhase mapRow(ResultSet rs, int rowNum) throws SQLException {
			HiringPhase hs = new HiringPhase();

			hs.setHphId(rs.getInt("hph_id"));
			hs.setHphPldId(rs.getInt("hph_pld_id"));
			hs.setHphName(rs.getString("hph_name"));
			hs.setHphSequence(rs.getInt("hph_sequence"));
			hs.setTotalScore(rs.getDouble("total_score"));
			hs.setThresholdScore(rs.getDouble("threshold_score"));
			return hs;
		}
	};

	// Get Round Wise Shortlisted Students by Placement Drive ID
	@Override
	public List<RoundWiseShortlisted> getRoundWiseShortlisted(int pldId) {
		String sql = "SELECT rws.phase_id, rws.pld_id, rws.student_id, rws.score, "
				+ "hp.hph_name AS phase_name, s.full_name AS student_name " + "FROM round_wise_shortlisted rws "
				+ "JOIN hiring_phases hp ON hp.hph_id = rws.phase_id "
				+ "JOIN students s ON s.roll_no = rws.student_id " + "WHERE rws.pld_id = ? "
				+ "ORDER BY hp.hph_sequence, rws.student_id";
		return jdbcTemplate.query(sql, rowMapper, pldId);
	}

	// Get Final Shortlisted Students
	@Override
	public List<Shortlisted> getFinalShortlisted(int pldId) {
		String sql = "SELECT s.roll_no AS student_id, s.full_name AS student_name, SUM(rws.score) AS score, a.app_status AS status,a.remarks "
				+ "FROM shortlisted sl "
				+ "JOIN applications a ON a.app_pld_id = sl.pld_id AND a.app_usr_id = sl.student_id "
				+ "JOIN students s ON s.roll_no = a.app_usr_id "
				+ "JOIN round_wise_shortlisted rws ON rws.student_id = sl.student_id AND rws.pld_id = sl.pld_id "
				+ "JOIN hiring_phases hp ON hp.hph_id = rws.phase_id " + "WHERE sl.pld_id = ? "
				+ "GROUP BY s.roll_no, s.full_name, a.app_status,a.remarks";
		return jdbcTemplate.query(sql, shortlistedMapper, pldId);
	}

	private final RowMapper<Shortlisted> shortlistedMapper = new RowMapper<>() {
		@Override
		public Shortlisted mapRow(ResultSet rs, int rowNum) throws SQLException {
			Shortlisted list = new Shortlisted();
			list.setStudentName(rs.getString("student_name"));
			list.setStatus(rs.getString("status"));
			list.setScore(rs.getDouble("score"));
			list.setStudentId(rs.getString("student_id"));
			list.setRemarks(rs.getString("remarks"));

			return list;
		}
	};

	// Get Round Wise Shortlisted Students by Round
	@Override
	public List<RoundWiseShortlisted> getRoundWiseShortlistedByRound(int pldId, String round) {
		// Fix: We join hiring_phases to filter by phase_name
		String sql = "SELECT rws.phase_id, rws.pld_id, rws.student_id, rws.score, " + "s.full_name AS student_name, "
				+ "hp.hph_name AS phase_name " + "FROM round_wise_shortlisted rws "
				+ "JOIN students s ON rws.student_id = s.roll_no "
				+ "JOIN hiring_phases hp ON rws.phase_id = hp.hph_id " + "WHERE rws.pld_id = ? AND hp.hph_name = ? "
				+ "ORDER BY hp.hph_sequence";
		return jdbcTemplate.query(sql, rowMapper, pldId, round);
	}

	private final RowMapper<RoundWiseShortlisted> rowMapper = new RowMapper<>() {
		@Override
		public RoundWiseShortlisted mapRow(ResultSet rs, int rowNum) throws SQLException {
			RoundWiseShortlisted list = new RoundWiseShortlisted();
			list.setStudent_name(rs.getString("student_name"));
			list.setPlacementId(rs.getInt("pld_id"));
			list.setStudentId(rs.getString("student_id"));
			list.setPhaseId(rs.getInt("phase_id"));
			list.setScore(rs.getInt("score"));
			list.setPhase_name(rs.getString("phase_name"));
			return list;
		}
	};

	@Override
	public String findDriveName(int pldId) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject("select pld_name from placement_drives where pld_id=?", String.class, pldId);
	}

	private final RowMapper<Resume> resumeMapper = (rs, rowNum) -> {
		Resume resume = new Resume();
		resume.setResId(rs.getInt("res_id"));
		resume.setResUsrId(rs.getString("res_usr_id"));
		resume.setResFileName(rs.getString("res_file_name"));
		resume.setResType(rs.getString("res_file_type"));
		resume.setResumeData(rs.getBytes("res_data"));
		resume.setResUploadDate(rs.getObject("res_upload_date", LocalDate.class));
		return resume;
	};

	@Override
	public Resume getResumesByUserId(String usrId) {
		try {
			return jdbcTemplate.queryForObject("SELECT * FROM resumes WHERE res_usr_id = ?", resumeMapper,
					new Object[] { usrId });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public PlacementDrive findPldId(String userId) {
		String sql = "select * from placement_drives pd join hr h on h.cmp_id = pd.pld_cmp_id where h.hr_id = ?";
		return jdbcTemplate.queryForObject(sql, new PlacementDriveRowMapper(), userId);
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
			drive.setPldRole(rs.getString("pld_role_name"));
			drive.setPldSalary(rs.getDouble("pld_package"));
			return drive;
		}
	}

	@Override
	public List<PlacementDrive> findAllPlacementsByCmpId(int cmpId) {
		// TODO Auto-generated method stub
		return jdbcTemplate.query("select * from placement_drives where pld_cmp_id=?", new PlacementDriveRowMapper(),
				cmpId);
	}

	@Override
	public int getPlacementId() {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject("select distinct pld_id from temp_Shortlisted", Integer.class);
	}

}
