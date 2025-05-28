package com.cpt.dao;

import java.util.List;

import com.cpt.model.Notification;

public interface NotificationDAOInt {

	long countUnreadByUserId(String usrId);

	void update(Notification notification);

	Notification findById(Long ntfId);

	List<Notification> findByUserId(String usrId);

	void save(Notification notification);

}
