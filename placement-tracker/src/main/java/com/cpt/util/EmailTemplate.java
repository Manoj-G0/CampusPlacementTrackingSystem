package com.cpt.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplate {

	private String loadTemplate(String templateName) throws IOException {
		return Files.readString(Paths.get(
				"D:/Team_6_Project/placement-tracker/placement-tracker/src/main/resources/templates" + templateName));
	}

	public String getStudentDriveTemplate(Map<String, String> content) throws IOException {
		String template = loadTemplate("student_drive.html");
		for (String key : content.keySet()) {
			template = template.replace("{" + key + "}", content.get(key));
		}
		return template;
	}

	public String getHrJobTemplate(Map<String, String> content) throws IOException {
		String template = loadTemplate("hr_job.html");
		for (String key : content.keySet()) {
			template = template.replace("{" + key + "}", content.get(key));
		}
		return template;
	}

	public String getAdminSelectionTemplate(Map<String, String> content) throws IOException {
		String template = loadTemplate("admin_selection.html");
		for (String key : content.keySet()) {
			template = template.replace("{" + key + "}", content.get(key));
		}
		return template;
	}
}