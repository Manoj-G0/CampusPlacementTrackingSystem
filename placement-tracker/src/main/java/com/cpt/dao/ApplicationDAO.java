package com.cpt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.Application;

@Repository
public class ApplicationDAO implements ApplicationDAOInt {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void save(Application application) {
		String sql = "INSERT INTO applications (app_usr_id, app_pld_id, app_date, app_status) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, application.getAppUsrId(), application.getAppPldId(), application.getAppDate(),
				application.getAppStatus());
	}

	@Override
	public List<Application> findByUserId(String usrId) {
		String sql = "SELECT * FROM applications WHERE app_usr_id = ?";
		return jdbcTemplate.query(sql, new Object[] { usrId }, new ApplicationRowMapper());
	}

	@Override
	public List<Application> findByPldId(Integer pldId) {
		String sql = "SELECT * FROM applications WHERE app_pld_id = ?";
		return jdbcTemplate.query(sql, new Object[] { pldId }, new ApplicationRowMapper());
	}

	@Override
	public long countByPldId(Integer pldId) {
		String sql = "SELECT COUNT(*) FROM applications WHERE app_pld_id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { pldId }, Long.class);
	}

	@Override
	public long countPlaced() {
		String sql = "SELECT COUNT(*) FROM shortlisted";
		return jdbcTemplate.queryForObject(sql, Long.class);
	}

	private static class ApplicationRowMapper implements RowMapper<Application> {
		@Override
		public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
			Application application = new Application();
			application.setAppId(rs.getInt("app_id"));
			application.setAppUsrId(rs.getString("app_usr_id"));
			application.setAppPldId(rs.getInt("app_pld_id"));
			application.setAppDate(rs.getDate("app_date"));
			application.setAppStatus(rs.getString("app_status"));
			return application;
		}
	}
}
