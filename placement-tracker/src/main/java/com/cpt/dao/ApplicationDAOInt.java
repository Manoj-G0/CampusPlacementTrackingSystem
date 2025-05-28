package com.cpt.dao;

import java.util.List;

import com.cpt.model.Application;

public interface ApplicationDAOInt {

	long countPlaced();

	long countByPldId(Integer pldId);

	List<Application> findByPldId(Integer pldId);

	List<Application> findByUserId(String usrId);

	void save(Application application);

}
