package com.bfa.transacao.kafka;

public interface KafkaConsumerService {
	public void receiveStatusTransaction(String payload);
	public void receiveStatusTransfer(String payload);
	public void receiveHistory(String payload);
	public void receiveinterbankTransfer(String payload);
}
