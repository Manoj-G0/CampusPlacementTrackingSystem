package com.cpt.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpt.dao.ApplicationDAOInt;
import com.cpt.dao.CompanyDAOInt;
import com.cpt.dao.CompanyTeamDAOInt;
import com.cpt.dao.HRDAOInt;
import com.cpt.dao.HiringPhaseDAOInt;
import com.cpt.dao.PlacementDriveDAOInt;
import com.cpt.dao.RoundDataDAOInt;
import com.cpt.dao.StudentDAOInt;
import com.cpt.model.Application;
import com.cpt.model.ApplicationDTO;
import com.cpt.model.Company;
import com.cpt.model.CompanyCategories;
import com.cpt.model.CompanyTeam;
import com.cpt.model.HR;
import com.cpt.model.HiringPhase;
import com.cpt.model.Parameter;
import com.cpt.model.Phase;
import com.cpt.model.PhaseData;
import com.cpt.model.PlacementDrive;
import com.cpt.model.Resume;
import com.cpt.model.Round;
import com.cpt.model.RoundData;
import com.cpt.model.RoundParameter;
import com.cpt.model.RoundWiseShortlisted;
import com.cpt.model.ScreeningCriteria;
import com.cpt.model.Shortlisted;
import com.cpt.model.Student;
import com.cpt.model.StudentRoundStatus;

@Service
public class HRService {

	@Autowired
	private HRDAOInt hrDAO;
	@Autowired
	private PlacementDriveDAOInt driveDAO;
	@Autowired
	private ApplicationDAOInt applicationDAO;
	@Autowired
	private CompanyTeamDAOInt companyTeamDAO;
	@Autowired
	private CompanyDAOInt companyDAO;
	@Autowired
	private HiringPhaseDAOInt hiringPhaseDAO;
	@Autowired
	private StudentDAOInt studentDAO;
	@Autowired
	private RoundDataDAOInt roundDataDAO;

	// To get the application count
	public long getApplicationCount(String userId) {
		HR hr = hrDAO.findById(userId);
		List<PlacementDrive> drives = driveDAO.findByCompanyId(hr.getCmpId());
		return drives.stream().mapToLong(drive -> applicationDAO.countByPldId(drive.getPldId())).sum();
	}

	// To get ongoing drives by user Id
	public List<PlacementDrive> getOngoingDrives(String userId) {
		HR hr = hrDAO.findById(userId);
		return driveDAO.findByOngoingCompanyId(hr.getCmpId());
	}

	// To get completed drives

	public List<PlacementDrive> getCompletedDrives(String userId) {
		HR hr = hrDAO.findById(userId);
		return driveDAO.findByCompanyId(hr.getCmpId()).stream()
				.filter(drive -> drive.getEndDate() != null && drive.getEndDate().before(Date.valueOf(LocalDate.now())))
				.collect(Collectors.toList());
	}

	public List<PlacementDrive> getPlacementDrivesByCompany(String hr_id) {
		// TODO Auto-generated method stub
		return driveDAO.getPlacementDrivesByCompany(hr_id);
	}

	// To get the application for drives based on user Id and placement Id
	public List<Application> getApplicationsForDrive(Integer pldId, String userId) {
		HR hr = hrDAO.findById(userId);
		PlacementDrive drive = driveDAO.findById(pldId);
		if (!drive.getCompanyId().equals(hr.getCmpId())) {
			throw new RuntimeException("Unauthorized");
		}
		return applicationDAO.findByPldId(pldId);
	}

	// to add company details
	public void addCompanyTeam(CompanyTeam team, String userId) {
		HR hr = hrDAO.findById(userId);
		team.setCtmCmpId(hr.getCmpId());
		companyTeamDAO.save(team);
	}

	public HR getHRProfile(String userId) {
		return hrDAO.findById(userId);
	}
	// HR Integration

	// to get the upcoming drives
	public List<PlacementDrive> getUpcomingDrivesByCompany(int cmpId) {
		return hrDAO.findUpcomingDrivesByCompany(cmpId);
	}

	public Company getCompanyByHrId(String hrId) {

		return hrDAO.findCompanyByHrId(hrId);
	}

	// To get the hiring phases
	public List<HiringPhase> getHiringPhases(int pldId) {
		return hrDAO.getHiringPhases(pldId);
	}

