package com.bfa.transacao.kafka;

import java.util.List;

import com.bfa.transacao.model.Deposito;
import com.bfa.transacao.model.Levantamento;
import com.bfa.transacao.model.Transferencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusBancario {
	private String iban;
	private List<Deposito> depositos;
	private List<Levantamento> levantamentos;
	private List<Transferencia> transferencias;
}
