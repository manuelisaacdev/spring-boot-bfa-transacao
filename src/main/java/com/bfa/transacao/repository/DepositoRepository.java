package com.bfa.transacao.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bfa.transacao.model.Deposito;


public interface DepositoRepository extends JpaRepository<Deposito, UUID> {
	public Optional<Deposito> findByNumeroOrdem(String numeroOrdem);
}
