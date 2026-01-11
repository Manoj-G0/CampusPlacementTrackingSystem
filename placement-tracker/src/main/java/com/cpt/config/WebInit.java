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

	private static final int MAX_UPLOAD_SIZE = 10 * 1024 * 1024; // 10 MB

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		// 1️⃣ Create Spring context
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

		// ✅ Correct way: scan base package
		context.setServletContext(servletContext);   // ✅ IMPORTANT
        context.scan("com.cpt");
        context.register(AppConfig.class);

		// 2️⃣ Register DispatcherServlet
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
				new DispatcherServlet(context));

		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");

		// ✅ Linux / Docker safe upload directory
		dispatcher.setMultipartConfig(new MultipartConfigElement("/tmp/uploads", MAX_UPLOAD_SIZE, MAX_UPLOAD_SIZE, 0));

		// 3️⃣ Register Role-based filter (secured paths only)
		FilterRegistration.Dynamic roleFilter = servletContext.addFilter("roleBasedAccessFilter",
				new RoleBasedAccessFilter());

		roleFilter.addMappingForUrlPatterns(null, false, "/student/*", "/admin/*", "/hr/*");
	}
}
