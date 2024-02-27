package com.bfa.transacao.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.bfa.transacao.exception.BadRequestException;
import com.bfa.transacao.model.Deposito;
import com.bfa.transacao.model.Status;

public interface DepositoService extends AbstractService<Deposito, UUID> {
	public List<Deposito> findAllByIBAN(String iban, String orderBy, Direction direction) throws BadRequestException;
	public Page<Deposito> paginationByIBAN(int page, int size, String iban, String orderBy, Direction direction) throws BadRequestException;
	public Deposito create(Deposito deposito);
	public void updateStatus(String numeroOrdem, Status status);
}
