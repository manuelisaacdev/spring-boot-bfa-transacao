package com.bfa.transacao.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoPayload {
	private Double montante;	
	private String numeroConta;
	private String numeroOrdem;
	private TipoTransacao tipoTransacao;
}
