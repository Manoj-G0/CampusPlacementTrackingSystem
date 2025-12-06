package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.Admin;
import com.cpt.model.College;
import com.cpt.model.Company;
import com.cpt.model.DriveInfo;
import com.cpt.model.HR;
import com.cpt.model.Student;
import com.cpt.model.StudentStatsDTO;

@Repository
public class AdminDAO implements AdminDAOInt {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private final RowMapper<Student> studentMapper = (rs, rowNum) -> {
		Student student = new Student();
		student.setRollNo(rs.getString(1));
		student.setFullName(rs.getString(2));
		student.setBranchId(rs.getLong(3));
		student.setClgId(rs.getLong(4));
		student.setGender(rs.getString(5));
		student.setStatus(rs.getString(6));
		student.setCgpa(rs.getDouble(7));
		student.setBacklogs(rs.getInt(8));
		student.setCollegeEmail(rs.getString(9));
		return student;
	};

	@Override
	public void save(HR hr) {
		String sql = "INSERT INTO HR VALUES(?,?,?,?,?)";
		jdbcTemplate.update(sql, hr.getHrId(), hr.getHrName(), hr.getCmpId(), hr.getClgId(), hr.getHrEmail());

	}

	@Override
	public boolean existsByRollNo(String rollNo) {
		String sql = "SELECT COUNT(*) FROM students WHERE roll_no = ?";
		Integer count = jdbcTemplate.queryForObject(sql, new Object[] { rollNo }, Integer.class);
		return count != null && count > 0;
	}

	@Override
	public boolean existsByCollegeEmail(String collegeEmail) {
		String sql = "SELECT COUNT(*) FROM students WHERE college_email = ?";
		Integer count = jdbcTemplate.queryForObject(sql, new Object[] { collegeEmail }, Integer.class);
		return count != null && count > 0;
	}

