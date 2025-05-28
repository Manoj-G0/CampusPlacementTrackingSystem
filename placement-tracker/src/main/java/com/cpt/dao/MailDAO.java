package com.cpt.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cpt.model.EmailLog;

@Repository
public class MailDAO implements MailDAOInt {

	private final JdbcTemplate jdbcTemplate;

	public MailDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void saveEmailLog(EmailLog emailLog) {
		String sql = "INSERT INTO email_log (sender, recipient, subject, status, sent_at) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, emailLog.getSender(), emailLog.getRecipient(), emailLog.getSubject(),
				emailLog.getStatus(), emailLog.getSentAt());
		System.out.println("insert");
	}

	@Override
	public void initTable() {
		String sql = "CREATE TABLE IF NOT EXISTS email_log (" + "id SERIAL PRIMARY KEY, " + "sender VARCHAR(255), "
				+ "recipient VARCHAR(255), " + "subject VARCHAR(255), " + "status VARCHAR(50), " + "sent_at TIMESTAMP)";
		jdbcTemplate.execute(sql);
	}
}