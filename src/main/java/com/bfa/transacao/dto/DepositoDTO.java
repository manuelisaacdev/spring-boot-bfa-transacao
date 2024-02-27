package com.bfa.transacao.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepositoDTO {
	@NotNull(message = "{Deposito.montante.notnull}")
	@Min(value = 1, message = "{Deposito.montante.min}")
	private Double montante;
	
	@NotBlank(message = "{Deposito.numeroConta.notblank}")
	private String numeroConta;
}
