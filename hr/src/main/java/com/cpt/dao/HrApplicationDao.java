package com.cpt.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cpt.model.HrApplication;
import com.cpt.model.HrStudent;

@Repository
public class HrApplicationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<HrApplication> findApplicationsByDrive(int pldId) {
        String sql = "SELECT a.app_id, a.app_date, a.app_status, s.rol_no, s.full_name, s.college_email " +
                     "FROM applications a JOIN students s ON a.app_usr_id = s.rol_no WHERE a.app_pld_id = ?";
        return jdbcTemplate.query(sql, new Object[]{pldId}, (rs, rowNum) -> {
            HrApplication app = new HrApplication();
            app.setAppId(rs.getInt("app_id"));
            app.setAppDate(rs.getDate("app_date"));
            app.setAppStatus(rs.getString("app_status"));
            HrStudent student = new HrStudent();
            student.setRolNo(rs.getString("rol_no"));
            student.setFullName(rs.getString("full_name"));
            student.setCollegeEmail(rs.getString("college_email"));
            app.setStudent(student); // Assuming a setter for student
            return app;
        });
    }
}