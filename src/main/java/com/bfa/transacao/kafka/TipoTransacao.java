package com.bfa.transacao.kafka;

import com.bfa.transacao.exception.BadRequestException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public enum TipoTransacao {
	DEPOSITO("Depósito"), 
	LEVANTAMENTO("Levantamento");
	
	@Getter
	private final String descricao;
	
	public static TipoTransacao of(String descricao) {
		for (TipoTransacao status : TipoTransacao.values()) {
			if (status.descricao.equalsIgnoreCase(descricao)) {
				return status;
			}
		}
		throw new BadRequestException("Tipo transação inválida: " + descricao);
	}
}
