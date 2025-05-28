package com.cpt.dao;

import java.util.List;
import java.util.Map;

public interface FaqDAOInt {

	List<Map<String, String>> getFAQQuestions();

	String getFAQAnswers(int faqid);

}
