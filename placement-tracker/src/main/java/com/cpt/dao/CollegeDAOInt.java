package com.cpt.dao;

import java.util.List;

import com.cpt.model.College;

public interface CollegeDAOInt {

	void save(College college);

	List<College> findAll();

	void updateCollege(College college);

	void removeCollege(int id);

	College getCollegeById(int id);

}
