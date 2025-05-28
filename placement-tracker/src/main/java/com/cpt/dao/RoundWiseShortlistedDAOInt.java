package com.cpt.dao;

import java.util.List;

import com.cpt.model.RoundWiseShortlisted;

public interface RoundWiseShortlistedDAOInt {

	void saveList(int phase_id, int pldId, String stdId, int score, String pname, int selectedCompanyId);

	List<RoundWiseShortlisted> getAllShortlisted();

}
