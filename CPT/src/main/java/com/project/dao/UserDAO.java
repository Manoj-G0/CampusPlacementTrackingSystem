package com.project.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.model.User;

@Repository
public class UserDAO {
	private final JdbcTemplate jdbcTemplate;

	public UserDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<User> findAll() {
		String sql = "SELECT * FROM users";
		return jdbcTemplate.query(sql, this::mapRowToUser);
	}

	public User findById(Short id) {
		String sql = "SELECT * FROM users WHERE usr_id = ?";
		return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
	}

	public User findByEmail(String email) {
		String sql = "SELECT * FROM users WHERE usr_email = ?";
		return jdbcTemplate.queryForObject(sql, this::mapRowToUser, email);
	}

	public void save(User user) {
		String sql = "INSERT INTO users (usr_email, usr_password, usr_name, usr_phone, usr_cdate, usr_role) VALUES (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getName(), user.getPhone(),
				user.getCreationDate(), user.getRole());
	}

	public List<User> findByRole(String role) {
		String sql = "SELECT * FROM users WHERE usr_role = ?";
		return jdbcTemplate.query(sql, this::mapRowToUser, role);
	}

	private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getShort("usr_id"));
		user.setEmail(rs.getString("usr_email"));
		user.setPassword(rs.getString("usr_password"));
		user.setName(rs.getString("usr_name"));
		user.setPhone(rs.getString("usr_phone"));
		user.setCreationDate(rs.getDate("usr_cdate") != null ? rs.getDate("usr_cdate").toLocalDate() : null);
		user.setRole(rs.getString("usr_role"));
		return user;
	}
}