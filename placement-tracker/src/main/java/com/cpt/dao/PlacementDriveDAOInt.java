package com.cpt.dao;

import java.util.List;
import java.util.Map;

import com.cpt.model.Branch;
import com.cpt.model.PlacementDrive;
import com.cpt.model.ScreeningCriteria;
import com.cpt.model.Student;

public interface PlacementDriveDAOInt {

	int addEligibleStudents(String rollno, int pld_id);

	String getBranchName(int brn_id);

	int addCriteriaDetails(ScreeningCriteria sc, int pld_id);

	List<Student> getAllStudents();

	int addDetailsPhases(int pld_id, String roundName, int sequence, double totalScore, double totalThreshold);

	int addDetailsPrameters(int pld_id, String name, double score, double threshold);

	List<Branch> getBranchDetails();

	void updateDriveSchedule(PlacementDrive drive);

	List<Map<String, Object>> getDriveScores(String usrId, Integer pldId);

	List<Map<String, Object>> getAttendedDrives(String usrId);

	List<Map<String, Object>> getUpcomingDrives();

	Map<String, Object> getDriveDetails(int pldId);

	List<Map<String, Object>> getAllDrives();

	Map<Integer, Map<String, Double>> getRoundSuccessRates();

	void deleteDrive(int pldId);

	void updateDrive(PlacementDrive drive);

	long countActive();

	List<PlacementDrive> findAll();

	PlacementDrive findById(Integer pldId);

	List<PlacementDrive> findByCompanyId(Integer cmpId);

	List<PlacementDrive> findUpcomingDrives();

	void save(PlacementDrive drive);

	List<PlacementDrive> getPlacementDrivesByCompany(String hr_id);

	List<PlacementDrive> findByOngoingCompanyId(Integer cmpId);

}
