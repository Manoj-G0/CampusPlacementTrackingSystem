
package com.cpt.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.Application;
import com.cpt.model.AttendedDrive;
import com.cpt.model.Notification;
import com.cpt.model.PlacementDrive;
import com.cpt.model.Resume;
import com.cpt.model.Student;

@Repository
public class StudentDAO {

	private final JdbcTemplate jdbcTemplate;

	public StudentDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private final RowMapper<Student> studentMapper = (rs, rowNum) -> {
		Student student = new Student();
		student.setRollNo(rs.getString("RollNo"));
		student.setFullName(rs.getString("FullName"));
		student.setBranchId(rs.getInt("branch_id"));
		student.setCollegeId(rs.getInt("college_id"));
		student.setGender(rs.getString("gender"));
		student.setStatus(rs.getString("status"));
		student.setCgpa(rs.getDouble("cgpa"));
		student.setBacklogs(rs.getInt("backlogs"));
		student.setCollegeEmail(rs.getString("collegeEmail"));
		return student;
	};

	private final RowMapper<PlacementDrive> driveMapper = (rs, rowNum) -> {
		PlacementDrive drive = new PlacementDrive();
		drive.setPldId(rs.getInt("pld_id"));
		drive.setPldClgId(rs.getInt("pld_clg_id"));
		drive.setPldCmpId(rs.getInt("pld_cmp_id"));
		drive.setPldName(rs.getString("pld_name"));
		drive.setPldStartDate(rs.getDate("pld_start_date").toLocalDate());
		drive.setPldEndDate(rs.getObject("pld_end_date", LocalDate.class));
		drive.setPldStatus(rs.getString("pld_status"));
		drive.setCmpName(rs.getString("cmp_name"));
		return drive;
	};

	private final RowMapper<Resume> resumeMapper = (rs, rowNum) -> {
		Resume resume = new Resume();
		resume.setResId(rs.getInt("res_id"));
		resume.setResUsrId(rs.getString("res_usr_id"));
		resume.setResFile(rs.getString("res_file"));
		resume.setResUploadDate(rs.getObject("res_upload_date", LocalDate.class));
		return resume;
	};

	private final RowMapper<AttendedDrive> attendedDriveMapper = (rs, rowNum) -> {
		AttendedDrive drive = new AttendedDrive();
		drive.setPldId(rs.getInt("pld_id"));
		drive.setUsrId(rs.getString("usr_id"));
		drive.setStatus(rs.getString("status"));
		drive.setFizzledRound(rs.getString("fizzled_round"));
		drive.setPldName(rs.getString("pld_name"));
		drive.setCmpName(rs.getString("cmp_name"));
		drive.setHphId(rs.getObject("hph_id", Integer.class));
		drive.setHphName(rs.getString("hph_name"));
		drive.setHphSequence(rs.getObject("hph_sequence", Integer.class));
		return drive;
	};

	private final RowMapper<Notification> notificationMapper = (rs, rowNum) -> {
		Notification notification = new Notification();
		notification.setNtfId(rs.getInt("ntf_id"));
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
		application.setAppDate(rs.getDate("app_date").toLocalDate());
		application.setAppStatus(rs.getString("app_status"));
		return application;
	};

	public Student getStudentById(String rollNo) {
		List<Student> students = jdbcTemplate.query("SELECT * FROM students WHERE RollNo = ?", studentMapper, rollNo);
		return students.isEmpty() ? null : students.get(0);
	}

	public void updateStudent(Student student) {
		jdbcTemplate.update(
				"UPDATE students SET FullName = ?, collegeEmail = ?, cgpa = ?, backlogs = ? WHERE RollNo = ?",
				student.getFullName(), student.getCollegeEmail(), student.getCgpa(), student.getBacklogs(),
				student.getRollNo());
	}

	public List<Resume> getResumesByUserId(String usrId) {
		return jdbcTemplate.query("SELECT * FROM resumes WHERE res_usr_id = ?", resumeMapper, usrId);
	}

