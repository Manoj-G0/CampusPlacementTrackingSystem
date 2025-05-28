package com.cpt.dao;

import java.util.List;
import java.util.Map;

import com.cpt.model.PlacementDrive;

public interface ReportDAOInt {

	List<Map<String, Object>> getPhasesByDrive(int pldId);

	List<Map<String, Object>> getRoundwiseAllShortlisted();

	List<Map<String, Object>> getRoundwiseShortlisted(int pld_id, int phase_id);

	List<PlacementDrive> getAllPlacements();

	List<Map<String, Object>> getStudentsPlacementDetails();

	List<Map<String, Object>> getBranchPlacementDetails();

	List<Map<String, Object>> getDriveDetails();

	List<Map<String, Object>> getPlacementStats();

}
