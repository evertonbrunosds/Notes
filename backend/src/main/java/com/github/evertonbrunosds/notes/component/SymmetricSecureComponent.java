package com.github.evertonbrunosds.notes.component;

import org.springframework.stereotype.Component;

import com.github.evertonbrunosds.notes.model.SymmetricSecureModel;

import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SymmetricSecureComponent implements AttributeConverter<String, String> {

    private final SymmetricSecureModel symmetricSecure;

    @Override
    public String convertToDatabaseColumn(String target) {
        return symmetricSecure.encode(target);
    }

    @Override
    public String convertToEntityAttribute(String target) {
        return symmetricSecure.decode(target);
    }
    
}
