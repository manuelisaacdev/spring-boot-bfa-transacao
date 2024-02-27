package com.bfa.transacao.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.bfa.transacao.exception.BadRequestException;
import com.bfa.transacao.kafka.KafkaProducerService;
import com.bfa.transacao.kafka.TipoTransacao;
import com.bfa.transacao.kafka.TransacaoPayload;
import com.bfa.transacao.model.Deposito;
import com.bfa.transacao.model.Status;
import com.bfa.transacao.repository.DepositoRepository;
import com.bfa.transacao.service.DepositoService;
import com.bfa.transacao.util.PasswordGeneratorManager;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class DepositoServiceImpl extends AbstractServiceImpl<Deposito, UUID, DepositoRepository> implements DepositoService  {
	private final String bankCode;
	private final String prefixIBan;
	private final Integer sizeOrderNumber;
	private final KafkaProducerService kafkaProducerService;
	private final PasswordGeneratorManager passwordGeneratorManager;

	public DepositoServiceImpl(DepositoRepository repository, HttpServletRequest request, MessageSource messageSource,
			@Value("${application.bfa.code}") String bankCode, @Value("${application.bfa.prefix-iban}") String prefixIBan, 
			@Value("${application.bfa.size-account-number}") Integer sizeOrderNumber, 
			KafkaProducerService kafkaProducerService, PasswordGeneratorManager passwordGeneratorManager) {
		super(repository, request, messageSource);
		this.bankCode = bankCode;
		this.prefixIBan = prefixIBan;
		this.sizeOrderNumber = sizeOrderNumber;
		this.kafkaProducerService = kafkaProducerService;
		this.passwordGeneratorManager = passwordGeneratorManager;
	}
	
	@Override
	public List<Deposito> findAllByIBAN(String iban, String orderBy, Direction direction) throws BadRequestException {
		this.validateIBAN(iban);
		return super.findAll(Example.of(Deposito.builder().numeroConta(iban.substring(prefixIBan.length())).build(), ExampleMatcher.matching()
		.withMatcher("numeroConta", matcher -> matcher.contains().ignoreCase())), orderBy, direction);
	}
	
	@Override
	public Page<Deposito> paginationByIBAN(int page, int size, String iban, String orderBy, Direction direction) throws BadRequestException {
		this.validateIBAN(iban);
		return super.pagination(page, size, Example.of(Deposito.builder().numeroConta(iban.substring(prefixIBan.length())).build(),ExampleMatcher.matching()
		.withMatcher("numeroConta", matcher -> matcher.contains().ignoreCase())), orderBy, direction);
	}

	@Override
	public Deposito create(Deposito deposito) {
		deposito.setStatus(Status.PROCESSO);
		deposito.setNumeroOrdem(bankCode + passwordGeneratorManager.generateOnlyDigits(sizeOrderNumber, 1));
		Deposito entity = super.save(deposito);
		kafkaProducerService.send(TransacaoPayload.builder()
		.montante(deposito.getMontante())
		.tipoTransacao(TipoTransacao.DEPOSITO)
		.numeroOrdem(deposito.getNumeroOrdem())
		.numeroConta(deposito.getNumeroConta())
		.build());
		return entity;
	}

	@Override
	public void updateStatus(String numeroOrdem, Status status) {
		Optional<Deposito> deposito = super.repository.findByNumeroOrdem(numeroOrdem);
		System.out.println("======================> DEPOSITO: " + deposito.orElse(null));
		if (deposito.isPresent()) {
			deposito.get().setStatus(status);
			super.save(deposito.get());
		}
	}
	
	private void validateIBAN(String iban) throws BadRequestException {
		if (!iban.startsWith(prefixIBan)) {
			throw new BadRequestException(super.messageSource.getMessage("iban.invalido", new String[]{iban}, super.request.getLocale()));
		}
	}
}
