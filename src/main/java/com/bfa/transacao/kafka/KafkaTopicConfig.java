package com.bfa.transacao.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
	// Tópico que o banco receberá as transações internas
	public static final String TOPIC_BFA_TRANSFER = "topic-bfa-transfer";
	public static final String TOPIC_BFA_TRANSACTION = "topic-bfa-transaction";
	
	public static final String TOPIC_INTERMEDIARIO_HISTORY = "topic-intermediario-history";
	
	// Tópico que o seviço de transações receberá os status das transações
	public static final String TOPIC_BFA_TRANSACAO_HISTORY = "topic-bfa-transacao-history";
	public static final String TOPIC_BFA_TRANSACAO_STATUS_TRANSFER = "topic-bfa-transacao-status-transfer";
	public static final String TOPIC_BFA_TRANSACAO_STATUS_TRANSACTION = "topic-bfa-transacao-status-transaction";
	
	public static final String TOPIC_BFA_INTERBANK_TRANSFER = "topic-bfa-interbank-transfer";
	public static final String TOPIC_BFA_TRANSACAO_INTERBANK_TRANSFER = "topic-bfa-transacao-interbank-transfer";

	@Bean
	NewTopic topicReceiveStatusTransfer() {
		return TopicBuilder
		.name(TOPIC_BFA_TRANSACAO_STATUS_TRANSFER)
		.build();
	}

	@Bean
	NewTopic topicStatusTransaction() {
		return TopicBuilder
		.name(TOPIC_BFA_TRANSACAO_STATUS_TRANSACTION)
		.build();
	}

	@Bean
	NewTopic topicHistory() {
		return TopicBuilder
		.name(TOPIC_BFA_TRANSACAO_HISTORY)
		.build();
	}

	@Bean
	NewTopic topicInterbankTransfer() {
		return TopicBuilder
		.name(TOPIC_BFA_TRANSACAO_INTERBANK_TRANSFER)
		.build();
	}
}
