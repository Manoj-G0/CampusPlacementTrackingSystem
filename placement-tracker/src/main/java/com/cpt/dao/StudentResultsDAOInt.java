package com.cpt.dao;

import java.util.List;

public interface StudentResultsDAOInt {

	void saveList(int phase_id, int pldId, String stdId, int param_id, int score);

	List<Integer> getParameters(int pldId);

}
