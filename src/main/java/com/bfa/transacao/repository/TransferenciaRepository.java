package com.bfa.transacao.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bfa.transacao.model.Transferencia;

public interface TransferenciaRepository extends JpaRepository<Transferencia, UUID> {
	public Optional<Transferencia> findByNumeroOrdem(String numeroOrdem);
	public List<Transferencia> findAllByIbanContaOrigemOrIbanContaDestino(String ibanContaOrigem, String ibanContaDestino);
	public Page<Transferencia> findAllByIbanContaOrigemOrIbanContaDestino(String ibanContaOrigem, String ibanContaDestino, Pageable pageable);
}
