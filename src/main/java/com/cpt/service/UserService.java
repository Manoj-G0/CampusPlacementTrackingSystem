package com.cpt.service;

import org.springframework.stereotype.Service;

import com.cpt.dao.UserDAO;
import com.cpt.model.User;

@Service
public class UserService {

	private final UserDAO userDao;

	public UserService(UserDAO userDao) {
		this.userDao = userDao;
	}

	public String getRole(String userId, String passwd) {
		return userDao.getRole(userId, passwd);
	}

	public User findById(String userId) {
		return userDao.findById(userId);
	}

	public boolean validateUser(String usrId, String usrPassword) {
		String storedPassword = userDao.getPassword(usrId);
		return storedPassword != null && storedPassword.equals(usrPassword); // Replace with BCrypt in production
	}
}