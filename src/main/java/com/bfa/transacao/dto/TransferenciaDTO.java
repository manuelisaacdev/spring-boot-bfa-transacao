package com.bfa.transacao.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransferenciaDTO {
	@NotNull(message = "{Transferencia.montante.notnull}")
	@Min(value = 1, message = "{Transferencia.montante.min}")
	private Double montante;
	
	@NotBlank(message = "{Transferencia.ibanContaOrigem.notblank}")
	private String ibanContaOrigem;
	
	@NotBlank(message = "{Transferencia.ibanContaDestino.notblank}")
	private String ibanContaDestino;
}
