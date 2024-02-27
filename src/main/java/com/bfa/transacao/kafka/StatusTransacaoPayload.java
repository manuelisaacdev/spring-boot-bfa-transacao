package com.bfa.transacao.kafka;

import com.bfa.transacao.model.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusTransacaoPayload {
	private Status status;
	private String numeroOrdem;
	private TipoTransacao tipoTransacao;
}
