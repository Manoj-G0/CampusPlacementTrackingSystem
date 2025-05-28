package com.cpt.dao;

import java.util.List;
import java.util.Map;

import com.cpt.model.Admin;
import com.cpt.model.College;
import com.cpt.model.Company;
import com.cpt.model.HR;
import com.cpt.model.Student;
import com.cpt.model.StudentStatsDTO;

public interface AdminDAOInt {

	boolean existsByRollNo(String rollNo);

	boolean existsByCollegeEmail(String collegeEmail);

	void save(Student student);

	void save(HR hr);

	void addAdmin(Admin admin);

	int updateCompany(Company company);

	int deleteCompany(int cmpId);

	List<College> getEveryCollege();

	int getPhaseId(int pldId, String pname);

	String getLastPhase(int pldId);

	void addtoAttended(String stdId, int pldId, int phId);

	List<StudentStatsDTO> getAllStudentsWithStats();

	Map<String, Object> getStudentRoundWiseStatus(int pldId);

}
