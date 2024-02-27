package com.bfa.transacao.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bfa.transacao.model.Levantamento;

public interface LevantamentoRepository extends JpaRepository<Levantamento, UUID> {
	public Optional<Levantamento> findByNumeroOrdem(String numeroOrdem);
}
