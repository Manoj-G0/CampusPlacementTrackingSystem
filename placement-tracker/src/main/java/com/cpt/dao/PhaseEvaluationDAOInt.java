package com.cpt.dao;

import java.util.List;

import com.cpt.model.PhaseEvaluation;

public interface PhaseEvaluationDAOInt {

	List<PhaseEvaluation> findByAppId(Integer appId);

}
