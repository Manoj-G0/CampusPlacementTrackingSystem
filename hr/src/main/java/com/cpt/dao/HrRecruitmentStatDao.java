package com.cpt.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cpt.model.HrRecruitmentStat;

@Repository
public class HrRecruitmentStatDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<HrRecruitmentStat> findRecruitmentStats(int clgId) {
        String sql = "SELECT EXTRACT(YEAR FROM pd.pld_end_date) AS year, COUNT(*) AS selected_count " +
                     "FROM attended_drives ad JOIN placement_drives pd ON ad.pld_id = pd.pld_id " +
                     "WHERE pd.pld_clg_id = ? AND ad.status = 'SEL' GROUP BY year";
        return jdbcTemplate.query(sql, new Object[]{clgId}, (rs, rowNum) -> {
            HrRecruitmentStat stat = new HrRecruitmentStat();
            stat.setYear(rs.getInt("year"));
            stat.setSelectedCount(rs.getInt("selected_count"));
            return stat;
        });
    }
}