	@Override
	public void save(Student student) {
		System.out.println(student);
		String sql = "INSERT INTO students (roll_no, full_name, branch_id, college_id, gender, status, cgpa, backlogs, college_email) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, student.getRollNo(), student.getFullName(), student.getBranchId(), student.getClgId(),
				student.getGender(), student.getStatus(), student.getCgpa(), student.getBacklogs(),
				student.getCollegeEmail());
	}

	@Override
	public void addAdmin(Admin admin) {
		String sql = "INSERT INTO admins (po_id, name, designation, email, clg_id) VALUES (?, ?, ?, ?,?)";
		jdbcTemplate.update(sql, admin.getAdminId(), admin.getAdminName(), admin.getDesignation(), admin.getEmail(),
				admin.getCollegeId());
	}

	@Override
	public int updateCompany(Company company) {
		// TODO Auto-generated method stub
		return jdbcTemplate.update("update companies set cmp_name=?,cmp_cct_id=?,cmp_desc=? where cmp_id=?",
				company.getCmpName(), company.getCmpCctId(), company.getCmpDesc(), company.getCmpId());
	}

	@Override
	public int deleteCompany(int cmpId) {
		return jdbcTemplate.update("delete from companies where cmp_id=?", cmpId);
	}

	@Override
	public List<College> getEveryCollege() {
		String sql = "SELECT clg_id, clg_name FROM colleges";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new College(rs.getInt("clg_id"), rs.getString("clg_name")));
	}

	@Override
	public int getPhaseId(int pldId, String pname) {
		try {
			return jdbcTemplate.queryForObject(
					"select hph_sequence from hiring_phases where hph_name = ? and hph_pld_id = ?",
					new Object[] { pname, pldId }, Integer.class);
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public String getLastPhase(int pldId) {
		return jdbcTemplate.queryForObject(
				"select hph_name from hiring_phases where hph_pld_id = ? order by hph_sequence desc limit 1",
				new Object[] { pldId }, String.class);
	}

	@Override
	public void addtoAttended(String stdId, int pldId, int phId) {
		// TODO Auto-generated method stub
		int rounds = jdbcTemplate.queryForObject("select count(*) from hiring_phases where hph_pld_id = ?",
				new Object[] { pldId }, Integer.class);
		if (phId != rounds) {
			jdbcTemplate.update(
					"insert into attended_drives(pld_id, usr_id, status, fizzled_round) values(?,?,'NOT SELECTED', ?)",
					pldId, stdId, phId);
		} else {
			jdbcTemplate.update("insert into attended_drives(pld_id, usr_id, status) values(?,?,'SELECTED')", pldId,
					stdId);
		}
	}

	private final RowMapper<DriveInfo> driveInfoMapper = new RowMapper<DriveInfo>() {
		@Override
		public DriveInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			DriveInfo drive = new DriveInfo();
			drive.setPldId(rs.getInt("pld_id"));
			drive.setPldName(rs.getString("pld_name"));
			drive.setCmpName(rs.getString("cmp_name"));
			drive.setStatus(rs.getString("status"));
			drive.setPldDate(rs.getString("pld_date"));
			drive.setMinCgpa(rs.getDouble("pld_min_cgpa"));
			drive.setMaxBacklogs(rs.getInt("pld_max_backlogs"));
			return drive;
		}
	};

	public List<StudentStatsDTO> getAllStudentsWithStats() {
		// Fetch students
		String studentQuery = "SELECT st.roll_no, st.full_name, st.branch_id, st.college_id, br.brn_name, clg.clg_name, "
				+ "st.gender, st.status, st.cgpa, st.backlogs, st.college_email, st.skills, st.github_url "
				+ "FROM students st " + "JOIN branches br ON br.brn_id = st.branch_id "
				+ "JOIN colleges clg ON clg.clg_id = st.college_id";
		List<Student> students = jdbcTemplate.query(studentQuery, studentMapper);

		List<StudentStatsDTO> result = new ArrayList<>();
		for (Student student : students) {
			StudentStatsDTO dto = new StudentStatsDTO();
			dto.setStudent(student);

			// Attended Drives Count
			String attendedCountQuery = "SELECT COUNT(*) FROM attended_drives WHERE usr_id = ?";
			int attendedCount = jdbcTemplate.queryForObject(attendedCountQuery, Integer.class, student.getRollNo());
			dto.setAttendedDrivesCount(attendedCount);

			// Selected Count
			String selectedCountQuery = "SELECT COUNT(*) FROM attended_drives WHERE usr_id = ? AND status = 'SELECTED'";
			int selectedCount = jdbcTemplate.queryForObject(selectedCountQuery, Integer.class, student.getRollNo());
			dto.setSelectedCount(selectedCount);

			// Eligible Drives Count
			String eligibleCountQuery = "SELECT COUNT(*) FROM placement_drives pd " + "WHERE pd.pld_status = 'OPEN' "
					+ "AND (pd.pld_min_cgpa IS NULL OR pd.pld_min_cgpa <= ?) "
					+ "AND (pd.pld_max_backlogs IS NULL OR pd.pld_max_backlogs >= ?)";
			int eligibleCount = jdbcTemplate.queryForObject(eligibleCountQuery, Integer.class, student.getCgpa(),
					student.getBacklogs());
			dto.setEligibleDrivesCount(eligibleCount);

			// Attended Drives Details
			String attendedDrivesQuery = "SELECT pd.pld_id, pd.pld_name, c.cmp_name, ad.status, pd.pld_date, "
					+ "pd.pld_min_cgpa, pd.pld_max_backlogs " + "FROM attended_drives ad "
					+ "JOIN placement_drives pd ON ad.pld_id = pd.pld_id "
					+ "JOIN companies c ON pd.pld_cmp_id = c.cmp_id " + "WHERE ad.usr_id = ?";
			List<DriveInfo> attendedDrives = jdbcTemplate.query(attendedDrivesQuery, driveInfoMapper,
					student.getRollNo());
			dto.setAttendedDrives(attendedDrives);

			// Selected Drives Details
			String selectedDrivesQuery = "SELECT pd.pld_id, pd.pld_name, c.cmp_name, ad.status, pd.pld_date, "
					+ "pd.pld_min_cgpa, pd.pld_max_backlogs " + "FROM attended_drives ad "
					+ "JOIN placement_drives pd ON ad.pld_id = pd.pld_id "
					+ "JOIN companies c ON pd.pld_cmp_id = c.cmp_id "
					+ "WHERE ad.usr_id = ? AND ad.status = 'SELECTED'";
			List<DriveInfo> selectedDrives = jdbcTemplate.query(selectedDrivesQuery, driveInfoMapper,
					student.getRollNo());
			dto.setSelectedDrives(selectedDrives);

			// Eligible Drives Details
			String eligibleDrivesQuery = "SELECT pd.pld_id, pd.pld_name, c.cmp_name, pd.pld_date, "
					+ "pd.pld_min_cgpa, pd.pld_max_backlogs " + "FROM placement_drives pd "
					+ "JOIN companies c ON pd.pld_cmp_id = c.cmp_id " + "WHERE pd.pld_status = 'OPEN' "
					+ "AND (pd.pld_min_cgpa IS NULL OR pd.pld_min_cgpa <= ?) "
					+ "AND (pd.pld_max_backlogs IS NULL OR pd.pld_max_backlogs >= ?)";
			List<DriveInfo> eligibleDrives = jdbcTemplate.query(eligibleDrivesQuery, driveInfoMapper, student.getCgpa(),
					student.getBacklogs());
			dto.setEligibleDrives(eligibleDrives);

			result.add(dto);
		}
		return result;
	}

	@Override
	public Map<String, Object> getStudentRoundWiseStatus(int pldId) {
		return null;
		// TODO Auto-generated method stub
		
	}

}
