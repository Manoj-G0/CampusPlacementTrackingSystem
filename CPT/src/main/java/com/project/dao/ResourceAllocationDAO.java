package com.project.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.model.ResourceAllocation;

@Repository
public class ResourceAllocationDAO {
	private final JdbcTemplate jdbcTemplate;

	public ResourceAllocationDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<ResourceAllocation> findByPlacementDriveId(Short placementDriveId) {
		String sql = "SELECT * FROM resource_allocations WHERE rsa_pld_id = ?";
		return jdbcTemplate.query(sql, this::mapRowToResourceAllocation, placementDriveId);
	}

	public void save(ResourceAllocation allocation) {
		String sql = "INSERT INTO resource_allocations (rsa_pld_id, rsa_type, rsa_quantity, rsa_date) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, allocation.getPlacementDriveId(), allocation.getType(), allocation.getQuantity(),
				allocation.getDate());
	}

	private ResourceAllocation mapRowToResourceAllocation(ResultSet rs, int rowNum) throws SQLException {
		ResourceAllocation allocation = new ResourceAllocation();
		allocation.setId(rs.getShort("rsa_id"));
		allocation.setPlacementDriveId(rs.getShort("rsa_pld_id"));
		allocation.setType(rs.getString("rsa_type"));
		allocation.setQuantity(rs.getInt("rsa_quantity"));
		allocation.setDate(rs.getDate("rsa_date") != null ? rs.getDate("rsa_date").toLocalDate() : null);
		return allocation;
	}
}