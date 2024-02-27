package com.bfa.transacao.model.converter;

import com.bfa.transacao.model.Status;

import jakarta.persistence.AttributeConverter;

public class StatusConverter implements AttributeConverter<Status, String> {

	@Override
	public String convertToDatabaseColumn(Status status) {
		return status.getDescricao();
	}

	@Override
	public Status convertToEntityAttribute(String dbData) {
		return Status.of(dbData);
	}

}
