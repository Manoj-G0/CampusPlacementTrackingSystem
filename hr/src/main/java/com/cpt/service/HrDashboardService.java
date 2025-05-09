package com.cpt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cpt.model.HrHrDetails;
import com.cpt.model.HrPlacementDrive;
import com.cpt.model.HrRecruitmentStat;

@Service
public class HrDashboardService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public HrHrDetails getHrDetails(String usrId) {
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

    public List<HrPlacementDrive> getOngoingDrives(int clgId, int cmpId) {
        String sql = "SELECT pd.*, c.cmp_desc FROM placement_drives pd " +
                     "JOIN companies c ON pd.pld_cmp_id = c.cmp_id " +
                     "WHERE pd.pld_clg_id = ? AND pd.pld_cmp_id = ? AND pd.pld_status = 'ASSIGNED' " +
                     "AND CURRENT_DATE BETWEEN pd.pld_start_date AND pd.pld_end_date";
        List<HrPlacementDrive> drives = jdbcTemplate.query(sql, new Object[]{clgId, cmpId}, (rs, rowNum) -> {
            HrPlacementDrive drive = new HrPlacementDrive();
            drive.setPldId(rs.getInt("pld_id"));
            drive.setPldName(rs.getString("pld_name"));
            drive.setPldRole(rs.getString("pld_role"));
            drive.setPldPackage(rs.getBigDecimal("pld_package"));
            drive.setPldStartDate(rs.getDate("pld_start_date"));
            drive.setPldEndDate(rs.getDate("pld_end_date"));
            drive.setCmpDesc(rs.getString("cmp_desc"));
            return drive;
        });

        // Fetch application counts
        for (HrPlacementDrive drive : drives) {
            String countSql = "SELECT COUNT(*) FROM applications WHERE app_pld_id = ?";
            int count = jdbcTemplate.queryForObject(countSql, new Object[]{drive.getPldId()}, Integer.class);
            drive.setApplicationCount(count); // Assuming a setter for applicationCount
        }

        return drives;
    }

    public List<HrPlacementDrive> getCompletedDrives(int clgId, int cmpId) {
        String sql = "SELECT pd.*, c.cmp_desc FROM placement_drives pd " +
                     "JOIN companies c ON pd.pld_cmp_id = c.cmp_id " +
                     "WHERE pd.pld_clg_id = ? AND pd.pld_cmp_id = ? AND pd.pld_end_date < CURRENT_DATE";
        return jdbcTemplate.query(sql, new Object[]{clgId, cmpId}, (rs, rowNum) -> {
            HrPlacementDrive drive = new HrPlacementDrive();
            drive.setPldId(rs.getInt("pld_id"));
            drive.setPldName(rs.getString("pld_name"));
            drive.setPldRole(rs.getString("pld_role"));
            drive.setPldPackage(rs.getBigDecimal("pld_package"));
            drive.setPldEndDate(rs.getDate("pld_end_date"));
            drive.setCmpDesc(rs.getString("cmp_desc"));
            return drive;
        });
    }

    public List<HrRecruitmentStat> getRecruitmentStats(int clgId) {
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