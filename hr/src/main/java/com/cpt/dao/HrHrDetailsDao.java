package com.cpt.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cpt.model.HrHrDetails;

@Repository
public class HrHrDetailsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public HrHrDetails findHrDetails(String usrId) {
        String sql = "SELECT hr_name, designation, cmp_id, clg_id FROM hr WHERE hr_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{usrId}, (rs, rowNum) -> {
            HrHrDetails hr = new HrHrDetails();
            hr.setHrName(rs.getString("hr_name"));
            hr.setDesignation(rs.getString("designation"));
            hr.setCmpId(rs.getInt("cmp_id"));
            hr.setClgId(rs.getInt("clg_id"));
            return hr;
        });
    }
}