	// to get the round wise short listed
	public List<RoundWiseShortlisted> getRoundWiseShortlisted(int pldId) {
		return hrDAO.getRoundWiseShortlisted(pldId);
	}

	// To get the final list
	public List<Shortlisted> getFinalShortlisted(int pldId) {
		return hrDAO.getFinalShortlisted(pldId);
	}

	public List<RoundWiseShortlisted> getRoundWiseShortlistedByRound(int pldId, String round) {
		return hrDAO.getRoundWiseShortlistedByRound(pldId, round);
	}

	// To update the application status
	public void updateApplicationStatus(int pldId, String studentId, String status, String remarks) {
		hrDAO.updateApplicationStatus(pldId, studentId, status, remarks);
	}

	public List<Company> getAllCompanies() {
		return hrDAO.findAllCompanies();
	}

	// To get hr user Id
	public HR getHrUserById(String id) {

		return hrDAO.findById(id);
	}

	public List<CompanyTeam> getTeamsByCompany(int cmpId) {
		return hrDAO.findTeamsByCompany(cmpId);
	}

	// To get the ongoing drives
	public List<PlacementDrive> getOngoingDrivesByCompany(int cmpId) {
		return hrDAO.findOngoingDrivesByCompany(cmpId);
	}

	// To get completed drives
	public List<PlacementDrive> getCompletedDrivesByCompany(int cmpId) {
		return hrDAO.findCompletedDrivesByCompany(cmpId);
	}

	public List<ApplicationDTO> getCandidates(int pld_id) {
		return hrDAO.getApplicationsByPldId(pld_id);
	}

	// To add company team
	public int addCompanyTeam(CompanyTeam companyTeam) {
		return hrDAO.save(companyTeam);
	}

	public List<Application> getCandidatesByRound(int pld_id, String round) {
		return hrDAO.findCandidatesByRound(pld_id, round);
	}

	// To get the details of the rounds
	public List<String> getRoundsList(int pld_id) {
		return hrDAO.findRoundsByPldId(pld_id);
	}

	public void saveHR(HR hr) {
		hrDAO.save(hr);
	}

	public List<String> getAllColleges() {
		return hrDAO.findAllColleges();
	}

	// to get college Id by college name
	public int getCollgeId(String college_name) {
		return hrDAO.findCollegeId(college_name);
	}

	public String getDriveName(int pldId) {
		return hrDAO.findDriveName(pldId);
	}

	// To get the company categories
	public List<CompanyCategories> getCategories() {
		List<CompanyCategories> categories = companyDAO.findAllCategories();
		return categories;
	}

	// To add the company
	public void addCompany(Company company) {
		companyDAO.save(company);
	}

	// To update the company
	public void updateCompany(Company company, int cmpid) {
		companyDAO.update(company, cmpid);
	}

	// To save the rounds
	public void saveRounds(List<Round> roundDTOs, int pld_id) {
		List<HiringPhase> phases = new ArrayList<>();
		for (Round dto : roundDTOs) {
			HiringPhase phase = new HiringPhase();
			phase.setHphName(dto.getRoundName());
			phase.setHphSequence(dto.getSequence());

			double tot_score = 0;
			double tot_threshold = 0;

			List<Parameter> parameters = new ArrayList<>();
			for (Parameter paramDTO : dto.getParameters()) {

				Parameter param = new Parameter();
				param.setParameterName(paramDTO.getParameterName());
				param.setTotalScore(paramDTO.getTotalScore());
				param.setThreshold(paramDTO.getThreshold());

				tot_score = tot_score + param.getTotalScore();
				tot_threshold = tot_threshold + param.getThreshold();

			}
			int hph_id = driveDAO.addDetailsPhases(pld_id, dto.getRoundName(), dto.getSequence(), tot_score,
					tot_threshold);

			for (Parameter paramDTO : dto.getParameters()) {

				Parameter param = new Parameter();
				param.setParameterName(paramDTO.getParameterName());
				param.setTotalScore(paramDTO.getTotalScore());
				param.setThreshold(paramDTO.getThreshold());

				driveDAO.addDetailsPrameters(hph_id, param.getParameterName(), param.getThreshold(),
						param.getTotalScore());

				tot_score = tot_score + param.getTotalScore();
				tot_threshold = tot_threshold + param.getThreshold();
			}
			phase.setParameters(parameters);
			phases.add(phase);

		}

	}

