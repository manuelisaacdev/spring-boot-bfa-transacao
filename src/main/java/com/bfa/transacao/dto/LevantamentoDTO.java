package com.bfa.transacao.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LevantamentoDTO {
	@NotNull(message = "{Levantamento.montante.notnull}")
	@Min(value = 1, message = "{Levantamento.montante.min}")
	private Double montante;
	
	@NotBlank(message = "{Levantamento.montante.notblank}")
	private String numeroConta;
}
