package com.github.evertonbrunosds.notes.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.evertonbrunosds.notes.component.ConverterComponent;
import com.github.evertonbrunosds.notes.component.UpdaterComponent;
import com.github.evertonbrunosds.notes.configuration.AESProcessorConfiguration.AESProcessor;
import com.github.evertonbrunosds.notes.constraint.annotation.Email;
import com.github.evertonbrunosds.notes.constraint.annotation.UserName;
import com.github.evertonbrunosds.notes.dto.UserProfileDTO;
import com.github.evertonbrunosds.notes.service.UserProfileService;
import com.github.evertonbrunosds.notes.util.Pagination;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Controller
@Validated
@RequestMapping(value = UserProfileDTO.ENDPOINT)
public class UserProfileController {

    private final ConverterComponent converter;

    private final UserProfileService service;

    private final UpdaterComponent updater;

    private final AESProcessor aes;

    @PostMapping
    public ResponseEntity<UserProfileDTO> create(@Valid @RequestBody final UserProfileDTO dto) {
        return ResponseEntity.ok(converter.toDTO(service.save(converter.toORM(dto))));
    }

    @PutMapping("/id={id}")
    public ResponseEntity<UserProfileDTO> update(@Valid @RequestBody final UserProfileDTO dto, @PathVariable final UUID id) {
        final var optional = service.findById(id);
        optional.ifPresent(orm -> {
            updater.inORM(orm).accept(dto);
            service.save(orm);
        });
        return ResponseEntity.of(optional
                .stream()
                .map(converter::toDTO)
                .findAny());
    }

    @GetMapping("/id={id}")
    public ResponseEntity<UserProfileDTO> readById(@PathVariable final UUID id) {
        return ResponseEntity.of(service
                .findById(id)
                .stream()
                .map(converter::toDTO)
                .findAny());
    }

    @GetMapping("/email={email}")
    public ResponseEntity<UserProfileDTO> readByEmail(@PathVariable @Email final String email) {
        return ResponseEntity.of(service
                .findByEmail(aes.encode(email))
                .stream()
                .map(converter::toDTO)
                .findAny());
    }

    @GetMapping("/user_name={userName}")
    public ResponseEntity<List<UserProfileDTO>> readyByUserNameContaining(@PathVariable @UserName final String userName,
            final HttpServletRequest request, final HttpServletResponse response) {
        final var details = Pagination.in(request);
        final var ormPage = service.findByUserNameContaining(userName, details.currentPage(), details.sizePage());
        Pagination.in(response).accept(ormPage);
        return ResponseEntity.ok(ormPage.getContent().stream().map(converter::toDTO).collect(Collectors.toList()));
    }

    @DeleteMapping("/id={id}")
    public ResponseEntity<UserProfileDTO> deleteById(@PathVariable final UUID id) {
        final var responseEntity = readById(id);
        service.deleteById(id);
        return responseEntity;
    }

}