	public List<Phase> getPhasesById(int pld_id) {
		return hiringPhaseDAO.getPhasesByPldId(pld_id);
	}

	// To get students by phase Id
	public List<PhaseData> getStudentsByPhaseId(int hph_id, int pld_id) {
		return hiringPhaseDAO.getStudentsByPhaseId(hph_id, pld_id);
	}

	public Double getThresholdByPhaseId(Integer hph_id, int pld_id) {
		return hiringPhaseDAO.getThresholdByPhaseId(hph_id, pld_id);
	}

	// To get the updated threshold value
	public void updateThresholdByPhaseId(int hph_id, double modifiedThreshold) {

		hiringPhaseDAO.updateThresholdByPhaseId(hph_id, modifiedThreshold);
	}

	public void addCriteriaDetails(ScreeningCriteria sc, int pld_id) {
		driveDAO.addCriteriaDetails(sc, pld_id);

	}

	// To get all students details
	public List<Student> getAllStudentsDetails() {
		return driveDAO.getAllStudents();
	}

	// To filter the students based on the criteria
	public void filterAndSaveStudents(double cgpa, int backlogs, String gender, int brn_id, int pld_id) {

		List<Student> students = getAllStudentsDetails();
		List<Student> filteredStudents;
		System.out.println(cgpa + " " + backlogs + " " + gender + " " + brn_id + " " + pld_id);
		filteredStudents = students.stream().filter(student -> student.getCgpa() >= cgpa
				&& student.getBranchId() == brn_id && student.getBacklogs() <= backlogs).collect(Collectors.toList());

		for (Student s : filteredStudents) {
			driveDAO.addEligibleStudents(s.getRollNo(), pld_id);
		}

	}

	// To get resumes based on the Id
	public Resume getResumesByUserId(String usrId) {
		return hrDAO.getResumesByUserId(usrId);
	}

	public PlacementDrive getPldId(String userId) {
		return hrDAO.findPldId(userId);
	}

	// To get the student round status
	public Map<String, Map<String, String>> getStudentRoundStatus() {
		List<RoundData> roundDataList = roundDataDAO.getRoundData();
		Map<String, Map<String, String>> studentRoundStatus = new HashMap<>();
		for (RoundData data : roundDataList) {
			String studentId = data.getStudent_id();
			String phaseName = data.getPhase_name();
			String status = data.getStatus();
			String studentName = data.getStudent_name();
			studentRoundStatus.putIfAbsent(studentId, new HashMap<>());
			Map<String, String> studentData = studentRoundStatus.get(studentId);
			if (phaseName != null) {
				studentData.put("name", studentName);
				studentData.put(phaseName, status);
			}
		}
		return studentRoundStatus;
	}

	// To get the round names
	public Set<String> getRoundNames() {
		List<RoundData> roundDataList = roundDataDAO.getRoundData();
		Set<String> roundNames = new LinkedHashSet<>();
		for (RoundData data : roundDataList) {
			String phaseName = data.getPhase_name();
			roundNames.add(phaseName);
		}
		return roundNames;
	}

	public List<StudentRoundStatus> getStudentRoundStatus(int pldId) {
		return roundDataDAO.getStudentRoundDataByDrive(pldId);
	}

	// To ge5t the round names by the drive Id
	public Set<String> getRoundNamesByDrive(int pldId) {
		return roundDataDAO.getRoundNamesByDrive(pldId);
	}

	public int getTotalStudents(int pldId) {
		return roundDataDAO.getTotalStudents(pldId);
	}

	// To get the students phase count
	public List<Map<String, Object>> getPhaseStudentCounts(int pldId) {
		return roundDataDAO.getPhaseStudentCounts(pldId);
	}

	public List<RoundParameter> getStudentDrillDown(String studentId, int pldId) {
		return roundDataDAO.getStudentDrillDown(studentId, pldId);
	}

	// To get all the placements by the company Id
	public List<PlacementDrive> getAllPlacementsByCmpId(int cmpId) {
		return hrDAO.findAllPlacementsByCmpId(cmpId);
	}

	public int getPlacementId() {
		// TODO Auto-generated method stub
		return hrDAO.getPlacementId();
	}

	public boolean checkIfPldIdExists(int pld_id) {
		return hiringPhaseDAO.existsByPldId(pld_id);
	}

}