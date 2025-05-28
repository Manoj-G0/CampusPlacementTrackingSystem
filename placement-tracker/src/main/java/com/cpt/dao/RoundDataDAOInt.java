package com.cpt.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cpt.model.RoundData;
import com.cpt.model.RoundParameter;
import com.cpt.model.StudentRoundStatus;

public interface RoundDataDAOInt {

	List<Map<String, Object>> getStudentsInDrives();

	List<RoundParameter> getStudentDrillDown(String studentId, int pldId);

	List<Map<String, Object>> getPhaseStudentCounts(int pldId);

	int getTotalStudents(int pldId);

	List<StudentRoundStatus> getStudentRoundDataByDrive(int pldId);

	Set<String> getRoundNamesByDrive(int pldId);

	List<RoundData> getRoundData();

}
