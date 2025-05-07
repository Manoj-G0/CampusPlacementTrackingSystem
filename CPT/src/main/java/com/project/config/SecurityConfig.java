package com.project.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource) {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
		manager.setUsersByUsernameQuery("SELECT usr_email, usr_password, 1 FROM users WHERE usr_email = ?");
		manager.setAuthoritiesByUsernameQuery("SELECT usr_email, usr_role FROM users WHERE usr_email = ?");
		return manager;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/tpo/**").hasRole("TPO").requestMatchers("/hr/**")
				.hasRole("HR").requestMatchers("/student/**").hasAnyRole("STUDENT", "TPO").requestMatchers("/login")
				.permitAll().anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/dashboard").failureUrl("/login?error"))
				.logout(logout -> logout.logoutSuccessUrl("/login?logout"));
		return http.build();
	}
}