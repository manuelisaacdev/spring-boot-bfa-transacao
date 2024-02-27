package com.bfa.transacao.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.bfa.transacao.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationFilter extends BasicAuthenticationFilter {

	private final String authorization;
	private final JWTService jwtService;
	
	public AuthorizationFilter(AuthenticationManager authenticationManager, @Value("${application.jwt.header-authorization}") String authorization, JWTService jwtService) {
		super(authenticationManager);
		this.jwtService = jwtService;
		this.authorization = authorization;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE");
		
		if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
			response.addHeader("Content-Type", "application/json;charset=UTF-8");
			response.setStatus(HttpStatus.NO_CONTENT.value());
			response.getWriter().flush();
			return;
		}
		
    	String authHeader = request.getHeader(authorization);
    	System.out.println("TOKEN: " + authHeader);
    	
    	if (authHeader == null) {
    		chain.doFilter(request, response);
    		return;
		}
    	try {
    		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(jwtService.extractSubjectAccessToken(authHeader), null, List.of()));
    		chain.doFilter(request, response);
		} catch (Exception e) {	
			chain.doFilter(request, response);
		}
    	
	}
}
