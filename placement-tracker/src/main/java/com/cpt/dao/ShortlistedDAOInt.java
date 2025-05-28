package com.cpt.dao;

import java.util.List;

import com.cpt.model.Shortlisted;

public interface ShortlistedDAOInt {

	boolean check(String stdid);

	List<Shortlisted> getShortlisted();

	void saveList(int pldId, String stdId);

}
