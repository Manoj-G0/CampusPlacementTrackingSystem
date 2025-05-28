package com.cpt.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.util.Pair;

import com.cpt.model.DriveSubmit;
import com.cpt.model.Resource;
import com.cpt.model.ResourceCrudDTO;
import com.cpt.model.ResourceDTO;

public interface ResourceDAOInt {

	List<Integer> getRounds(int pid);

	List<String> getFaculty(int no);

	int getAppliedStu(int pid);

	int getClgId(String cname);

	Map<String, Object> getDrDet(String dname);

	List<Pair<String, String>> getAsRes(DriveSubmit ds);

	int getStu(int round);

	List<String> getDrives();

	void addRes(Resource res);

	List<String> getCollegesList();

	List<ResourceDTO> getResourcesForDrives(List<String> driveNames);

	void insertAllocationsWithDelta(Map<Integer, Integer> cumulativeResources, List<String> facultyList, int pldId,
			DriveSubmit ds);

	void clearExistingAllocationsByPldId(int pldId);

	List<ResourceCrudDTO> getAllResources();

	void updateResource(ResourceCrudDTO resource);

	void deleteResource(int resourceId);

	List<Map<String, Object>> getBranchesByCollegeId(String clgId);

}
