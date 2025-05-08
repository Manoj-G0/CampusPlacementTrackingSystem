package com.cpt.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.User;

@Repository
public class UserDAO {

	private final JdbcTemplate jdbcTemplate;

	public UserDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// Row Mapper for User
	private final RowMapper<User> userMapper = (rs, rowNum) -> {
		User user = new User();
		user.setUsrId(rs.getString("usr_id"));
		user.setUsrPassword(rs.getString("usr_password"));
		user.setUsrRole(rs.getString("usr_role"));
		return user;
	};

	/**
	 * Retrieves the role of a user given their user ID and password.
	 *
	 * @param userId The user's ID (e.g., 21s91a0501, poid12321322, hrinfosys).
	 * @param passwd The user's password.
	 * @return The user's role (STUD, ADMIN, HR) or null if credentials are invalid.
	 */
	public String getRole(String userId, String passwd) {
		String sql = "SELECT usr_role FROM users WHERE usr_id = ? AND usr_password = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { userId, passwd }, String.class);
		} catch (EmptyResultDataAccessException e) {
			// Invalid credentials or user not found
			return null;
		}
	}

	public User findById(String userId) {
		List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE usr_id = ?", userMapper, userId);
		return users.isEmpty() ? null : users.get(0);
	}

	public String getPassword(String usrId) {
		try {
			return jdbcTemplate.queryForObject("SELECT usr_password FROM users WHERE usr_id = ?", String.class, usrId);
		} catch (Exception e) {
			return null;
		}
	}
}