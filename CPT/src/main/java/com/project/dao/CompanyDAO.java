package com.project.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.model.Company;

@Repository
public class CompanyDAO {
	private final JdbcTemplate jdbcTemplate;

	public CompanyDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Company> findAll() {
		String sql = "SELECT * FROM companies";
		return jdbcTemplate.query(sql, this::mapRowToCompany);
	}

	public void save(Company company) {
		String sql = "INSERT INTO companies (cmp_cat_id, cmp_name, cmp_description) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, company.getCategoryId(), company.getName(), company.getDescription());
	}

	private Company mapRowToCompany(ResultSet rs, int rowNum) throws SQLException {
		Company company = new Company();
		company.setId(rs.getShort("cmp_id"));
		company.setCategoryId(rs.getShort("cmp_cat_id"));
		company.setName(rs.getString("cmp_name"));
		company.setDescription(rs.getString("cmp_description"));
		return company;
	}
}