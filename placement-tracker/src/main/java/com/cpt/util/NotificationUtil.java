package com.cpt.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cpt.dao.NotificationDAOInt;
import com.cpt.model.Notification;

@Component
public class NotificationUtil {

	@Autowired
	NotificationDAOInt notificationDAO;

	public Long getNotificationCount(String userId) {
		return notificationDAO.countUnreadByUserId(userId);

	}

	public void save(Notification notification) {
		notificationDAO.save(notification);
	}
}
