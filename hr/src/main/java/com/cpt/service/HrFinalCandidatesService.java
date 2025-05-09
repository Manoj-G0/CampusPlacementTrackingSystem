package com.cpt.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cpt.model.HrStudent;

@Service
public class HrFinalCandidatesService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean verifyDriveAccess(int pldId, int cmpId, int clgId) {
        String sql = "SELECT pld_id FROM placement_drives WHERE pld_id = ? AND pld_cmp_id = ? AND pld_clg_id = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, pldId, cmpId, clgId);
        return !result.isEmpty();
    }

    public List<HrStudent> getFinalCandidates(int pldId) {
        String sql = "SELECT s.rol_no, s.full_name, s.college_email, s.contact_number " +
                     "FROM attended_drives ad " +
                     "JOIN students s ON ad.usr_id = s.rol_no " +
                     "WHERE ad.pld_id = ? AND ad.status = 'SEL'";
        return jdbcTemplate.query(sql, new Object[]{pldId}, (rs, rowNum) -> {
            HrStudent student = new HrStudent();
            student.setRolNo(rs.getString("rol_no"));
            student.setFullName(rs.getString("full_name"));
            student.setCollegeEmail(rs.getString("college_email"));
            student.setContactNumber(rs.getString("contact_number"));
            return student;
        });
    }
}