package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.Company;
import com.cpt.model.CompanyCategories;
import com.cpt.model.CompanyStatsDTO;
import com.cpt.model.Criterion;
import com.cpt.model.DriveInfo;
import com.cpt.model.DriveStatsDTO;
import com.cpt.model.Parameter;
import com.cpt.model.RoundShortlist;
import com.cpt.model.StudentShortlist;

@Repository
public class CompanyDAO implements CompanyDAOInt {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Override
	public void save(Company company) {
		String sql = "INSERT INTO companies (cmp_cct_id, cmp_name, cmp_desc) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, company.getCmpCctId(), company.getCmpName(), company.getCmpDesc());
	}

	@Override
	public List<Company> findAll() {
		String sql = "SELECT * FROM companies";
		return jdbcTemplate.query(sql, new CompanyRowMapper());
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

	@Override
	public Company getCompanyById(int company_id) {
		String query = "SELECT * FROM companies WHERE cmp_id = ?";
		return jdbcTemplate.queryForObject(query, new Object[] { company_id }, new CompanyRowMapper());
	}

	@Override
	public List<CompanyCategories> findAllCategories() {
		// TODO Auto-generated method stub
		return jdbcTemplate.query("select * from company_categories", new CompanyCategoryMapper());
	}

	public class CompanyCategoryMapper implements RowMapper<CompanyCategories> {

		@Override
		public CompanyCategories mapRow(ResultSet resultSet, int i) throws SQLException {

			CompanyCategories comc = new CompanyCategories();

			comc.setId(resultSet.getInt("cct_id"));

			comc.setName(resultSet.getString("cct_name"));

			comc.setDesc(resultSet.getString("cct_desc"));

			return comc;

		}
	}

	@Override
	public void update(Company company, int cmpid) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("update companies set cmp_cct_id=?,cmp_name=?,cmp_desc=? where cmp_id=?",
				company.getCmpCctId(), company.getCmpName(), company.getCmpDesc(), cmpid);
	}

	// DTO Classes (simplified)

	private final RowMapper<DriveInfo> driveInfoMapper = (rs, rowNum) -> {
		DriveInfo drive = new DriveInfo();
		drive.setPldId(rs.getInt("pld_id"));
		drive.setPldName(rs.getString("pld_name"));
		drive.setCollegeName(rs.getString("clg_name"));
		drive.setStartDate(rs.getString("pld_start_date"));
		drive.setStatus(rs.getString("pld_status"));
		drive.setPackageAmount(rs.getDouble("pld_package"));
		drive.setRoleName(rs.getString("pld_role_name"));
		return drive;
	};

	private final RowMapper<Parameter> parameterMapper = (rs, rowNum) -> {
		Parameter param = new Parameter();
		param.setParamId(rs.getInt("param_id"));
		param.setParameterName(rs.getString("param_name"));
		param.setThreshold(rs.getInt("threshold_score"));
		param.setTotalScore(rs.getInt("total_score"));
		return param;
	};

	private final RowMapper<Criterion> criterionMapper = (rs, rowNum) -> {
		Criterion criterion = new Criterion();
		criterion.setScrId(rs.getInt("scr_id"));
		criterion.setMinGpa(rs.getDouble("min_gpa"));
		criterion.setMinBacklogs(rs.getInt("min_backlogs"));
		criterion.setBranchName(rs.getString("brn_name"));
		return criterion;
	};

	private final RowMapper<RoundShortlist> roundShortlistMapper = (rs, rowNum) -> {
		RoundShortlist round = new RoundShortlist();
		round.setHphId(rs.getInt("hph_id"));
		round.setHphName(rs.getString("hph_name"));
		round.setSequence(rs.getInt("hph_sequence"));
		round.setShortlistedCount(rs.getInt("shortlisted_count"));
		return round;
	};

	private final RowMapper<Company> companyMapper = (rs, rowNum) -> {
		try {
			Company company = new Company();
			company.setCmpId(rs.getInt("cmp_id"));
			company.setCmpName(rs.getString("cmp_name"));
			company.setCategoryName(rs.getString("category_name"));
			company.setCmpDesc(rs.getString("cmp_desc"));
			return company;
		} catch (SQLException e) {
			throw e;
		}
	};

