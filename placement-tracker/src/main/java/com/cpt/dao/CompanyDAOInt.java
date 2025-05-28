package com.cpt.dao;

import java.util.List;

import com.cpt.model.Company;
import com.cpt.model.CompanyCategories;
import com.cpt.model.CompanyStatsDTO;

public interface CompanyDAOInt {

	void update(Company company, int cmpid);

	List<CompanyCategories> findAllCategories();

	Company getCompanyById(int company_id);

	List<Company> findAll();

	void save(Company company);

	List<CompanyStatsDTO> getAllCompaniesWithStats();

}
