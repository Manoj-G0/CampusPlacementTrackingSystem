package com.cpt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cpt.dao.StudentDAO;
import com.cpt.model.Application;
import com.cpt.model.AttendedDrive;
import com.cpt.model.Notification;
import com.cpt.model.PlacementDrive;
import com.cpt.model.Resume;
import com.cpt.model.Student;
import com.cpt.model.User;

@Service
public class StudentService {

	private final StudentDAO studentDao;
	private final UserService userService;

	public StudentService(StudentDAO studentDao, UserService userService) {
		this.studentDao = studentDao;
		this.userService = userService;
	}

	public Student getStudentById(String rollNo) {
		return studentDao.getStudentById(rollNo);
	}

	public boolean isStudent(String usrId) {
		User user = userService.findById(usrId);
		return user != null && "STUD".equals(user.getUsrRole());
	}

	public void updateStudent(Student student) {
		studentDao.updateStudent(student);
	}

	public List<Resume> getResumesByUserId(String usrId) {
		return studentDao.getResumesByUserId(usrId);
	}

	public void addResume(Resume resume) {
		studentDao.addResume(resume);
	}

	public void deleteResume(Integer resId) {
		studentDao.deleteResume(resId);
	}

	public List<PlacementDrive> getUpcomingDrives() {
		return studentDao.getUpcomingDrives();
	}

	public List<PlacementDrive> getEligibleDrives(String usrId) {
		return studentDao.getEligibleDrives(usrId);
	}

	public boolean hasApplied(String usrId, Integer pldId) {
		return studentDao.hasApplied(usrId, pldId);
	}

	public void submitApplication(String usrId, Integer pldId, Integer cmpId, String resumeFile) {
		studentDao.submitApplication(usrId, pldId, cmpId, resumeFile);
	}

	public List<AttendedDrive> getAttendedDrives(String usrId) {
		return studentDao.getAttendedDrives(usrId);
	}

	public List<Notification> getNotifications(String usrId) {
		return studentDao.getNotifications(usrId);
	}

	public void markNotificationRead(Integer ntfId) {
		studentDao.markNotificationRead(ntfId);
	}

	public List<Object[]> getApplicationCounts() {
		return studentDao.getApplicationCounts();
	}

	public List<Application> getApplicationsByUserId(String usrId) {
		return studentDao.getApplicationsByUserId(usrId);
	}
}