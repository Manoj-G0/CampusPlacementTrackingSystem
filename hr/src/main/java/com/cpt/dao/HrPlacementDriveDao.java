package com.cpt.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cpt.model.HrPlacementDrive;

@Repository
public class HrPlacementDriveDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<HrPlacementDrive> findOngoingDrives(int clgId, int cmpId) {
        String sql = "SELECT pd.*, c.cmp_desc FROM placement_drives pd " +
                     "JOIN companies c ON pd.pld_cmp_id = c.cmp_id " +
                     "WHERE pd.pld_clg_id = ? AND pd.pld_cmp_id = ? AND pd.pld_status = 'ASSIGNED' " +
                     "AND CURRENT_DATE BETWEEN pd.pld_start_date AND pd.pld_end_date";
        return jdbcTemplate.query(sql, new Object[]{clgId, cmpId}, (rs, rowNum) -> {
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
    }

    public List<HrPlacementDrive> findCompletedDrives(int clgId, int cmpId) {
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

    public int getApplicationCount(int pldId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE app_pld_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{pldId}, Integer.class);
    }

    public int savePlacementDrive(HrPlacementDrive drive) {
        String sql = "INSERT INTO placement_drives (pld_clg_id, pld_cmp_id, pld_name, pld_role, pld_package, pld_start_date, pld_end_date, pld_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'NOT ASSIGNED') RETURNING pld_id";
        return jdbcTemplate.queryForObject(sql, new Object[]{
                drive.getPldClgId(), drive.getPldCmpId(), drive.getPldName(), drive.getPldRole(),
                drive.getPldPackage(), drive.getPldStartDate(), drive.getPldEndDate()
        }, Integer.class);
    }

    public boolean verifyDriveAccess(int pldId, int cmpId, int clgId) {
        String sql = "SELECT pld_id FROM placement_drives WHERE pld_id = ? AND pld_cmp_id = ? AND pld_clg_id = ?";
        List<Integer> result = jdbcTemplate.queryForList(sql, new Object[]{pldId, cmpId, clgId}, Integer.class);
        return !result.isEmpty();
    }
}