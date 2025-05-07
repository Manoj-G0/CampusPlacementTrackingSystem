package com.project.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.dao.ApplicationDAO;
import com.project.dao.BranchDAO;
import com.project.dao.CollegeDAO;
import com.project.dao.CompanyDAO;
import com.project.dao.EvaluationDAO;
import com.project.dao.HiringPhaseDAO;
import com.project.dao.JobDescriptionDAO;
import com.project.dao.NotificationDAO;
import com.project.dao.PlacementDriveDAO;
import com.project.dao.ProfileDAO;
import com.project.dao.ResourceAllocationDAO;
import com.project.dao.UserDAO;
import com.project.model.Application;
import com.project.model.Branch;
import com.project.model.College;
import com.project.model.Company;
import com.project.model.Evaluation;
import com.project.model.HiringPhase;
import com.project.model.JobDescription;
import com.project.model.Notification;
import com.project.model.PlacementDrive;
import com.project.model.Profile;
import com.project.model.ResourceAllocation;
import com.project.model.User;

@Service
public class PlacementService {
	private final UserDAO userDAO;
	private final ProfileDAO profileDAO;
	private final CollegeDAO collegeDAO;
	private final BranchDAO branchDAO;
	private final CompanyDAO companyDAO;
	private final PlacementDriveDAO placementDriveDAO;
	private final ApplicationDAO applicationDAO;
	private final NotificationDAO notificationDAO;
	private final JobDescriptionDAO jobDescriptionDAO;
	private final HiringPhaseDAO hiringPhaseDAO;
	private final EvaluationDAO evaluationDAO;
	private final ResourceAllocationDAO resourceAllocationDAO;
	private final JdbcTemplate jdbcTemplate;

	public PlacementService(UserDAO userDAO, ProfileDAO profileDAO, CollegeDAO collegeDAO, BranchDAO branchDAO,
			CompanyDAO companyDAO, PlacementDriveDAO placementDriveDAO, ApplicationDAO applicationDAO,
			NotificationDAO notificationDAO, JobDescriptionDAO jobDescriptionDAO, HiringPhaseDAO hiringPhaseDAO,
			EvaluationDAO evaluationDAO, ResourceAllocationDAO resourceAllocationDAO, JdbcTemplate jdbcTemplate) {
		this.userDAO = userDAO;
		this.profileDAO = profileDAO;
		this.collegeDAO = collegeDAO;
		this.branchDAO = branchDAO;
		this.companyDAO = companyDAO;
		this.placementDriveDAO = placementDriveDAO;
		this.applicationDAO = applicationDAO;
		this.notificationDAO = notificationDAO;
		this.jobDescriptionDAO = jobDescriptionDAO;
		this.hiringPhaseDAO = hiringPhaseDAO;
		this.evaluationDAO = evaluationDAO;
		this.resourceAllocationDAO = resourceAllocationDAO;
		this.jdbcTemplate = jdbcTemplate;
	}

	// User Management
	public User getUserByEmail(String email) {
		return userDAO.findByEmail(email);
	}

	public User getUserById(Short id) {
		return userDAO.findById(id);
	}

	public void saveUser(User user) {
		user.setCreationDate(LocalDate.now());
		userDAO.save(user);
	}

	public List<User> getAllStudents() {
		return userDAO.findByRole("STUDENT");
	}

	public List<User> getAllTPOs() {
		return userDAO.findByRole("TPO");
	}

	public List<User> getAllHRs() {
		return userDAO.findByRole("HR");
	}

	// Profile Management
	public Profile getProfileByUserId(Short userId) {
		return profileDAO.findByUserId(userId);
	}

	public void saveProfile(Profile profile) {
		profile.setUpdated(LocalDate.now());
		profileDAO.save(profile);
	}

	// College Management
	public List<College> getAllColleges() {
		return collegeDAO.findAll();
	}

