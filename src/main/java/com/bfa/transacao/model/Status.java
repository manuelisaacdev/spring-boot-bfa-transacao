package com.bfa.transacao.model;

import com.bfa.transacao.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public enum Status {
	NEGADO("Negado"), PROCESSO("Processo"), CONCLUIDO("Conluido");
	
	@Getter
	@JsonValue
	private final String descricao;
	
	public static Status of(String descricao) {
		for (Status status : Status.values()) {
			if (status.descricao.equalsIgnoreCase(descricao)) {
				return status;
			}
		}
		throw new BadRequestException("Status inválido: " + descricao);
	}
	
}
