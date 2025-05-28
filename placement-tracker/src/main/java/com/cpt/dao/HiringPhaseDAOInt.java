package com.cpt.dao;

import java.util.List;
import java.util.Map;

import com.cpt.model.HiringPhase;
import com.cpt.model.Phase;
import com.cpt.model.PhaseData;

public interface HiringPhaseDAOInt {

	Map<String, Integer> getCriteria(String pname);

	List<HiringPhase> getAll(int selectedCompanyId);

	void updateThresholdByPhaseId(int hphid, double newThreshold);

	Double getThresholdByPhaseId(int hphId, int pldId);

	List<PhaseData> getStudentsByPhaseId(int hphId, int pldId);

	List<Phase> getPhasesByPldId(int pldId);

	boolean existsByPldId(int pld_id);

}
