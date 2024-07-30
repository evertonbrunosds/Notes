package com.github.evertonbrunosds.notes.component;

import org.springframework.stereotype.Component;

import com.github.evertonbrunosds.notes.configuration.AESProcessorConfiguration.AESProcessor;
import com.github.evertonbrunosds.notes.dto.UserProfileDTO;
import com.github.evertonbrunosds.notes.orm.UserProfileORM;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ConverterComponent {

    private final AESProcessor aesProcessor;

    public UserProfileDTO toDTO(final UserProfileORM orm) {
        final var dto = new UserProfileDTO();
        dto.setId(orm.getId());
        dto.setUserName(orm.getUserName());
        dto.setEmail(aesProcessor.decode(orm.getEmail()));
        dto.setDisplayName(orm.getDisplayName());
        dto.setDescription(orm.getDescription());
        dto.setBirthday(orm.getBirthday());
        dto.setPassword(orm.getPassword());
        dto.setCreatedAt(orm.getCreatedAt());
        return dto;
    }

    public UserProfileORM toORM(final UserProfileDTO dto) {
        final var orm = new UserProfileORM();
        orm.setUserName(dto.getUserName());
        orm.setEmail(aesProcessor.encode(dto.getEmail()));
        orm.setDisplayName(dto.getDisplayName());
        orm.setDescription(dto.getDescription());
        orm.setBirthday(dto.getBirthday());
        orm.setPassword(dto.getPassword());
        return orm;
    }

}
