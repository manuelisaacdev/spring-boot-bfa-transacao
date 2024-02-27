package com.bfa.transacao.kafka;

public interface KafkaProducerService {
	public void send(TransacaoPayload payload);
	public void send(TransferenciaPayload payload);
	public void send(RespostaHistoricoBancario payload);
	public void send(TransferenciaInterbancariaPayload payload);
}
