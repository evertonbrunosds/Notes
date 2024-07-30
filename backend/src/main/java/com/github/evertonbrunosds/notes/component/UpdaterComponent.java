package com.github.evertonbrunosds.notes.component;


import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.github.evertonbrunosds.notes.configuration.AESProcessorConfiguration.AESProcessor;
import com.github.evertonbrunosds.notes.dto.UserProfileDTO;
import com.github.evertonbrunosds.notes.orm.UserProfileORM;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UpdaterComponent {

    private final AESProcessor aesProcessor;

    public Consumer<UserProfileDTO> inORM(final UserProfileORM orm) {
        return (dto) -> {
            orm.setUserName(dto.getUserName());
            orm.setEmail(aesProcessor.encode(dto.getEmail()));
            orm.setDisplayName(dto.getDisplayName());
            orm.setDescription(dto.getDescription());
            orm.setBirthday(dto.getBirthday());
            orm.setPassword(dto.getPassword());
        };

    }

}
