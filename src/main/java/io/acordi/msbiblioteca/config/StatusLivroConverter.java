package io.acordi.msbiblioteca.config;

import io.acordi.msbiblioteca.domain.types.StatusLivro;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusLivroConverter implements AttributeConverter<StatusLivro, String> {

    @Override
    public String convertToDatabaseColumn(StatusLivro status) {
        if (status == null) {
            return "DISPONIVEL";
        }
        return status.name();
    }

    @Override
    public StatusLivro convertToEntityAttribute(String statusString) {
        if (statusString == null || statusString.isEmpty()) {
            return StatusLivro.DISPONIVEL;
        }

        // Try direct conversion first
        try {
            return StatusLivro.valueOf(statusString);
        } catch (IllegalArgumentException e) {
            // If direct conversion fails, try case-insensitive
            for (StatusLivro status : StatusLivro.values()) {
                if (status.name().equalsIgnoreCase(statusString)) {
                    return status;
                }
            }
            // Default if no match is found
            return StatusLivro.DISPONIVEL;
        }
    }
}