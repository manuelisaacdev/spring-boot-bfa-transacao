package com.bfa.transacao.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseController {
	
	public <T> ResponseEntity<T> ok(T body) {
		return ResponseEntity.ok(body);
	}
	
	public <T> ResponseEntity<T> created(T body) {
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}
	
	public ResponseEntity<Void> noContent() {
		return ResponseEntity.noContent().build();
	}
}
