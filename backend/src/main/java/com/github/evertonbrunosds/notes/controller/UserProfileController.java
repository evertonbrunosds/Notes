package com.github.evertonbrunosds.notes.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.evertonbrunosds.notes.entity.UserProfileEntity;
import com.github.evertonbrunosds.notes.service.UserProfileService;
import com.github.evertonbrunosds.notes.shared.UserProfileShared;
import com.github.evertonbrunosds.notes.util.Pagination;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Controller
@RequestMapping(value = UserProfileShared.ENDPOINT)
public class UserProfileController {

    private final UserProfileService service;

    private final ModelMapper mapper;

    private UserProfileEntity convert(final UserProfileShared shared) {
        return mapper.map(shared, UserProfileEntity.class);
    }

    private UserProfileShared convert(final UserProfileEntity entity) {
        return mapper.map(entity, UserProfileShared.class);
    }

    @PostMapping
    public ResponseEntity<UserProfileShared> create(@RequestBody final Optional<UserProfileShared> body) {
        return ResponseEntity.of(body.map(this::convert).map(service::save).map(this::convert));
    }

    @PutMapping("/id={id}")
    public ResponseEntity<UserProfileShared> update(@RequestBody final UserProfileShared shared, @PathVariable final UUID id) {
        return ResponseEntity.of(service.findById(id).map(entity -> {
            mapper.map(shared, entity);
            return service.save(entity);
        }).map(this::convert));
    }

    @GetMapping("/id={id}")
    public ResponseEntity<UserProfileShared> readById(@PathVariable final UUID id) {
        return ResponseEntity.of(service.findById(id).map(this::convert));
    }

    @GetMapping("/email={email}")
    public ResponseEntity<UserProfileShared> readByEmail(@PathVariable final String email) {
        return ResponseEntity.of(service.findByEmail(email).map(this::convert));
    }

    @GetMapping("/user_name={userName}")
    public ResponseEntity<List<UserProfileShared>> readyByUserNameContaining(

            @PathVariable final String userName,

            final HttpServletRequest request,

            final HttpServletResponse response

    ) {
        final var details = Pagination.in(request);
        final var page = service.findByUserNameContaining(userName, details.currentPage(), details.sizePage());
        Pagination.in(response).accept(page);
        return ResponseEntity.ok(page.getContent().stream().map(this::convert).collect(Collectors.toList()));
    }

    @DeleteMapping("/id={id}")
    public ResponseEntity<UserProfileShared> deleteById(@PathVariable final UUID id) {
        final var response = readById(id);
        service.deleteById(id);
        return response;
    }

}