	private final RowMapper<StudentShortlist> studentShortlistMapper = (rs, rowNum) -> {
		try {
			StudentShortlist student = new StudentShortlist();
			student.setRollNo(rs.getString("roll_no"));
			student.setFullName(rs.getString("full_name"));
			student.setEmail(rs.getString("college_email") != null ? rs.getString("college_email") : "N/A");
			return student;
		} catch (SQLException e) {
			throw e;
		}
	};

	public List<CompanyStatsDTO> getAllCompaniesWithStats() {
		String companyQuery = "SELECT c.cmp_id, c.cmp_name, COALESCE(cc.cct_name, 'None') AS category_name, "
				+ "COALESCE(c.cmp_desc, '') AS cmp_desc " + "FROM companies c "
				+ "LEFT JOIN company_categories cc ON c.cmp_cct_id = cc.cct_id";
		List<Company> companies = jdbcTemplate.query(companyQuery, companyMapper);

		List<CompanyStatsDTO> result = new ArrayList<>();
		for (Company company : companies) {
			CompanyStatsDTO dto = new CompanyStatsDTO();
			dto.setCompany(company);
			List<DriveStatsDTO> drives = new ArrayList<>();

			// Fetch placement drives
			String driveQuery = "SELECT pd.pld_id, pd.pld_name, clg.clg_name, pd.pld_start_date, pd.pld_status, "
					+ "pd.pld_package, pd.pld_role_name " + "FROM placement_drives pd "
					+ "JOIN colleges clg ON pd.pld_clg_id = clg.clg_id " + "WHERE pd.pld_cmp_id = ?";
			List<DriveInfo> driveInfos = jdbcTemplate.query(driveQuery, driveInfoMapper, company.getCmpId());

			for (DriveInfo drive : driveInfos) {
				DriveStatsDTO driveStats = new DriveStatsDTO();
				driveStats.setDrive(drive);

				// Number of rounds
				String roundCountQuery = "SELECT COUNT(*) FROM hiring_phases hph WHERE hph.hph_pld_id = ?";
				int roundCount = jdbcTemplate.queryForObject(roundCountQuery, Integer.class, drive.getPldId());
				driveStats.setRoundCount(roundCount);

				// Parameters
				String paramQuery = "SELECT hp.param_id, hp.param_name, hp.threshold_score, hp.total_score "
						+ "FROM hiring_phase_parameters hp " + "JOIN hiring_phases hph ON hp.hph_id = hph.hph_id "
						+ "WHERE hph.hph_pld_id = ?";
				List<Parameter> parameters = jdbcTemplate.query(paramQuery, parameterMapper, drive.getPldId());
				driveStats.setParameters(parameters);

				// Criteria
				String criteriaQuery = "SELECT sc.scr_id, COALESCE(sc.scr_min_gpa, 0) AS min_gpa, "
						+ "COALESCE(sc.scr_min_backlogs, 0) AS min_backlogs, b.brn_name "
						+ "FROM screening_criteria sc " + "JOIN branches b ON sc.scr_brn_id = b.brn_id "
						+ "WHERE sc.scr_pld_id = ?";
				List<Criterion> criteria = jdbcTemplate.query(criteriaQuery, criterionMapper, drive.getPldId());
				driveStats.setCriteria(criteria);

				// Shortlisted per round
				String shortlistQuery = "SELECT hph.hph_id, hph.hph_name, hph.hph_sequence, "
						+ "COUNT(rws.student_id) AS shortlisted_count " + "FROM hiring_phases hph "
						+ "LEFT JOIN round_wise_shortlisted rws ON hph.hph_id = rws.phase_id AND hph.hph_pld_id = rws.pld_id "
						+ "WHERE hph.hph_pld_id = ? " + "GROUP BY hph.hph_id, hph.hph_name, hph.hph_sequence "
						+ "ORDER BY hph.hph_sequence";
				List<RoundShortlist> shortlisted = jdbcTemplate.query(shortlistQuery, roundShortlistMapper,
						drive.getPldId());
				driveStats.setShortlistedPerRound(shortlisted);

				// Finalized count
				String finalizedQuery = "SELECT COUNT(*) FROM attended_drives ad WHERE ad.pld_id = ? AND ad.status = 'SELECTED'";
				int finalizedCount = jdbcTemplate.queryForObject(finalizedQuery, Integer.class, drive.getPldId());
				driveStats.setFinalizedCount(finalizedCount);

				drives.add(driveStats);
			}
			dto.setDrives(drives);
			result.add(dto);
		}
		return result;
	}

}