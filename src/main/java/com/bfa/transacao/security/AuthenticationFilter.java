package com.bfa.transacao.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.bfa.transacao.exception.JWTAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter implements AuthenticationFailureHandler {
	private final AuthenticationManager authenticationManager;
	
	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		this.authenticationManager = authenticationManager;
		this.setAuthenticationFailureHandler(this);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> map= new ObjectMapper().readValue(request.getInputStream(), Map.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(map.get("user"), map.get("password"), new ArrayList<>()));
		} catch (Exception e) {
			throw new JWTAuthenticationException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		request.setAttribute("authentication", authentication);
		request.getRequestDispatcher("/authentication").forward(request, response);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE");
		final HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
		response.setStatus(unauthorized.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of(
			"timestamp", LocalDateTime.now().toString(),
			"status", unauthorized.value(),
			"message", exception.getMessage(),
			"path", request.getRequestURI()
		)));
		response.getWriter().flush();
	}
}
