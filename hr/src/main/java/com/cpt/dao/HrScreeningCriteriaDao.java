package com.cpt.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cpt.model.HrScreeningCriteria;

@Repository
public class HrScreeningCriteriaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveScreeningCriteria(HrScreeningCriteria criteria) {
        String sql = "INSERT INTO screening_criteria (scr_pld_id, scr_min_gpa, scr_min_backlogs, scr_brn_id, scr_gender) " +
                     "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, criteria.getScrPldId(), criteria.getScrMinGpa(), criteria.getScrMinBacklogs(),
                criteria.getScrBrnId(), criteria.getScrGender());
    }
}