package com.cpt.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpt.dao.UserDAOInt;

@Service
public class UserService {
	@Autowired
	private UserDAOInt userDao;

	// To get the notifications
	public List<Map<String, Object>> getNotifications(String userId) {
		return userDao.getNotifications(userId);
	}

	// To change the password
	public boolean changePassword(String usrId, String currentPassword, String newPassword) {
		return userDao.changePassword(usrId, currentPassword, newPassword);
	}

	// To get the role
	public String getRole(String usr, String passwd) {
		return userDao.getRole(usr, passwd);
	}
}
