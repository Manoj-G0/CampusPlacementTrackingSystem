package com.cpt.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.cpt.filter.RoleBasedAccessFilter;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

public class WebInit implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// Create the Spring application context
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation("com"); // Java config package

		// Register the DispatcherServlet
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
				new DispatcherServlet(context));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
		dispatcher.setMultipartConfig(new MultipartConfigElement("C:/temp/uploads", 10485760, 10485760, 0));

		FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("roleBasedAccessFilter",
				new RoleBasedAccessFilter());
		filterRegistration.addMappingForUrlPatterns(null, false, "/student/*", "/admin/*", "/hr/*", "/login",
				"/changepassword");

	}
}
