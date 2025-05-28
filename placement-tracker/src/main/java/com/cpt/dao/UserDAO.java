package com.cpt.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cpt.model.User;

@Repository
public class UserDAO implements UserDAOInt {
	private JdbcTemplate jdbcTemplate;

	public UserDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private final RowMapper<User> userMapper = (rs, rowNum) -> {
		User user = new User();
		user.setUsrId(rs.getString("usr_id"));
		user.setUsrPassword(rs.getString("usr_password"));
		user.setUsrRole(rs.getString("usr_role"));
		return user;
	};

	@Override
	public boolean changePassword(String usrId, String currPasswd, String newPasswd) {
		String qry = "update users set usr_password = ? where usr_id = ?";
		Integer cnt = jdbcTemplate.update(qry, newPasswd, usrId);
		return cnt != null && cnt > 0;
	}

	// public List<String> getNotifications(String userId) {
	// String sql = "select ntf_message from notifications where ntf_usr_id = ? order by ntf_date";
	// List<String> li = jdbcTemplate.queryForList(sql, new Object[] { userId }, String.class);
	// return li;
	// }

	@Override
	public String getRole(String userId, String passwd) {
		String sql = "select usr_role from users where usr_id = ? and usr_password = ?";
		System.out.println(userId + " " + passwd);
		try {
			String role = jdbcTemplate.queryForObject(sql, new Object[] { userId, passwd }, String.class);
			return role;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public User findById(String userId) {
		List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE usr_id = ?", userMapper, userId);
		return users.isEmpty() ? null : users.get(0);
	}

	@Override
	public List<Map<String, Object>> getNotifications(String usr_id) {
		return jdbcTemplate.queryForList(
				"SELECT ntf_message, ntf_date FROM notifications WHERE ntf_usr_id = ? AND ntf_read = FALSE ORDER BY ntf_date DESC",
				usr_id);
	}
}
