package com.bfa.transacao.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {
	private final KafkaTemplate<String, String> kafkaTemplate;

	@Override
	public void send(TransacaoPayload payload) {
		log.info("==================> BFA TRANSACOES sendTransation: " + payload);
		try {
			kafkaTemplate.send(KafkaTopicConfig.TOPIC_BFA_TRANSACTION, new ObjectMapper().writeValueAsString(payload));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void send(TransferenciaPayload payload) {
		log.info("==================> BFA TRANSACOES sendTransation: " + payload);
		try {
			kafkaTemplate.send(KafkaTopicConfig.TOPIC_BFA_TRANSFER, new ObjectMapper().writeValueAsString(payload));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void send(RespostaHistoricoBancario payload) {
		log.info("==================> BFA TRANSACOES sendHistory: " + payload);
		try {
			kafkaTemplate.send(KafkaTopicConfig.TOPIC_INTERMEDIARIO_HISTORY, new ObjectMapper().writeValueAsString(payload));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void send(TransferenciaInterbancariaPayload payload) {
		log.info("==================> BFA TRANSACOES TransferenciaInterbancariaPayload: " + payload);
		try {
			kafkaTemplate.send(KafkaTopicConfig.TOPIC_BFA_INTERBANK_TRANSFER, new ObjectMapper().writeValueAsString(payload));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
