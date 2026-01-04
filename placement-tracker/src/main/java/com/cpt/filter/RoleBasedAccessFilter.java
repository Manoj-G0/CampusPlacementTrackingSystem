package com.cpt.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RoleBasedAccessFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization if needed
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String contextPath = httpRequest.getContextPath(); // e.g., /Application_name
		String uri = httpRequest.getRequestURI(); // e.g., /Application_name/student/dashboard
		String path = uri.substring(contextPath.length()); // e.g., /student/dashboard

		String role = (String) httpRequest.getSession().getAttribute("role");

		// Allow access to login and change password
		if (path.equals("/login") || path.equals("/changePassword") || path.equals("/getNotifications")) {
			chain.doFilter(request, response);
			return;
		}

		// If no role is found in session
		if (role == null) {
			httpResponse.sendRedirect(contextPath + "/login");
			return;
		}

		// Role-based access logic
		if (role.equals("STUD") && path.startsWith("/student/")) {
			chain.doFilter(request, response);
		} else if (role.equals("ADMN") && path.startsWith("/admin/")) {
			chain.doFilter(request, response);
		} else if (role.equals("HR") && path.startsWith("/hr/")) {
			chain.doFilter(request, response);
		} else {
			httpResponse.sendRedirect(contextPath + "/login");
		}
	}

	@Override
	public void destroy() {
		// Cleanup if needed
	}
}
