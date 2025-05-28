package com.cpt.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cpt.dao.ApplicationDAOInt;
import com.cpt.dao.FaqDAOInt;
import com.cpt.dao.NotificationDAOInt;
import com.cpt.dao.PhaseEvaluationDAOInt;
import com.cpt.dao.PlacementDriveDAOInt;
import com.cpt.dao.StudentDAOInt;
import com.cpt.model.Application;
import com.cpt.model.AttendedDrive;
import com.cpt.model.EligibleDrive;
import com.cpt.model.Notification;
import com.cpt.model.PlacementDrive;
import com.cpt.model.Profile;
import com.cpt.model.Resume;
import com.cpt.model.Student;

@Service
public class StudentService {

	@Autowired
	private ApplicationDAOInt applicationDAO;
	@Autowired
	private NotificationDAOInt notificationDAO;
	@Autowired
	private StudentDAOInt studentDAO;
	@Autowired
	private PhaseEvaluationDAOInt phaseEvaluationDAO;
	@Autowired
	private PlacementDriveDAOInt placementDriveDAO;
	@Autowired
	private FaqDAOInt faqDao;

	@Autowired
	JdbcTemplate jdbcTemplate;

	// To get the students by ID
	public Student getStudentById(String rollNo) {
		return studentDAO.findById(rollNo);
	}

	// To get the resumes by user ID
	public Resume getResumesByUserId(String usrId) {
		return studentDAO.getResumesByUserId(usrId);
	}

	// To add the resumes
	public void addResume(Resume resume) {
		studentDAO.addResume(resume);
	}

	public void deleteResume(String usrId) {
		studentDAO.deleteResume(usrId);
	}

	public Student getStudentProfiles(String usrId) {
		return studentDAO.getStudentById(usrId);
	}

	// To get the profile by user Id
	public Profile getProfile(String usrId) {
		return studentDAO.getProfile(usrId);
	}

	// To update the profile
	public void updateProfile(Profile profile) {
		if (profile.getPrfUsrId() == null || profile.getPrfUsrId().isEmpty()) {
			throw new IllegalArgumentException("User ID is required.");
		}
		if (profile.getEmail() == null || profile.getEmail().isEmpty()) {
			throw new IllegalArgumentException("Email is required.");
		}
		studentDAO.updateStudentProfile(profile);
	}

	public List<EligibleDrive> getEligibleDrives(String usrId) {
		return studentDAO.getEligibleDrives(usrId);
	}

	// student who has applied
	public boolean hasApplied(String usrId, Integer pldId) {
		return studentDAO.hasApplied(usrId, pldId);
	}

	public void submitApplication(String usrId, Integer pldId, Integer cmpId) {
		studentDAO.submitApplication(usrId, pldId, cmpId);
	}

	// To get the attended drives
	public List<AttendedDrive> getAttendedDrives(String usrId) {
		return studentDAO.getAttendedDrives(usrId);
	}

	public List<Map<String, Object>> getAttendedDrivesForJson(String usrId) {
		return placementDriveDAO.getAttendedDrives(usrId);
	}

	public void markNotificationRead(Integer notifId) {
		studentDAO.markNotificationRead(notifId);
	}

	// to get the application counts
	public List<Object[]> getApplicationCounts() {
		return studentDAO.getApplicationCounts();
	}

	public List<Application> getApplicationsByUserId(String usrId) {
		return studentDAO.getApplicationsByUserId(usrId);
	}

	// to generate csv template
	public byte[] generateCsvTemplate() {
		String header = "rollno,fullname,branch_id,college_id,gender,status,cgpa,backlogs,collegeemail\n";
		return header.getBytes();
	}

	// To parse the file
	public List<Student> parseFile(MultipartFile file) throws IOException {
		List<Student> students = new ArrayList<>();
		String fileName = file.getOriginalFilename().toLowerCase();

		if (fileName.endsWith(".csv") || fileName.endsWith(".txt")) {
			students = parseCsv(file.getInputStream());
		} else if (fileName.endsWith(".xls")) {
			students = parseExcel(file.getInputStream());
		}

		return students;
	}

