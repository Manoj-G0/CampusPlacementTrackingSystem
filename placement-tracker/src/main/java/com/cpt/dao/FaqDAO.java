package com.cpt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FaqDAO implements FaqDAOInt {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, String>> getFAQQuestions() {
		return jdbcTemplate.query("SELECT faq_id, faq_question FROM faqs order by faq_id", (rs, rowNum) -> {
			Map<String, String> map = new HashMap<>();
			map.put("faq_id", String.valueOf(rs.getInt("faq_id")));
			map.put("faq_question", rs.getString("faq_question"));
			return map;
		});
	}

	@Override
	public String getFAQAnswers(int faqid) {
		return jdbcTemplate.queryForObject("SELECT faq_answer FROM faqs where faq_id=?", new Object[] { faqid },
				String.class);
	}
}
