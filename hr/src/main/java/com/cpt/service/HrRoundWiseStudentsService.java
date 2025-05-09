package com.cpt.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cpt.model.HrHiringPhase;
import com.cpt.model.HrStudent;

@Service
public class HrRoundWiseStudentsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean verifyDriveAccess(int pldId, int cmpId, int clgId) {
        String sql = "SELECT pld_id FROM placement_drives WHERE pld_id = ? AND pld_cmp_id = ? AND pld_clg_id = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, pldId, cmpId, clgId);
        return !result.isEmpty();
    }

    public List<HrHiringPhase> getHiringPhases(int pldId) {
        String sql = "SELECT hph_id, hph_name, hph_sequence FROM hiring_phases WHERE hph_pld_id = ? ORDER BY hph_sequence";
        List<HrHiringPhase> phases = jdbcTemplate.query(sql, new Object[]{pldId}, (rs, rowNum) -> {
            HrHiringPhase phase = new HrHiringPhase();
            phase.setHphId(rs.getInt("hph_id"));
            phase.setHphName(rs.getString("hph_name"));
            phase.setHphSequence(rs.getInt("hph_sequence"));
            return phase;
        });

        // Fetch shortlisted students per phase
        for (HrHiringPhase phase : phases) {
            String studentSql = "SELECT s.rol_no, s.full_name, s.college_email " +
                               "FROM round_wise_shortlisted rws " +
                               "JOIN applications a ON rws.app_id = a.app_id " +
                               "JOIN students s ON a.app_usr_id = s.rol_no " +
                               "WHERE rws.phase_id = ? AND rws.pld_id = ?";
            List<HrStudent> students = jdbcTemplate.query(studentSql, new Object[]{phase.getHphId(), pldId}, (rs, rowNum) -> {
                HrStudent student = new HrStudent();
                student.setRolNo(rs.getString("rol_no"));
                student.setFullName(rs.getString("full_name"));
                student.setCollegeEmail(rs.getString("college_email"));
                return student;
            });
            phase.setStudents(students); // Assuming a setter for students
        }

        return phases;
    }
}