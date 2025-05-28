package com.cpt.dao;

import java.util.List;

import com.cpt.model.Application;
import com.cpt.model.AttendedDrive;
import com.cpt.model.EligibleDrive;
import com.cpt.model.Notification;
import com.cpt.model.Profile;
import com.cpt.model.Resume;
import com.cpt.model.SelectedDrive;
import com.cpt.model.Student;
import com.cpt.model.StudentStatsDTO;

public interface StudentDAOInt {

	boolean resumeExists(String usrId);

	int getCmpId(int pldId);

	void saveStudents(List<Student> students);

	List<Application> getApplicationsByUserId(String usrId);

	List<Object[]> getApplicationCounts();

	void markNotificationRead(Integer ntfId);

	List<Notification> getNotifications(String usrId);

	List<SelectedDrive> getSelectedDrives(String usrId);

	List<AttendedDrive> getAttendedDrives(String usrId);

	void submitApplication(String usrId, Integer pldId, Integer cmpId);

	boolean hasApplied(String usrId, Integer pldId);

	List<EligibleDrive> getEligibleDrives(String usrId);

	void deleteResume(String usrId);

	void addResume(Resume resume);

	Resume getResumesByUserId(String usrId);

	Student findById(String usrId);

	long count();

	void updateProfile(Student student);

	void updateStudentProfile(Profile profile);

	Profile getProfile(String usrId);

	Student getStudentById(String RollNo);

	List<Student> getAllStudents();

	List<StudentStatsDTO> getAllStudentsWithStats();

}
