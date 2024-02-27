package com.bfa.transacao.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bfa.transacao.exception.JWTServiceException;
import com.bfa.transacao.service.JWTService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JWTServiceImpl implements JWTService {
	private final String type;
	private final String secretAccessToken;
	private final HttpServletRequest request;
	private final MessageSource messageSource;
	
	public JWTServiceImpl(@Value("${application.jwt.type}") String type, 
			@Value("${application.jwt.access-token.secret}") String secretAccessToken,
			HttpServletRequest request, MessageSource messageSource) {
		super();
		this.type = type;
		this.secretAccessToken = secretAccessToken;
		this.request = request;
		this.messageSource = messageSource;
	}


	@Override
	public UUID extractSubjectAccessToken(String authorization) {
		try {
			return UUID.fromString(JWT.require(Algorithm.HMAC512(secretAccessToken)).build().verify(this.extractToken(authorization)).getSubject());
		} catch (JWTCreationException | JWTServiceException | JWTVerificationException | IllegalArgumentException e) {
			throw new JWTServiceException(e);
		}
	}

	@Override
	public String extractToken(String authorization) throws JWTServiceException {
		if (!authorization.startsWith(type)) throw new JWTServiceException(messageSource.getMessage("jwt.invalid.authorization", null, request.getLocale()));
		return authorization.substring(type.length());
	}
}
