package com.cpt.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cpt.model.HrStudent;

@Repository
public class HrStudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<HrStudent> findShortlistedStudents(int phaseId, int pldId) {
        String sql = "SELECT s.rol_no, s.full_name, s.college_email " +
                     "FROM round_wise_shortlisted rws " +
                     "JOIN applications a ON rws.app_id = a.app_id " +
                     "JOIN students s ON a.app_usr_id = s.rol_no " +
                     "WHERE rws.phase_id = ? AND rws.pld_id = ?";
        return jdbcTemplate.query(sql, new Object[]{phaseId, pldId}, (rs, rowNum) -> {
            HrStudent student = new HrStudent();
            student.setRolNo(rs.getString("rol_no"));
            student.setFullName(rs.getString("full_name"));
            student.setCollegeEmail(rs.getString("college_email"));
            return student;
        });
    }

    public List<HrStudent> findFinalCandidates(int pldId) {
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