package com.cpt.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cpt.model.DriveSubmit;
import com.cpt.model.Resource;
import com.cpt.model.ResourceCrudDTO;
import com.cpt.model.ResourceDTO;

@Repository
public class ResourceDAO implements ResourceDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Method to fetch resources for the list of drive names
	@Override
	public List<ResourceDTO> getResourcesForDrives(List<String> driveNames) {
		// SQL query to retrieve the required columns from the database

//    	String sql1 = "select ral_id from resource_allocations_temp where ral_pld_name IN (?)";
//    	int count = jdbcTemplate.queryForObject(sql1,driveNames.get(0), Integer.class);
		// Construct a query with placeholders for each item in driveNames
		String sql = "SELECT ral_id, ral_pld_id, ral_pld_name, ral_resource_id, ral_phase_id, "
				+ "ral_capacity, ral_occupied, ral_faculty_name " + "FROM resource_allocations "
				+ "WHERE ral_pld_name IN (" + String.join(",", Collections.nCopies(driveNames.size(), "?")) + ")";

		// Execute the query with the list of drive names
		List<ResourceDTO> rlist = jdbcTemplate.query(sql, driveNames.toArray(), (rs, rowNum) -> {
			ResourceDTO resourceDTO = new ResourceDTO();
			resourceDTO.setRalId(rs.getInt("ral_id"));
			resourceDTO.setRalPldId(rs.getInt("ral_pld_id"));
			resourceDTO.setRalPldName(rs.getString("ral_pld_name"));
			resourceDTO.setRalResourceId(rs.getInt("ral_resource_id"));
			resourceDTO.setRalPhaseId(rs.getInt("ral_phase_id"));
			resourceDTO.setRalCapacity(rs.getInt("ral_capacity"));
			resourceDTO.setRalOccupied(rs.getInt("ral_occupied"));

			resourceDTO.setRalFacultyName(rs.getString("ral_faculty_name"));
			return resourceDTO;
		});

		return rlist;
	}
	// ---------------------------------------------------------------

//	@Override
//	public List<String> getCollegesList() {
//		// TODO Auto-generated method stub
//		String sql = "select clg_name from colleges";
//		return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("clg_name"));
//	}