	public void addResume(Resume resume) {
		jdbcTemplate.update("INSERT INTO resumes (res_usr_id, res_file, res_upload_date) VALUES (?, ?, CURRENT_DATE)",
				resume.getResUsrId(), resume.getResFile());
	}

	public void deleteResume(Integer resId) {
		jdbcTemplate.update("DELETE FROM resumes WHERE res_id = ?", resId);
	}

	public List<PlacementDrive> getUpcomingDrives() {
		return jdbcTemplate.query(
				"SELECT pd.*, c.cmp_name " + "FROM placement_drives pd "
						+ "JOIN companies c ON pd.pld_cmp_id = c.cmp_id " + "WHERE pd.pld_start_date >= CURRENT_DATE",
				driveMapper);
	}

	public List<PlacementDrive> getEligibleDrives(String usrId) {
		return jdbcTemplate.query("SELECT pd.*, c.cmp_name " + "FROM placement_drives pd "
				+ "JOIN companies c ON pd.pld_cmp_id = c.cmp_id "
				+ "JOIN screening_criteria sc ON pd.pld_id = sc.scr_pld_id " + "JOIN students s ON s.RollNo = ? "
				+ "WHERE s.cgpa >= sc.scr_min_gpa " + "  AND (sc.scr_brn_id IS NULL OR s.branch_id = sc.scr_brn_id)",
				driveMapper, usrId);
	}

	public boolean hasApplied(String usrId, Integer pldId) {
		Integer count = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM applications WHERE app_usr_id = ? AND app_pld_id = ?", Integer.class, usrId,
				pldId);
		return count != null && count > 0;
	}

	public void submitApplication(String usrId, Integer pldId, Integer cmpId, String resumeFile) {
		jdbcTemplate.update("INSERT INTO applications (app_usr_id, app_pld_id, app_cmp_id, app_date, app_status) "
				+ "VALUES (?, ?, ?, CURRENT_DATE, 'APPL')", usrId, pldId, cmpId);
		jdbcTemplate.update(
				"INSERT INTO notifications (ntf_usr_id, ntf_message, ntf_date, ntf_read) "
						+ "VALUES (?, ?, CURRENT_DATE, FALSE)",
				usrId, "Application for drive ID " + pldId + " received.");
	}

	public List<AttendedDrive> getAttendedDrives(String usrId) {
		return jdbcTemplate.query("SELECT ad.*, pd.pld_name, c.cmp_name, hp.hph_id, hp.hph_name, hp.hph_sequence "
				+ "FROM attended_drives ad " + "JOIN placement_drives pd ON ad.pld_id = pd.pld_id "
				+ "JOIN companies c ON pd.pld_cmp_id = c.cmp_id "
				+ "LEFT JOIN hiring_phases hp ON pd.pld_id = hp.hph_pld_id " + "WHERE ad.usr_id = ? "
				+ "ORDER BY pd.pld_id, hp.hph_sequence", attendedDriveMapper, usrId);
	}

	public List<Notification> getNotifications(String usrId) {
		return jdbcTemplate.query("SELECT * FROM notifications WHERE ntf_usr_id = ? ORDER BY ntf_date DESC",
				notificationMapper, usrId);
	}

	public void markNotificationRead(Integer ntfId) {
		jdbcTemplate.update("UPDATE notifications SET ntf_read = TRUE WHERE ntf_id = ?", ntfId);
	}

	public List<Object[]> getApplicationCounts() {
		return jdbcTemplate.query(
				"SELECT c.cmp_name, pd.pld_name, COUNT(a.app_id) as app_count " + "FROM applications a "
						+ "JOIN placement_drives pd ON a.app_pld_id = pd.pld_id "
						+ "JOIN companies c ON a.app_cmp_id = c.cmp_id " + "GROUP BY c.cmp_name, pd.pld_name",
				(rs, rowNum) -> new Object[] { rs.getString("cmp_name"), rs.getString("pld_name"),
						rs.getInt("app_count") });
	}

	public List<Application> getApplicationsByUserId(String usrId) {
		return jdbcTemplate.query("SELECT * FROM applications WHERE app_usr_id = ?", applicationMapper, usrId);
	}
}
