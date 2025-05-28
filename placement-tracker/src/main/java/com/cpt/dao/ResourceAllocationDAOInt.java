package com.cpt.dao;

import java.util.List;

import com.cpt.model.ResourceAllocation;

public interface ResourceAllocationDAOInt {

	List<ResourceAllocation> findAll();

	void save(ResourceAllocation allocation);

}
