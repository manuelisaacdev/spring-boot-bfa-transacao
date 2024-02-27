package com.bfa.transacao.kafka;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bfa.transacao.model.Status;
import com.bfa.transacao.model.TipoTransferencia;
import com.bfa.transacao.model.Transferencia;
import com.bfa.transacao.service.DepositoService;
import com.bfa.transacao.service.LevantamentoService;
import com.bfa.transacao.service.TransferenciaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements KafkaConsumerService {
	private final DepositoService depositoService;
	private final LevantamentoService levantamentoService;
	private final KafkaProducerService kafkaProducerService;
	private final TransferenciaService transferenciaService;

	@Override
	@KafkaListener(topics = KafkaTopicConfig.TOPIC_BFA_TRANSACAO_STATUS_TRANSACTION, groupId = "group-bank")
	public void receiveStatusTransaction(String payload) {
		log.info("========>  BFA TRANSACOES receiveStatusTransaction: " + payload);
		try {
			StatusTransacaoPayload statusTransacaoPayload = new ObjectMapper().readValue(payload, StatusTransacaoPayload.class);
			if (statusTransacaoPayload.getTipoTransacao().equals(TipoTransacao.DEPOSITO)) {
				depositoService.updateStatus(statusTransacaoPayload.getNumeroOrdem(), statusTransacaoPayload.getStatus());
			} else if (statusTransacaoPayload.getTipoTransacao().equals(TipoTransacao.LEVANTAMENTO)) {
				levantamentoService.updateStatus(statusTransacaoPayload.getNumeroOrdem(), statusTransacaoPayload.getStatus());	
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@KafkaListener(topics = KafkaTopicConfig.TOPIC_BFA_TRANSACAO_STATUS_TRANSFER, groupId = "group-bank")
	public void receiveStatusTransfer(String payload) {
		log.info("========>  BFA TRANSACOES receiveStatusTransferTransaction: " + payload);
		try {			
			StatusTransferenciaPayload statusTransferenciaPayload = new ObjectMapper().readValue(payload, StatusTransferenciaPayload.class);
			transferenciaService.updateStatus(statusTransferenciaPayload.getNumeroOrdem(), statusTransferenciaPayload.getStatus());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@KafkaListener(topics = KafkaTopicConfig.TOPIC_BFA_TRANSACAO_HISTORY, groupId = "group-bank")
	public void receiveHistory(String payload) {
		log.info("========>  BFA TRANSACOES receiveHistory: " + payload);
		try {
			String orderBy = "dataTransacao";
			SolicitacaoHistoricoBancario solicitacaoHistoricoBancario = new ObjectMapper().readValue(payload, SolicitacaoHistoricoBancario.class);
			kafkaProducerService.send(RespostaHistoricoBancario.builder()
			.id(solicitacaoHistoricoBancario.getId())
			.iban(solicitacaoHistoricoBancario.getIban())
			.depositos(depositoService.findAllByIBAN(solicitacaoHistoricoBancario.getIban(), orderBy, Direction.DESC))
			.levantamentos(levantamentoService.findAllByIBAN(solicitacaoHistoricoBancario.getIban(), orderBy, Direction.DESC))
			.transferencias(transferenciaService.findAllByIBAN(solicitacaoHistoricoBancario.getIban(), orderBy, Direction.DESC))
			.build());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@KafkaListener(topics = KafkaTopicConfig.TOPIC_BFA_TRANSACAO_INTERBANK_TRANSFER, groupId = "group-bank")
	public void receiveinterbankTransfer(String payload) {
		log.info("========>  BFA TRANSACOES receiveinterbankTransfer: " + payload);
		try {
			TransferenciaInterbancariaPayload transferenciaInterbancariaPayload = new ObjectMapper().readValue(payload, TransferenciaInterbancariaPayload.class);
			transferenciaService.save(Transferencia.builder().
			status(Status.PROCESSO)
			.tipoTransferencia(TipoTransferencia.INTERBANCARIA)
			.montante(transferenciaInterbancariaPayload.getMontante())
			.numeroOrdem(transferenciaInterbancariaPayload.getNumeroOrdem())
			.ibanContaOrigem(transferenciaInterbancariaPayload.getIbanContaOrigem())
			.ibanContaDestino(transferenciaInterbancariaPayload.getIbanContaDestino())
			.build());
			kafkaProducerService.send(transferenciaInterbancariaPayload);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