	// To parse the csv
	private List<Student> parseCsv(InputStream inputStream) throws IOException {
		List<Student> students = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		boolean firstLine = true;

		while ((line = reader.readLine()) != null) {
			if (firstLine) {
				firstLine = false;
				continue; // Skip header
			}
			String[] data = line.split(",");
			if (data.length == 9) {
				try {
					Student student = new Student();
					student.setRollNo(data[0].trim());
					student.setFullName(data[1].trim());
					student.setBranchId(Long.parseLong(data[2].trim()));
					student.setClgId(Long.parseLong(data[3].trim()));
					student.setGender(data[4].trim());
					student.setStatus(data[5].trim());
					student.setCgpa(Double.parseDouble(data[6].trim()));
					student.setBacklogs(Integer.parseInt(data[7].trim()));
					student.setCollegeEmail(data[8].trim());
					students.add(student);
				} catch (Exception e) {
					System.err.println("Error parsing line: " + line + ", Error: " + e.getMessage());
				}
			}
		}
		reader.close();
		return students;
	}

	// To parse the excel
	private List<Student> parseExcel(InputStream inputStream) throws IOException {
		List<Student> students = new ArrayList<>();
		Workbook workbook = new HSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		boolean firstRow = true;

		for (Row row : sheet) {
			if (firstRow) {
				firstRow = false;
				continue; // Skip header
			}
			if (row.getPhysicalNumberOfCells() >= 9) {
				try {
					Student student = new Student();
					student.setRollNo(getCellValue(row.getCell(0)));
					student.setFullName(getCellValue(row.getCell(1)));
					student.setBranchId(Long.parseLong(getCellValue(row.getCell(2))));
					student.setClgId(Long.parseLong(getCellValue(row.getCell(3))));
					student.setGender(getCellValue(row.getCell(4)));
					student.setStatus(getCellValue(row.getCell(5)));
					student.setCgpa(Double.parseDouble(getCellValue(row.getCell(6))));
					student.setBacklogs(Integer.parseInt(getCellValue(row.getCell(7))));
					student.setCollegeEmail(getCellValue(row.getCell(8)));
					students.add(student);
				} catch (Exception e) {
					System.err.println("Error parsing row: " + row.getRowNum() + ", Error: " + e.getMessage());
				}
			}
		}
		workbook.close();
		return students;
	}

