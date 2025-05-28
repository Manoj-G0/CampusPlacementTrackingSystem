package com.cpt.dao;

import java.util.List;

import com.cpt.model.Branch;

public interface BranchDAOInt {

	void updateBranch(Branch branch);

	Branch findById(int brnId);

	List<Branch> findAll();

	void save(Branch branch);

	String getBranchName(int brn_id);

}
