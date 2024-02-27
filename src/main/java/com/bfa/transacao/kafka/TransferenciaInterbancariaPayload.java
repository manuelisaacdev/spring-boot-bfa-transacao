package com.bfa.transacao.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaInterbancariaPayload {
	private Double montante;
	private String numeroOrdem;
	private String ibanContaOrigem;
	private String ibanContaDestino;
}