//	@Override
//	public void addRes(Resource res) {
//		// TODO Auto-generated method stub
//		String sql = "insert into RESOURCES values(?,?,?,?,?)";
//		jdbcTemplate.update(sql, res.getResourceId(), res.getClg_id(), res.getCollege(), res.getBranch(),
//				res.getCapacity());
//	}

	@Override
	public List<String> getDrives() {
		// TODO Auto-generated method stub
		String sql = "select pld_name from placement_drives where pld_status = 'NOT ASSIGNED'";
		return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("pld_name"));
	}

	@Override
	public int getStu(int round) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from round_wise_shortlisted where phase_id = ?";
		int no = jdbcTemplate.queryForObject(sql, Integer.class, round);
		return no;
	}

	@Override
	public List<Pair<String, String>> getAsRes(DriveSubmit ds) {
		// TODO Auto-generated method stub
		String sql = "select resource_id,resource_capacity from resources where status = 'Available' and clg_id = ?";
		return jdbcTemplate.query(sql, new Object[] { ds.getClg_id() },
				(rs, rowNum) -> Pair.of(rs.getString("resource_id"), rs.getString("resource_capacity")));
	}

	@Override
	public Map<String, Object> getDrDet(String dname) {
		// TODO Auto-generated method stub
		String sql = "select pld_id, pld_clg_id from placement_drives where pld_name = ?";
		return jdbcTemplate.queryForMap(sql, dname);
	}

	@Override
	public int getClgId(String cname) {
		String sql = "select clg_id from colleges where clg_name = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, cname);
	}

	@Override
	public int getAppliedStu(int pid) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from applications where app_pld_id = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, pid);
	}

	@Override
	public List<String> getFaculty(int no) {
		// TODO Auto-generated method stub
		String sql = "select fac_name from faculty where status = ? limit ?";
		List<Object> flist = jdbcTemplate.query(sql, new Object[] { "NOT ASSIGNED", no },
				(rs, rowNum) -> rs.getString("fac_name"));
		List<String> rlist = new ArrayList<>();
		for (Object obj : flist) {
			String name = (String) obj;
			rlist.add(name);
		}
		return rlist;
	}

	@Override
	public List<Integer> getRounds(int pid) {
		// TODO Auto-generated method stub
		String sql = "select hph_sequence from hiring_phases where hph_pld_id = ?";
		return jdbcTemplate.query(sql, new Object[] { pid }, (rs, rowNum) -> rs.getInt("hph_sequence"));
	}

	@Override
	public void insertAllocationsWithDelta(Map<Integer, Integer> cumulativeResources, List<String> facultyList,
			int pldId, DriveSubmit ds) {
		int phaseId = 1; // You can pass this as param if needed
		int i = 0;
		int previousCumulative = 0;

		// Sort resource IDs to ensure consistent order
		List<Integer> sortedResourceIds = new ArrayList<>(cumulativeResources.keySet());
		Collections.sort(sortedResourceIds);

		for (Integer resourceId : sortedResourceIds) {
			int cumulative = cumulativeResources.get(resourceId);
			int occupied = cumulative - previousCumulative; // delta occupied
			previousCumulative = cumulative;

			String facultyName = (i < facultyList.size()) ? facultyList.get(i) : "N/A";

			// Get resource capacity
			Integer capacity = jdbcTemplate.queryForObject(
					"SELECT resource_capacity FROM resources WHERE resource_id = ?", new Object[] { resourceId },
					Integer.class);

			// Insert into resource_allocations
			jdbcTemplate.update(
					"INSERT INTO resource_allocations (ral_pld_id, ral_pld_name, ral_resource_id, ral_phase_id, ral_capacity, ral_occupied, ral_faculty_name) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?)",
					pldId, ds.getDriveName(), resourceId, ds.getRounds(), capacity, occupied, facultyName);

			// Update resource status to OCCUPIED
			jdbcTemplate.update("UPDATE resources SET status = 'Occupied' WHERE resource_id = ?", resourceId);

			// Update faculty status to ASSIGNED
			jdbcTemplate.update("UPDATE faculty SET status = 'ASSIGNED' WHERE fac_name = ?", facultyName);

			i++;
		}
	}

	@Override
	public void clearExistingAllocationsByPldId(int pldId) {
		// Step 1: Get all resource IDs and faculty names associated with this pld_id
		String selectSql = "SELECT ral_resource_id, ral_faculty_name FROM resource_allocations WHERE ral_pld_id = ?";
		List<Map<String, Object>> records = jdbcTemplate.queryForList(selectSql, pldId);

		// Step 2: Update statuses back to NOT ASSIGNED
		for (Map<String, Object> row : records) {
			Integer resourceId = (Integer) row.get("ral_resource_id");
			String facultyName = (String) row.get("ral_faculty_name");

			if (resourceId != null) {
				jdbcTemplate.update("UPDATE resources SET status = 'Available' WHERE resource_id = ?", resourceId);
			}

			if (facultyName != null && !facultyName.isEmpty()) {
				jdbcTemplate.update("UPDATE faculty SET status = 'NOT ASSIGNED' WHERE fac_name = ?", facultyName);
			}
		}

		// Step 3: Delete the old allocations
		jdbcTemplate.update("DELETE FROM resource_allocations WHERE ral_pld_id = ?", pldId);
	}

	@Override
	public List<String> getCollegesList() {
		// TODO Auto-generated method stub
		String sql = "select clg_name from colleges";
		return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("clg_name"));
	}

	@Override
	public List<ResourceCrudDTO> getAllResources() {
		String sql = "SELECT r.resource_id, c.clg_name, b.brn_name, r.resource_capacity " + "FROM resources r "
				+ "JOIN colleges c ON r.clg_id = c.clg_id " + "JOIN branches b ON r.brn_id = b.brn_id";
		List<ResourceCrudDTO> rlist = jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> {
			ResourceCrudDTO dto = new ResourceCrudDTO();
			dto.setResourceId(rs.getInt("resource_id"));
			dto.setClgName(rs.getString("clg_name"));
			dto.setBrnName(rs.getString("brn_name"));
			dto.setResourceCapacity(rs.getInt("resource_capacity"));
			return dto;
		});
		return rlist;
	}

	@Override
	public void updateResource(ResourceCrudDTO resource) {
		String getClgId = "SELECT clg_id FROM colleges WHERE clg_name = ?";
		String getBrnId = "SELECT brn_id FROM branches WHERE brn_name = ? AND brn_clg_id = (SELECT clg_id FROM colleges WHERE clg_name = ?)";

		Integer clgId = jdbcTemplate.queryForObject(getClgId, new Object[] { resource.getClgName() }, Integer.class);
		Integer brnId = jdbcTemplate.queryForObject(getBrnId,
				new Object[] { resource.getBrnName(), resource.getClgName() }, Integer.class);

		String sql = "UPDATE resources SET clg_id=?, brn_id=?, resource_capacity=? WHERE resource_id=?";
		jdbcTemplate.update(sql, clgId, brnId, resource.getResourceCapacity(), resource.getResourceId());
	}

	@Override
	public void deleteResource(int resourceId) {
		String sql = "DELETE FROM resources WHERE resource_id=?";
		jdbcTemplate.update(sql, resourceId);
	}

	@Override
	public void addRes(Resource res) {

		String sql1 = "select brn_id from branches where brn_clg_id = ? and brn_name = ?";
		int brn_id = jdbcTemplate.queryForObject(sql1, new Object[] { res.getClg_id(), res.getBranch() },
				Integer.class);
		String sql = "insert into RESOURCES(resource_id,clg_id,resource_capacity,status,brn_id) values(?,?,?,?,?)";
		jdbcTemplate.update(sql, res.getResourceId(), res.getClg_id(), res.getCapacity(), "Available", brn_id);
	}

	@Override
	public List<Map<String, Object>> getBranchesByCollegeId(String clgId) {
		String sql1 = "select clg_id from colleges where clg_name = ?";
		int id = jdbcTemplate.queryForObject(sql1, new Object[] { clgId }, Integer.class);
		String sql = "SELECT brn_id,brn_name FROM branches WHERE brn_clg_id = ?";
		return jdbcTemplate.queryForList(sql, id);
	}

}