	private String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			return String.valueOf((int) cell.getNumericCellValue());
		default:
			return "";
		}
	}

	public void saveStudents(List<Student> students) {
		studentDAO.saveStudents(students);
	}

	public int getCmpId(int pldId) {
		return studentDAO.getCmpId(pldId);
	}

	public boolean resumeExists(String usrId) {
		return studentDAO.resumeExists(usrId);
	}

	public List<Map<String, Object>> getDriveScores(String usrId, Integer pldId) {
		return placementDriveDAO.getDriveScores(usrId, pldId);
	}

	//

	public List<PlacementDrive> getUpcomingDrives() {
		return placementDriveDAO.findUpcomingDrives();
	}

	public List<Application> getApplications(String userId) {
		return applicationDAO.findByUserId(userId);
	}

	public long getUnreadNotificationCount(String userId) {
		return notificationDAO.countUnreadByUserId(userId);
	}

	public List<Notification> getNotifications(String userId) {
		return notificationDAO.findByUserId(userId);
	}

	public void markNotificationRead(Long ntfId, String userId) {
		Notification notification = notificationDAO.findById(ntfId);
		if (!notification.getNtfUsrId().equals(userId)) {
			throw new RuntimeException("Unauthorized");
		}
		notification.setNtfRead(true);
		notificationDAO.update(notification);
	}

	// Applied for the drive
	public void applyForDrive(String userId, Integer pldId) {
		PlacementDrive drive = placementDriveDAO.findById(pldId);
		Application application = new Application();
		application.setAppUsrId(userId);
		application.setAppPldId(pldId);
		application.setAppDate(Date.valueOf(LocalDate.now()));
		application.setAppStatus("APPL");
		applicationDAO.save(application);

		Notification notification = new Notification();
		notification.setNtfUsrId(userId);
		notification.setNtfMessage("Application submitted for " + drive.getName());
		notification.setNtfDate(LocalDate.now());
		notification.setNtfRead(false);
		notificationDAO.save(notification);
	}

	public Student getStudentProfile(String userId) {
		return studentDAO.findById(userId);
	}

	// To update the student profile
	public void updateProfile(String userId, Student updatedStudent) {
		Student student = studentDAO.findById(userId);
		student.setFullName(updatedStudent.getFullName());
		student.setGender(updatedStudent.getGender());
		student.setCgpa(updatedStudent.getCgpa());
		student.setBacklogs(updatedStudent.getBacklogs());
		student.setCollegeEmail(updatedStudent.getCollegeEmail());
		studentDAO.updateProfile(student);
	}

	public Object getSelectedDrives(String usrId) {
		return studentDAO.getSelectedDrives(usrId);
	}

	// To get the Student drives
	public List<Map<String, Object>> getStudentDrives(String usrId) {
		List<Map<String, Object>> upcomingDrives = placementDriveDAO.getUpcomingDrives();
		List<Map<String, Object>> attendedDrives = placementDriveDAO.getAttendedDrives(usrId);

		List<Map<String, Object>> drives = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		LocalDate today = LocalDate.now();

		// Process upcoming drives
		for (Map<String, Object> result : upcomingDrives) {
			Map<String, Object> drive = new HashMap<>();
			drive.put("pldId", result.get("pld_id") != null ? result.get("pld_id") : 0);
			drive.put("name", result.get("pld_name") != null ? result.get("pld_name") : "Unknown");
			drive.put("company", result.get("cmp_name") != null ? result.get("cmp_name") : "N/A");
			drive.put("startDate",
					result.get("pld_start_date") != null ? sdf.format((Date) result.get("pld_start_date")) : "N/A");
			drive.put("endDate",
					result.get("pld_end_date") != null ? sdf.format((Date) result.get("pld_end_date")) : "N/A");
			drive.put("location", result.get("clg_address") != null ? result.get("clg_address") : "Main Campus");
			drive.put("applicants",
					result.get("applicant_count") != null ? ((Number) result.get("applicant_count")).longValue() : 0L);
			drive.put("type", "upcoming");
			drive.put("status", "UPCOMING");
			drives.add(drive);
		}

		// Process attended drives
		for (Map<String, Object> result : attendedDrives) {
			Map<String, Object> drive = new HashMap<>();
			drive.put("pldId", result.get("pld_id") != null ? result.get("pld_id") : 0);
			drive.put("name", result.get("pld_name") != null ? result.get("pld_name") : "Unknown");
			drive.put("company", result.get("cmp_name") != null ? result.get("cmp_name") : "N/A");
			drive.put("startDate",
					result.get("pld_start_date") != null ? sdf.format((Date) result.get("pld_start_date")) : "N/A");
			drive.put("endDate",
					result.get("pld_end_date") != null ? sdf.format((Date) result.get("pld_end_date")) : "N/A");
			drive.put("location", result.get("clg_address") != null ? result.get("clg_address") : "Main Campus");
			drive.put("applicants",
					result.get("applicant_count") != null ? ((Number) result.get("applicant_count")).longValue() : 0L);
			drive.put("type", "attended");
			drive.put("status", "COMPLETED");
			drives.add(drive);
		}

		return drives;
	}

	// To get the FAQs Questions
	public List<Map<String, String>> getFAQQuestions() {
		return faqDao.getFAQQuestions();
	}

	// To get FAQs Answers
	public String getFAQAnswers(int faqid) {
		return faqDao.getFAQAnswers(faqid);
	}

	public List<Student> getAllStudents() {
		return studentDAO.getAllStudents();
	}
}
