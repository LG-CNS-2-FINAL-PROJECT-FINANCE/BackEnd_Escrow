package com.ddiring.BackEnd_Escrow.converter;

import com.ddiring.BackEnd_Escrow.enums.EscrowStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class EscrowStatusConverter implements AttributeConverter<EscrowStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EscrowStatus attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public EscrowStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? EscrowStatus.fromCode(dbData) : null;
    }
}