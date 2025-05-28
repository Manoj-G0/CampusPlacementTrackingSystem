package com.cpt.dao;

import java.util.List;

import com.cpt.model.Faculty;

public interface FacultyDAOInt {

	List<Faculty> findAvailable();

	void assignFaculty(Integer facId, Integer pldId);

}
