package com.cpt.dao;

import com.cpt.model.EmailLog;

public interface MailDAOInt {

	void saveEmailLog(EmailLog emailLog);

	void initTable();

}