	public void saveCollege(College college) {
		collegeDAO.save(college);
	}

	public void updateCollege(College college) {
		collegeDAO.update(college);
	}

	// Branch Management
	public List<Branch> getAllBranches() {
		return branchDAO.findAll();
	}

	public void saveBranch(Branch branch) {
		branchDAO.save(branch);
	}

	// Company Management
	public List<Company> getAllCompanies() {
		return companyDAO.findAll();
	}

	public void saveCompany(Company company) {
		companyDAO.save(company);
	}

	// Placement Drive Management
	public List<PlacementDrive> getAllPlacementDrives() {
		return placementDriveDAO.findAll();
	}

	public PlacementDrive getPlacementDriveById(Short id) {
		return placementDriveDAO.findById(id);
	}

	public void savePlacementDrive(PlacementDrive drive) {
		placementDriveDAO.save(drive);
		notifyStudents("New placement drive created: " + drive.getName());
	}

	// Application Management
	public List<Application> getApplicationsByUserId(Short userId) {
		return applicationDAO.findByUserId(userId);
	}

	public List<Application> getApplicationsByPlacementDriveId(Short placementDriveId) {
		return applicationDAO.findByPlacementDriveId(placementDriveId);
	}

	public Application getApplicationById(Short id) {
		String sql = "SELECT * FROM applications WHERE app_id = ?";
		return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
			Application app = new Application();
			app.setId(rs.getShort("app_id"));
			app.setUserId(rs.getShort("app_usr_id"));
			app.setPlacementDriveId(rs.getShort("app_pld_id"));
			app.setCompanyId(rs.getShort("app_cmp_id"));
			app.setApplicationDate(rs.getDate("app_date") != null ? rs.getDate("app_date").toLocalDate() : null);
			app.setStatus(rs.getString("app_status"));
			return app;
		}, id);
	}

	public void saveApplication(Application application) {
		applicationDAO.save(application);
		Notification notification = new Notification();
		notification.setUserId(application.getUserId());
		notification.setMessage(
				"Your application for drive ID " + application.getPlacementDriveId() + " has been submitted.");
		notification.setDate(LocalDate.now());
		notification.setRead(false);
		notificationDAO.save(notification);
	}

	public void updateApplicationStatus(Short id, String status) {
		applicationDAO.updateStatus(id, status);
		Application app = getApplicationById(id);
		if ("APPR".equals(status)) {
			notifyStudent(app.getUserId(),
					"Congratulations! You have been selected for drive ID " + app.getPlacementDriveId());
		} else if ("REJC".equals(status)) {
			notifyStudent(app.getUserId(),
					"Your application for drive ID " + app.getPlacementDriveId() + " was not selected.");
		}
	}

	// Notification Management
	public List<Notification> getNotificationsByUserId(Short userId) {
		return notificationDAO.findByUserId(userId);
	}

	public void markNotificationAsRead(Short id) {
		notificationDAO.updateReadStatus(id, true);
	}

	// Resume Management
	public void saveResume(Short userId, MultipartFile file) throws IOException {
		String uploadDir = "uploads/resumes/";
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		String fileName = userId + "_" + file.getOriginalFilename();
		Files.write(uploadPath.resolve(fileName), file.getBytes());
		String sql = "INSERT INTO resumes (res_usr_id, res_file, res_upload_date) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, userId, fileName, LocalDate.now());
	}

	// Job Description Management
	public void saveJobDescription(JobDescription job) {
		jobDescriptionDAO.save(job);
		notifyTPO("New job description added for drive ID " + job.getPlacementDriveId() + ": " + job.getRole());
	}

	public List<JobDescription> getJobDescriptionsByPlacementDriveId(Short placementDriveId) {
		return jobDescriptionDAO.findByPlacementDriveId(placementDriveId);
	}

	// Hiring Phase Management
	public void saveHiringPhase(HiringPhase phase) {
		hiringPhaseDAO.save(phase);
		notifyTPO("New hiring phase added for drive ID " + phase.getPlacementDriveId() + ": " + phase.getName());
	}

	public List<HiringPhase> getHiringPhasesByPlacementDriveId(Short placementDriveId) {
		return hiringPhaseDAO.findByPlacementDriveId(placementDriveId);
	}

	// Evaluation Management
	public void saveEvaluation(Evaluation evaluation) {
		evaluationDAO.save(evaluation);
		Application app = getApplicationById(evaluation.getApplicationId());
		notifyStudent(app.getUserId(),
				"Evaluation updated for drive ID " + app.getPlacementDriveId() + ": Score " + evaluation.getScore());
	}

	public List<Evaluation> getEvaluationsByApplicationId(Short applicationId) {
		return evaluationDAO.findByApplicationId(applicationId);
	}

	// Resource Allocation Management
	public void saveResourceAllocation(ResourceAllocation allocation) {
		resourceAllocationDAO.save(allocation);
		notifyTPO("New resource allocation for drive ID " + allocation.getPlacementDriveId() + ": "
				+ allocation.getType());
	}

	public List<ResourceAllocation> getResourceAllocationsByPlacementDriveId(Short placementDriveId) {
		return resourceAllocationDAO.findByPlacementDriveId(placementDriveId);
	}

	// Eligibility Check for Drives
	public List<PlacementDrive> getEligibleDrives(Short userId) {
		Profile profile = profileDAO.findByUserId(userId);
		List<Application> applications = applicationDAO.findByUserId(userId);
		double highestPackage = applications.stream().filter(app -> "APPR".equals(app.getStatus()))
				.map(app -> getDrivePackage(app.getPlacementDriveId())).filter(pkg -> pkg != null)
				.mapToDouble(Double::doubleValue).max().orElse(0.0);

		return getAllPlacementDrives().stream().filter(drive -> isEligible(profile, drive, highestPackage))
				.collect(Collectors.toList());
	}

	private boolean isEligible(Profile profile, PlacementDrive drive, double highestPackage) {
		// Check backlogs
		if (profile.getBacklogs() > drive.getMaxBacklogs()) {
			return false;
		}
		// Check GPA
		if (profile.getGpa() < drive.getMinGpa()) {
			return false;
		}
		// Check branch
		if (!drive.getAllowedBranches().contains(String.valueOf(profile.getBranchId()))) {
			return false;
		}
		// Check if already placed with high package
		if (highestPackage > 10_00_000) {
			return false;
		}
		// Check package difference for placed students
		if (highestPackage > 0 && (drive.getPackageAmount() - highestPackage) <= 3_00_000) {
			return false;
		}
		return true;
	}

	private Double getDrivePackage(Short driveId) {
		PlacementDrive drive = placementDriveDAO.findById(driveId);
		return drive != null ? drive.getPackageAmount() : null;
	}

	// Notification Helpers
	private void notifyStudents(String message) {
		List<User> students = userDAO.findByRole("STUDENT");
		for (User student : students) {
			Notification notification = new Notification();
			notification.setUserId(student.getId());
			notification.setMessage(message);
			notification.setDate(LocalDate.now());
			notification.setRead(false);
			notificationDAO.save(notification);
		}
	}

	private void notifyTPO(String message) {
		List<User> tpos = userDAO.findByRole("TPO");
		for (User tpo : tpos) {
			Notification notification = new Notification();
			notification.setUserId(tpo.getId());
			notification.setMessage(message);
			notification.setDate(LocalDate.now());
			notification.setRead(false);
			notificationDAO.save(notification);
		}
	}

	private void notifyStudent(Short userId, String message) {
		Notification notification = new Notification();
		notification.setUserId(userId);
		notification.setMessage(message);
		notification.setDate(LocalDate.now());
		notification.setRead(false);
		notificationDAO.save(notification);
	}
}