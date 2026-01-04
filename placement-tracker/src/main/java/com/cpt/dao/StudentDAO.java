package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.Application;
import com.cpt.model.AttendedDrive;
import com.cpt.model.DriveInfo;
import com.cpt.model.EligibleDrive;
import com.cpt.model.HiringPhase;
import com.cpt.model.Notification;
import com.cpt.model.PlacementDrive;
import com.cpt.model.Profile;
import com.cpt.model.Resume;
import com.cpt.model.SelectedDrive;
import com.cpt.model.Student;
import com.cpt.model.StudentStatsDTO;

@Repository
public class StudentDAO implements StudentDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Student findById(String usrId) {
		String sql = "SELECT * FROM students WHERE roll_no = ?";
		return jdbcTemplate.queryForObject(sql, new StudentRowMapper(), new Object[] { usrId });
	}

	private static class StudentRowMapper implements RowMapper<Student> {
		@Override
		public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
			Student student = new Student();
			student.setRollNo(rs.getString("roll_no"));
			student.setClgId(rs.getLong("college_id"));
			student.setBranchId(rs.getLong("branch_id"));
			student.setFullName(rs.getString("full_name"));
			student.setGender(rs.getString("gender"));
			student.setCgpa(rs.getDouble("cgpa"));
			student.setBacklogs(rs.getInt("backlogs"));
			student.setCollegeEmail(rs.getString("college_email"));
			return student;
		}
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

	// private final RowMapper<AttendedDrive> attendedDriveMapper = (rs, rowNum) ->
	// {
	// AttendedDrive drive = new AttendedDrive();
	// drive.setPldId(rs.getInt("pld_id"));
	// drive.setUsrId(rs.getString("usr_id"));
	// drive.setStatus(rs.getString("status"));
	// drive.setFizzledRound(rs.getString("fizzled_round"));
	// drive.setPldName(rs.getString("pld_name"));
	// drive.setCmpName(rs.getString("cmp_name"));
	// drive.setHphId(rs.getObject("hph_id", Integer.class));
	// drive.setHphName(rs.getString("hph_name"));
	// drive.setHphSequence(rs.getObject("hph_sequence", Integer.class));
	// return drive;
	// };

	private final RowMapper<Notification> notificationMapper = (rs, rowNum) -> {
		Notification notification = new Notification();
		notification.setNtfId(rs.getLong("ntf_id"));
		notification.setNtfUsrId(rs.getString("ntf_usr_id"));
		notification.setNtfMessage(rs.getString("ntf_message"));
		notification.setNtfDate(rs.getObject("ntf_date", LocalDate.class));
		notification.setNtfRead(rs.getBoolean("ntf_read"));
		return notification;
	};

	private final RowMapper<Application> applicationMapper = (rs, rowNum) -> {
		Application application = new Application();
		application.setAppId(rs.getInt("app_id"));
		application.setAppUsrId(rs.getString("app_usr_id"));
		application.setAppPldId(rs.getInt("app_pld_id"));
		application.setAppCmpId(rs.getInt("app_cmp_id"));
		application.setAppDate(rs.getDate("app_date"));
		application.setAppStatus(rs.getString("app_status"));
		return application;
	};

	// private final RowMapper<AttendedDrive> attendedDriveMapper = (rs, rowNum) -> {
	// AttendedDrive attendedDrive = new AttendedDrive();
	// attendedDrive.setPldId(rs.getInt("pld_id"));
	// attendedDrive.setUsrId(rs.getString("usr_id"));
	// attendedDrive.setStatus(rs.getString("status"));
	// attendedDrive.setFizzledRound(rs.getInt("fizzled_round"));
	// attendedDrive.setPldName(rs.getString("pld_name"));
	// attendedDrive.setCmpName(rs.getString("cmp_name"));
	//
	// // Create and add hiring phases to the attended drive
	// List<HiringPhase> phases = new ArrayList<>();
	// do {
	// int hphId = rs.getInt("hph_id");
	// String hphName = rs.getString("hph_name");
	// int hphSequence = rs.getInt("hph_sequence");
	//
	// HiringPhase phase = new HiringPhase();
	// phase.setHphId(hphId);
	// phase.setHphName(hphName);
	// phase.setHphSequence(hphSequence);
	//
	// phases.add(phase);
	// } while (rs.next() && rs.getInt("pld_id") == attendedDrive.getPldId()); // Handle multiple phases for the same
	// // pldId
	//
	// attendedDrive.setPhases(phases);
	//
	// return attendedDrive;
	// };

	private final RowMapper<SelectedDrive> selectedDriveMapper = (rs, rowNum) -> {
		SelectedDrive selectedDrive = new SelectedDrive();
		selectedDrive.setPldId(rs.getInt("pld_id"));
		selectedDrive.setUsrId(rs.getString("usr_id"));
		selectedDrive.setPldName(rs.getString("pld_name"));
		selectedDrive.setCmpName(rs.getString("cmp_name"));

		// Create and add hiring phases to the attended drive
		List<HiringPhase> phases = new ArrayList<>();
		do {
			int hphId = rs.getInt("hph_id");
			String hphName = rs.getString("hph_name");
			int hphSequence = rs.getInt("hph_sequence");

			HiringPhase phase = new HiringPhase();
			phase.setHphId(hphId);
			phase.setHphName(hphName);
			phase.setHphSequence(hphSequence);

			phases.add(phase);
		} while (rs.next() && rs.getInt("pld_id") == selectedDrive.getPldId()); // Handle multiple phases for the same
																				// pldId

		selectedDrive.setPhases(phases);

		return selectedDrive;
	};

	public final RowMapper<EligibleDrive> eligibleDriveMapper() {
		return (rs, rowNum) -> {
			EligibleDrive drive = new EligibleDrive();
			drive.setUserId(rs.getString("student_id"));
			drive.setPldId(rs.getInt("pld_id"));
			drive.setDriveName(rs.getString("pld_name"));
			drive.setStartDate(rs.getDate("pld_start_date").toLocalDate());
			drive.setEndDate(rs.getDate("pld_end_date") != null ? rs.getDate("pld_end_date").toLocalDate() : null);
			drive.setCompanyName(rs.getString("cmp_name"));
			drive.setCompanyDescription(rs.getString("cmp_desc"));
			return drive;
		};
	}

	// public final RowMapper<Profile> profileMapper() {
	// return (rs, rowNum) -> {
	// Profile pr = new Profile();
	// pr.setUserId(rs.getString("usr_id"));
	// pr.setPldId(rs.getInt("pld_id"));
	// pr.setDriveName(rs.getString("pld_name"));
	// pr.setStartDate(rs.getDate("pld_start_date").toLocalDate());
	// pr.setEndDate(rs.getDate("pld_end_date") != null ? rs.getDate("pld_end_date").toLocalDate() : null);
	// pr.setCompanyName(rs.getString("cmp_name"));
	// pr.setCompanyDescription(rs.getString("cmp_desc"));
	// return pr;
	// };
	// }

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
	public void addResume(Resume resume) {
		String qry = "Select count(*) from resumes where res_usr_id = ?";
		Integer cnt = jdbcTemplate.queryForObject(qry, new Object[] { resume.getResUsrId() }, Integer.class);
		if (cnt == null || cnt == 0) {
			jdbcTemplate.update(
					"INSERT INTO resumes (res_usr_id, res_file_name, res_file_type, res_data, res_upload_date) VALUES (?, ?, ?, ?, CURRENT_DATE)",
					resume.getResUsrId(), resume.getResFileName(), resume.getResType(), resume.getResumeData());
		} else {
			jdbcTemplate.update(
					"update resumes set res_file_name = ?, res_file_type = ?, res_data = ?, res_upload_date = CURRENT_DATE where res_usr_id = ?",
					resume.getResFileName(), resume.getResType(), resume.getResumeData(), resume.getResUsrId());

		}

	}

	@Override
	public void deleteResume(String usrId) {
		jdbcTemplate.update("DELETE FROM resumes WHERE res_usr_id = ?", usrId);
	}

	// public List<PlacementDrive> getUpcomingDrives() {
	// return jdbcTemplate.queryForList(
	// "SELECT pd.*, c.cmp_name " + "FROM placement_drives pd "
	// + "JOIN companies c ON pd.pld_cmp_id = c.cmp_id " + "WHERE pd.pld_start_date >= CURRENT_DATE",
	// driveMapper);
	// }

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

	private final RowMapper<Profile> profileMapper = (rs, rowNum) -> {
		Profile profile = new Profile();
		profile.setPrfUsrId(rs.getString("roll_no"));
		profile.setPrfImage(rs.getBytes("prf_image"));
		profile.setPrfContact(rs.getString("prf_contact"));
		profile.setGithubUrl(rs.getString("github_url"));
		profile.setName(rs.getString("full_name"));
		profile.setGender(rs.getString("gender"));
		profile.setEmail(rs.getString("college_email"));
		profile.setBacklogs(rs.getInt("backlogs"));
		profile.setPrfGpa(rs.getDouble("cgpa"));
		profile.setCollegeName(rs.getString("clg_name"));
		profile.setBranchName(rs.getString("brn_name"));
		profile.setSkills(rs.getString("skills"));

		return profile;
	};

	@Override
	public List<EligibleDrive> getEligibleDrives(String usrId) {
		String sql = "SELECT es.student_id, pd.pld_id, pd.pld_name, pd.pld_start_date,pd.pld_end_date,c.cmp_name,c.cmp_desc "
				+ "FROM eligible_students es" + " JOIN placement_drives pd ON es.pld_id = pd.pld_id "
				+ "JOIN companies c ON pd.pld_cmp_id = c.cmp_id "
				+ "WHERE es.student_id = ? AND pd.pld_start_date >= CURRENT_DATE " + "ORDER BY pd.pld_start_date ";

		return jdbcTemplate.query(sql, eligibleDriveMapper(), new Object[] { usrId });
	}

	@Override
	public boolean hasApplied(String usrId, Integer pldId) {
		Integer count = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM applications WHERE app_usr_id = ? AND app_pld_id = ?", Integer.class, usrId,
				pldId);
		return count != null && count > 0;
	}

	@Override
	public void submitApplication(String usrId, Integer pldId, Integer cmpId) {
		jdbcTemplate.update("INSERT INTO applications (app_usr_id, app_pld_id, app_cmp_id, app_date, app_status) "
				+ "VALUES (?, ?, ?, CURRENT_DATE, 'APPL')", usrId, pldId, cmpId);

		jdbcTemplate.update(
				"INSERT INTO notifications (ntf_usr_id, ntf_message, ntf_date, ntf_read) "
						+ "VALUES (?, ?, CURRENT_DATE, FALSE)",
				usrId, "Application for drive ID " + pldId + " received.");
	}

	// @Override
	// public List<AttendedDrive> getAttendedDrives(String usrId) {
	//
	// String query = "SELECT " + "ad.pld_id, " + "ad.usr_id, " + "ad.status, "
	// + "case when ad.fizzled_round is null then (select count(*) from hiring_phases hp where hp.hph_pld_id =
	// ad.pld_id) else ad.fizzled_round end as fizzled_round, "
	// + "pd.pld_name, " + "c.cmp_name, " + "hph.hph_id, " + "hph.hph_name, " + "hph.hph_sequence "
	// + "FROM attended_drives ad " + "JOIN placement_drives pd ON ad.pld_id = pd.pld_id "
	// + "JOIN companies c ON pd.pld_cmp_id = c.cmp_id "
	// + "LEFT JOIN hiring_phases hph ON pd.pld_id = hph.hph_pld_id where ad.usr_id = ? "
	// + "ORDER BY ad.pld_id, hph.hph_sequence";
	//
	// return jdbcTemplate.query(query, attendedDriveMapper, new Object[] { usrId });
	// }

	@Override
	public List<SelectedDrive> getSelectedDrives(String usrId) {
		String query = "SELECT " + "ad.pld_id, " + "ad.usr_id, " + "ad.status, "
				+ "case when ad.fizzled_round is null then (select count(*) from hiring_phases hp where hp.hph_pld_id = ad.pld_id) else ad.fizzled_round end as fizzled_round, "
				+ "pd.pld_name, " + "c.cmp_name, " + "hph.hph_id, " + "hph.hph_name, " + "hph.hph_sequence "
				+ "FROM attended_drives ad " + "JOIN placement_drives pd ON ad.pld_id = pd.pld_id "
				+ "JOIN companies c ON pd.pld_cmp_id = c.cmp_id "
				+ "LEFT JOIN hiring_phases hph ON pd.pld_id = hph.hph_pld_id where ad.usr_id = ? and ad.status = 'SELECTED' "
				+ "ORDER BY ad.pld_id, hph.hph_sequence";

		return jdbcTemplate.query(query, selectedDriveMapper, new Object[] { usrId });
	}

	// private List<HiringPhase> getHiringPhases(Connection conn, String phaseSql, int pldId) throws SQLException {
	// List<HiringPhase> phases = new ArrayList<>();
	// try (PreparedStatement phaseStmt = conn.prepareStatement(phaseSql)) {
	// phaseStmt.setInt(1, pldId);
	// try (ResultSet rs = phaseStmt.executeQuery()) {
	// while (rs.next()) {
	// HiringPhase phase = new HiringPhase();
	// phase.setHphName(rs.getString("hph_name"));
	// phase.setHphSequence(rs.getInt("hph_sequence"));
	// phases.add(phase);
	// }
	// }
	// }
	// return phases;
	// }

	private final RowMapper<Student> studentMapper = (rs, rowNum) -> {
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
		student.setGithubUrl(rs.getString("github_url"));
		student.setSkills(rs.getString("skills"));
		student.setBranchName(rs.getString("brn_name"));
		student.setCollegeName(rs.getString("clg_name"));
		return student;
	};

	@Override
	public Student getStudentById(String RollNo) {
		try {
			return jdbcTemplate.queryForObject("SELECT * FROM students WHERE roll_no = ?", studentMapper,
					new Object[] { RollNo });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Notification> getNotifications(String usrId) {
		return jdbcTemplate.query("SELECT * FROM notifications WHERE ntf_usr_id = ? ORDER BY ntf_date DESC",
				notificationMapper, usrId);
	}

	@Override
	public void markNotificationRead(Integer ntfId) {
		jdbcTemplate.update("UPDATE notifications SET ntf_read = TRUE WHERE ntf_id = ?", ntfId);
	}

	@Override
	public List<Object[]> getApplicationCounts() {
		return jdbcTemplate.query(
				"SELECT c.cmp_name, pd.pld_name, COUNT(a.app_id) as app_count " + "FROM applications a "
						+ "JOIN placement_drives pd ON a.app_pld_id = pd.pld_id "
						+ "JOIN companies c ON a.app_cmp_id = c.cmp_id " + "GROUP BY c.cmp_name, pd.pld_name",
				(rs, rowNum) -> new Object[] { rs.getString("cmp_name"), rs.getString("pld_name"),
						rs.getInt("app_count") });
	}

	@Override
	public List<Application> getApplicationsByUserId(String usrId) {
		return jdbcTemplate.query("SELECT * FROM applications WHERE app_usr_id = ?", applicationMapper, usrId);
	}

	@Override
	public void saveStudents(List<Student> students) {
		String sql = "INSERT INTO students (roll_no, full_name, branch_id, college_id, gender, status, cgpa, backlogs, college_email) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		for (Student student : students) {
			try {
				jdbcTemplate.update(sql, student.getRollNo(), student.getFullName(), student.getBranchId(),
						student.getClgId(), student.getGender(), student.getStatus(), student.getCgpa(),
						student.getBacklogs(), student.getCollegeEmail());
			} catch (Exception e) {
				System.err.println("Error saving student: " + student.getRollNo() + ", Error: " + e.getMessage());
			}
		}
	}

	@Override
	public int getCmpId(int pldId) {
		return jdbcTemplate.queryForObject("select pld_cmp_id from placement_drives where pld_id = ?",
				new Object[] { pldId }, Integer.class);
	}

	@Override
	public boolean resumeExists(String usrId) {
		Integer ex = jdbcTemplate.queryForObject("select count(*) from resumes where res_usr_id = ?",
				new Object[] { usrId }, Integer.class);
		return ex != null && ex > 0;
	}

	// public Profile getStudentProfile(String usrId) {
	// return jdbcTemplate.queryForObject("select * from profiles where pr_usr_id = ?", usrId, )
	// return null;
	// }
	@Override
	public Profile getProfile(String usrId) {
		String sql = "SELECT pr.prf_contact, clg.clg_name, pr.prf_image, st.github_url, st.skills, st.full_name, st.gender, st.cgpa, st.backlogs, st.college_email, st.roll_no, brn.brn_name  FROM profiles pr join students st on st.roll_no = pr.prf_usr_id join branches brn on brn.brn_id = st.branch_id join colleges clg on st.college_id = clg.clg_id WHERE pr.prf_usr_id = ?";
		return jdbcTemplate.queryForObject(sql, profileMapper, new Object[] { usrId });
	}

	@Override
	public void updateStudentProfile(Profile profile) {
		// Update profiles table
		String sqlProfiles = "UPDATE profiles SET prf_contact = ?, prf_image = ? WHERE prf_usr_id = ?";
		jdbcTemplate.update(sqlProfiles, profile.getPrfContact(), profile.getPrfImage(), profile.getPrfUsrId());

		// Update students table
		String sqlStudents = "UPDATE students SET github_url = ?, skills = ?, college_email = ? WHERE roll_no = ?";
		jdbcTemplate.update(sqlStudents, profile.getGithubUrl(), profile.getSkills(), profile.getEmail(),
				profile.getPrfUsrId());
	}

	@Override
	public void updateProfile(Student student) {
		String sql = "UPDATE students SET full_name = ?, gender = ?, cgpa = ?, backlogs = ?, college_email = ? WHERE roll_no = ?";
		jdbcTemplate.update(sql, student.getFullName(), student.getGender(), student.getCgpa(), student.getBacklogs(),
				student.getCollegeEmail(), student.getRollNo());
	}

	@Override
	public long count() {
		String sql = "SELECT COUNT(*) FROM students";
		return jdbcTemplate.queryForObject(sql, Long.class);
	}

	private final RowMapper<AttendedDrive> attendedDriveMapper = new RowMapper<AttendedDrive>() {
		@Override
		public AttendedDrive mapRow(ResultSet rs, int rowNum) throws SQLException {
			// This RowMapper is used to process individual rows, but we'll handle grouping
			// in the query method
			AttendedDrive drive = new AttendedDrive();
			drive.setPldId(rs.getInt("pld_id"));
			drive.setUsrId(rs.getString("usr_id"));
			drive.setStatus(rs.getString("status"));
			drive.setFizzledRound(rs.getInt("fizzled_round"));
			drive.setPldName(rs.getString("pld_name"));
			drive.setCmpName(rs.getString("cmp_name"));

			List<HiringPhase> phases = new ArrayList<>();
			int hphId = rs.getInt("hph_id");
			if (hphId > 0) { // Only add valid phases
				HiringPhase phase = new HiringPhase();
				phase.setHphId(hphId);
				phase.setHphName(rs.getString("hph_name"));
				phase.setHphSequence(rs.getInt("hph_sequence"));
				phases.add(phase);
			}
			drive.setPhases(phases);

			return drive;
		}
	};

	@Override
	public List<AttendedDrive> getAttendedDrives(String usrId) {
		String query = "SELECT ad.pld_id, ad.usr_id, ad.status, " + "CASE WHEN ad.fizzled_round IS NULL THEN "
				+ "  (CASE WHEN ad.status = 'SELECTED' THEN "
				+ "    (SELECT COUNT(*) FROM hiring_phases hp WHERE hp.hph_pld_id = ad.pld_id) " + "  ELSE 1 END) "
				+ "ELSE ad.fizzled_round END AS fizzled_round, "
				+ "pd.pld_name, c.cmp_name, hph.hph_id, hph.hph_name, hph.hph_sequence " + "FROM attended_drives ad "
				+ "JOIN placement_drives pd ON ad.pld_id = pd.pld_id " + "JOIN companies c ON pd.pld_cmp_id = c.cmp_id "
				+ "INNER JOIN hiring_phases hph ON pd.pld_id = hph.hph_pld_id " + "WHERE ad.usr_id = ? "
				+ "ORDER BY ad.pld_id, hph.hph_sequence";

		List<AttendedDrive> drives = new ArrayList<>();
		List<AttendedDrive> rawDrives = jdbcTemplate.query(query, attendedDriveMapper, usrId);

		// Group phases by pld_id
		AttendedDrive currentDrive = null;
		for (AttendedDrive drive : rawDrives) {
			if (currentDrive == null || currentDrive.getPldId() != drive.getPldId()) {
				// New drive, add to list
				currentDrive = new AttendedDrive();
				currentDrive.setPldId(drive.getPldId());
				currentDrive.setUsrId(drive.getUsrId());
				currentDrive.setStatus(drive.getStatus());
				currentDrive.setFizzledRound(drive.getFizzledRound());
				currentDrive.setPldName(drive.getPldName());
				currentDrive.setCmpName(drive.getCmpName());
				currentDrive.setPhases(new ArrayList<>());
				drives.add(currentDrive);
			}
			// Add phases to the current drive
			currentDrive.getPhases().addAll(drive.getPhases());
		}

		return drives;
	}

	@Override
	public List<Student> getAllStudents() {
		return jdbcTemplate.query(
				"select st.roll_no, st.full_name, st.branch_id, st.college_id, br.brn_name, clg.clg_name, st.gender, st.status, st.cgpa, st.backlogs, st.college_email, st.skills, st.github_url from students st join branches br on br.brn_id = st.branch_id join colleges clg on clg.clg_id = st.college_id ",
				studentMapper);

	}

	private final RowMapper<DriveInfo> driveInfoMapper = new RowMapper<DriveInfo>() {
		@Override
		public DriveInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			DriveInfo drive = new DriveInfo();
			try {
				drive.setPldId(rs.getInt("pld_id"));
				drive.setPldName(rs.getString("pld_name"));
				drive.setCmpName(rs.getString("cmp_name"));
				drive.setStatus(rs.getString("status")); // Allow null for eligible drives
				drive.setPldDate(rs.getString("pld_date"));
				drive.setMinCgpa(rs.getDouble("pld_min_cgpa"));
				drive.setMaxBacklogs(rs.getInt("pld_max_backlogs"));
			} catch (SQLException e) {
				System.err.println("Error mapping drive row: " + e.getMessage());
				System.err.println("Available columns: ");
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					System.err.println(rs.getMetaData().getColumnName(i));
				}
				throw e;
			}
			return drive;
		}
	};

	public List<StudentStatsDTO> getAllStudentsWithStats() {
		// Fetch students
		String studentQuery = "SELECT "
				+ "st.roll_no, st.full_name, st.branch_id, st.college_id, br.brn_name, clg.clg_name, "
				+ "st.gender, st.status, st.cgpa, st.backlogs, st.college_email, "
				+ "COALESCE(st.skills, '') AS skills, COALESCE(st.github_url, '') AS github_url " + "FROM students st "
				+ "JOIN branches br ON br.brn_id = st.branch_id " + "JOIN colleges clg ON clg.clg_id = st.college_id "
				+ "WHERE st.status = 'Active'";
		List<Student> students;
		try {
			students = jdbcTemplate.query(studentQuery, studentMapper);
		} catch (Exception e) {
			System.err.println("Error executing student query: " + e.getMessage());
			throw e;
		}

		List<StudentStatsDTO> result = new ArrayList<>();
		for (Student student : students) {
			StudentStatsDTO dto = new StudentStatsDTO();
			dto.setStudent(student);

			try {
				// Attended Drives Count
				String attendedCountQuery = "SELECT COUNT(*) FROM attended_drives WHERE usr_id = ?";
				int attendedCount = jdbcTemplate.queryForObject(attendedCountQuery, Integer.class, student.getRollNo());
				dto.setAttendedDrivesCount(attendedCount);

				// Selected Count
				String selectedCountQuery = "SELECT COUNT(*) FROM attended_drives WHERE usr_id = ? AND status = 'SELECTED'";
				int selectedCount = jdbcTemplate.queryForObject(selectedCountQuery, Integer.class, student.getRollNo());
				dto.setSelectedCount(selectedCount);

				// Eligible Drives Count
				String eligibleCountQuery = "SELECT COUNT(DISTINCT pd.pld_id) " + "FROM placement_drives pd "
						+ "LEFT JOIN screening_criteria sc ON sc.scr_pld_id = pd.pld_id "
						+ "WHERE pd.pld_status IN ('NOT ASSIGNED', 'ASSIGNED') " + "AND (sc.scr_id IS NULL OR ("
						+ "(sc.scr_min_gpa IS NULL OR sc.scr_min_gpa <= ?) AND "
						+ "(sc.scr_min_backlogs IS NULL OR sc.scr_min_backlogs >= ?) AND "
						+ "(sc.scr_brn_id IS NULL OR sc.scr_brn_id = ?)))";
				int eligibleCount = jdbcTemplate.queryForObject(eligibleCountQuery, Integer.class, student.getCgpa(),
						student.getBacklogs(), student.getBranchId());
				dto.setEligibleDrivesCount(eligibleCount);

				// Attended Drives Details
				String attendedDrivesQuery = "SELECT pd.pld_id, pd.pld_name, c.cmp_name, ad.status, pd.pld_start_date AS pld_date, "
						+ "COALESCE(pd.pld_package / 100000.0, 0) AS pld_min_cgpa, 0 AS pld_max_backlogs "
						+ "FROM attended_drives ad " + "JOIN placement_drives pd ON ad.pld_id = pd.pld_id "
						+ "JOIN companies c ON pd.pld_cmp_id = c.cmp_id " + "WHERE ad.usr_id = ?";
				List<DriveInfo> attendedDrives = jdbcTemplate.query(attendedDrivesQuery, driveInfoMapper,
						student.getRollNo());
				dto.setAttendedDrives(attendedDrives);

				// Selected Drives Details
				String selectedDrivesQuery = "SELECT pd.pld_id, pd.pld_name, c.cmp_name, ad.status, pd.pld_start_date AS pld_date, "
						+ "COALESCE(pd.pld_package / 100000.0, 0) AS pld_min_cgpa, 0 AS pld_max_backlogs "
						+ "FROM attended_drives ad " + "JOIN placement_drives pd ON ad.pld_id = pd.pld_id "
						+ "JOIN companies c ON pd.pld_cmp_id = c.cmp_id "
						+ "WHERE ad.usr_id = ? AND ad.status = 'SELECTED'";
				List<DriveInfo> selectedDrives = jdbcTemplate.query(selectedDrivesQuery, driveInfoMapper,
						student.getRollNo());
				dto.setSelectedDrives(selectedDrives);

				// Eligible Drives Details
				String eligibleDrivesQuery = "SELECT pd.pld_id, pd.pld_name, c.cmp_name, pd.pld_start_date AS pld_date, "
						+ "COALESCE(sc.scr_min_gpa, 0) AS pld_min_cgpa, COALESCE(sc.scr_min_backlogs, 0) AS pld_max_backlogs, "
						+ "'ELIGIBLE' AS status " + // Add status column
						"FROM placement_drives pd " + "JOIN companies c ON pd.pld_cmp_id = c.cmp_id "
						+ "LEFT JOIN screening_criteria sc ON sc.scr_pld_id = pd.pld_id "
						+ "WHERE pd.pld_status IN ('NOT ASSIGNED', 'ASSIGNED') " + "AND (sc.scr_id IS NULL OR ("
						+ "(sc.scr_min_gpa IS NULL OR sc.scr_min_gpa <= ?) AND "
						+ "(sc.scr_min_backlogs IS NULL OR sc.scr_min_backlogs >= ?) AND "
						+ "(sc.scr_brn_id IS NULL OR sc.scr_brn_id = ?)))";
				List<DriveInfo> eligibleDrives = jdbcTemplate.query(eligibleDrivesQuery, driveInfoMapper,
						student.getCgpa(), student.getBacklogs(), student.getBranchId());
				dto.setEligibleDrives(eligibleDrives);

				result.add(dto);
			} catch (Exception e) {
				System.err.println("Error processing stats for student " + student.getRollNo() + ": " + e.getMessage());
				// Continue with next student
			}
		}
		return result;
	}
}