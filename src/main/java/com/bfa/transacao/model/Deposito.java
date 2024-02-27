package com.bfa.transacao.model;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import com.bfa.transacao.model.converter.StatusConverter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
	name = "depositos", 
	indexes = @Index(name = "idx_depositos_numero_ordem", columnList = "numero_ordem"),
	uniqueConstraints = @UniqueConstraint(name = "uk_depositos_numero_ordem", columnNames = "numero_ordem")
)
@JsonPropertyOrder({"id","numeroOrdem","montante","status","dataTransacao","numeroConta"})
public class Deposito {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "numero_ordem")
	private String numeroOrdem;
	
	@Column(nullable = false)
	private Double montante;
	
	@Column(nullable = false)
	@Convert(converter = StatusConverter.class)
	private Status status;
	
	@CreationTimestamp(source = SourceType.DB)
	@Column(name = "data_transacao", nullable = false, updatable = false)
	private LocalDateTime dataTransacao;

	@Column(name = "numero_conta", nullable = false, updatable = false)
	private String numeroConta;
	
	@JsonGetter("montante")
	public String montante() {
		return NumberFormat.getCurrencyInstance(Locale.of("pt", "AO")).format(montante);
	}

	@JsonGetter("dataTransacao")
	private String dataTransacao() {
		return dataTransacao.toString();
	}
}
