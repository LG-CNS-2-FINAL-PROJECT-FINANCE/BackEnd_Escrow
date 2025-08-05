package com.ddiring.BackEnd_Escrow.converter;

import com.ddiring.BackEnd_Escrow.enums.TransType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TransTypeConverter implements AttributeConverter<TransType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TransType attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public TransType convertToEntityAttribute(Integer dbData) {
        return dbData != null ? TransType.fromCode(dbData) : null;
    }
}