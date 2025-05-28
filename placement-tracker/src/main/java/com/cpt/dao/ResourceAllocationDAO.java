package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.ResourceAllocation;

@Repository
public class ResourceAllocationDAO implements ResourceAllocationDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void save(ResourceAllocation allocation) {
		String sql = "INSERT INTO resource_allocations (pld_id, resource_type, resource_name, allocation_date) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, allocation.getPldId(), allocation.getResourceType(), allocation.getResourceName(),
				allocation.getAllocationDate());
	}

	@Override
	public List<ResourceAllocation> findAll() {
		String sql = "SELECT ra.*, pd.pld_name FROM resource_allocations ra JOIN placement_drives pd ON ra.ral_pld_id = pd.pld_id";
		return jdbcTemplate.query(sql, new ResourceAllocationRowMapper());
	}

	private static class ResourceAllocationRowMapper implements RowMapper<ResourceAllocation> {
		@Override
		public ResourceAllocation mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResourceAllocation allocation = new ResourceAllocation();
			allocation.setId(rs.getInt("ral_id"));
			allocation.setPldId(rs.getInt("ral_pld_id"));
			allocation.setResourceType(rs.getString("ral_resource_type"));
			// allocation.setResourceName(rs.getString("resource_name"));
			allocation.setAllocationDate(rs.getObject("ral_date", LocalDate.class));
			// allocation.setDriveName(rs.getString("pld_name"));
			return allocation;
		}
	}
}
