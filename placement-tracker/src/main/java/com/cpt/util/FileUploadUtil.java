package com.cpt.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cpt.model.Application;

public class FileUploadUtil {

    public static List<Application> parseApplications(MultipartFile file) throws Exception {
        List<Application> applications = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                String[] data = line.split(",");
                if (data.length < 3) continue;

                Application app = new Application();
                app.setAppUsrId(data[0].trim());
                app.setAppDate(Date.valueOf(LocalDate.parse(data[1].trim())));
                app.setAppStatus(data[2].trim());
                applications.add(app);
            }
        }
        return applications;
    }
}