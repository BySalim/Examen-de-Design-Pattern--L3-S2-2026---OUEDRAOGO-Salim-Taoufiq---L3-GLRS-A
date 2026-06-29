package com.payment.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.YearMonth;

@Converter(autoApply = true)
public class YearMonthAttributeConverter implements AttributeConverter<YearMonth, String> {

    @Override
    public String convertToDatabaseColumn(YearMonth periode) {
        return periode == null ? null : periode.toString();
    }

    @Override
    public YearMonth convertToEntityAttribute(String valeur) {
        return valeur == null ? null : YearMonth.parse(valeur);
    }
}
