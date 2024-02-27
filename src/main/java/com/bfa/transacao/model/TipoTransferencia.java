package com.bfa.transacao.model;

import com.bfa.transacao.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public enum TipoTransferencia {
	INTERNA("Interna"), INTERBANCARIA("Interbancária");
	
	@Getter
	@JsonValue
	private final String descricao;
	
	public static TipoTransferencia of(String descricao) {
		for (TipoTransferencia tipoTransferencia : TipoTransferencia.values()) {
			if (tipoTransferencia.descricao.equalsIgnoreCase(descricao)) {
				return tipoTransferencia;
			}
		}
		throw new BadRequestException("Tipo de transferência inválida: " + descricao);
	}
}
