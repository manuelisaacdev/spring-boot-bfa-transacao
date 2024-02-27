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
import com.bfa.transacao.model.Levantamento;
import com.bfa.transacao.model.Status;
import com.bfa.transacao.repository.LevantamentoRepository;
import com.bfa.transacao.service.LevantamentoService;
import com.bfa.transacao.util.PasswordGeneratorManager;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LevantamentoServiceImpl extends AbstractServiceImpl<Levantamento, UUID, LevantamentoRepository> implements LevantamentoService  {
	private final String bankCode;
	private final String prefixIBan;
	private final Integer sizeOrderNumber;
	private final KafkaProducerService kafkaProducerService;
	private final PasswordGeneratorManager passwordGeneratorManager;

	public LevantamentoServiceImpl(LevantamentoRepository repository, HttpServletRequest request, MessageSource messageSource,
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
	public List<Levantamento> findAllByIBAN(String iban, String orderBy, Direction direction) throws BadRequestException {
		this.validateIBAN(iban);
		return super.findAll(Example.of(Levantamento.builder().numeroConta(iban.substring(prefixIBan.length())).build(), ExampleMatcher.matching()
		.withMatcher("numeroConta", matcher -> matcher.contains().ignoreCase())), orderBy, direction);
	}
	
	@Override
	public Page<Levantamento> paginationByIBAN(int page, int size, String iban, String orderBy, Direction direction) throws BadRequestException {
		this.validateIBAN(iban);
		return super.pagination(page, size, Example.of(Levantamento.builder().numeroConta(iban.substring(prefixIBan.length())).build(),ExampleMatcher.matching()
		.withMatcher("numeroConta", matcher -> matcher.contains().ignoreCase())), orderBy, direction);
	}

	@Override
	public Levantamento create(Levantamento levantamento) {
		levantamento.setStatus(Status.PROCESSO);
		levantamento.setNumeroOrdem(bankCode + passwordGeneratorManager.generateOnlyDigits(sizeOrderNumber, 1));
		Levantamento entity = super.save(levantamento);
		kafkaProducerService.send(TransacaoPayload.builder()
		.montante(levantamento.getMontante())
		.tipoTransacao(TipoTransacao.LEVANTAMENTO)
		.numeroOrdem(levantamento.getNumeroOrdem())
		.numeroConta(levantamento.getNumeroConta())
		.build());
		return entity;
	}

	@Override
	public void updateStatus(String numeroOrdem, Status status) {
		Optional<Levantamento> levantamento = super.repository.findByNumeroOrdem(numeroOrdem);
		if (levantamento.isPresent()) {
			levantamento.get().setStatus(status);
			super.save(levantamento.get());
		}
	}
	
	private void validateIBAN(String iban) throws BadRequestException {
		if (!iban.startsWith(prefixIBan)) {
			throw new BadRequestException(super.messageSource.getMessage("iban.invalido", new String[]{iban}, super.request.getLocale()));
		}
	}
}
