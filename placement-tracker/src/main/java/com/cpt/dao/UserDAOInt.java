package com.cpt.dao;

import java.util.List;
import java.util.Map;

import com.cpt.model.User;

public interface UserDAOInt {

	User findById(String userId);

	String getRole(String userId, String passwd);

	boolean changePassword(String usrId, String currPasswd, String newPasswd);

	List<Map<String, Object>> getNotifications(String usr_id);

}
