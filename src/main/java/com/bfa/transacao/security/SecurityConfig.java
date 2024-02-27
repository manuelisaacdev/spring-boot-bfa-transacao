package com.bfa.transacao.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
	
	@Bean
	AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authenticationProvider);
	}
	
	@Bean
	UserDetailsService users(PasswordEncoder encoder) {
		UserDetails admin = User.builder()
		.username("admin")
		.password(encoder.encode("12345678"))
		.roles("USER", "ADMIN")
		.build();
		return new InMemoryUserDetailsManager(admin);
	}
	
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationFilter authenticationFilter, AuthorizationFilter authorizationFilter) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(authorize -> authorize
			.requestMatchers("/error").permitAll()
			.anyRequest().authenticated()
		)
		.addFilter(authorizationFilter)
		.addFilter(authenticationFilter)
		.sessionManagement(sessionManagementConfig -> sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
	
	@Bean
	CorsConfigurationSource corsConfiguration() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE"));
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}
	
	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
}
