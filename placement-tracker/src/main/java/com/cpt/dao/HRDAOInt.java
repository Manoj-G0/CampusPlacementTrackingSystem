package com.cpt.dao;

import java.util.List;

import com.cpt.model.Application;
import com.cpt.model.ApplicationDTO;
import com.cpt.model.Company;
import com.cpt.model.CompanyTeam;
import com.cpt.model.HR;
import com.cpt.model.HiringPhase;
import com.cpt.model.PlacementDrive;
import com.cpt.model.Resume;
import com.cpt.model.RoundWiseShortlisted;
import com.cpt.model.Shortlisted;

public interface HRDAOInt {

	List<PlacementDrive> findOngoingDrivesByCompany(int cmpId);

	List<CompanyTeam> findTeamsByCompany(int cmpId);

	Company findCompanyByHrId(String hrId);

	HR findById(String id);

	void updateApplicationStatus(int pldId, String studentId, String status, String remarks);

	Resume getResumesByUserId(String usrId);

	String findDriveName(int pldId);

	List<HiringPhase> getHiringPhases(int pldId);

	int findCollegeId(String college_name);

	List<Company> findAllCompanies();

	List<String> findAllColleges();

	void save(HR hr);

	List<String> findRoundsByPldId(int pldId);

	List<Application> findCandidatesByRound(int pldId, String round);

	int save(CompanyTeam companyTeam);

	List<ApplicationDTO> getApplicationsByPldId(int pldId);

	List<PlacementDrive> findUpcomingDrivesByCompany(int cmpId);

	List<PlacementDrive> findCompletedDrivesByCompany(int cmpId);

	List<RoundWiseShortlisted> getRoundWiseShortlisted(int pldId);

	List<Shortlisted> getFinalShortlisted(int pldId);

	List<RoundWiseShortlisted> getRoundWiseShortlistedByRound(int pldId, String round);

	PlacementDrive findPldId(String userId);

	List<PlacementDrive> findAllPlacementsByCmpId(int cmpId);

	int getPlacementId();

}
