package com.github.evertonbrunosds.notes.service;

import static com.github.evertonbrunosds.notes.util.ResourceException.Type.BODY;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.github.evertonbrunosds.notes.entity.UserProfileEntity;
import com.github.evertonbrunosds.notes.repository.UserProfileRepository;
import com.github.evertonbrunosds.notes.util.ResourceException;
import com.github.evertonbrunosds.notes.validation.annotation.CurrentPage;
import com.github.evertonbrunosds.notes.validation.annotation.Email;
import com.github.evertonbrunosds.notes.validation.annotation.SizePage;
import com.github.evertonbrunosds.notes.validation.annotation.UserName;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@Service
public class UserProfileService {

    private final UserProfileRepository repository;

    public record Checker(UserProfileEntity target, ResourceException hasException) {

        public Checker(final UserProfileEntity target) {
            this(target, new ResourceException(CONFLICT, BODY));
        }

        public void check(final Optional<UserProfileEntity> result, final String fieldName) {
            result.ifPresent(found -> {
                if (!found.getId().equals(target.getId())) {
                    hasException.getContent().computeIfAbsent(fieldName, key -> {
                        return new LinkedList<>();
                    }).add("Not available, try another one");
                }
            });
        }
    }

    public UserProfileEntity save(@Valid final UserProfileEntity target) {
        final var checker = new Checker(target);
        checker.check(repository.findByEmail(target.getEmail()), "email");
        checker.check(repository.findByUserName(target.getUserName()), "user_name");
        if (!checker.hasException.getContent().isEmpty()) {
            throw checker.hasException;
        } else {
            return repository.save(target);
        }
    }

    public Page<UserProfileEntity> findByUserNameContaining(

            @UserName final String userName,

            @CurrentPage final int currentPage,

            @SizePage final int sizePage

    ) {
        final var sort = Sort.by(Sort.Direction.fromString("ASC"), "userName");
        final var pageRequest = PageRequest.of(currentPage, sizePage, sort);
        final var ormPage = repository.findByUserNameContaining(userName, pageRequest);
        if (currentPage >= ormPage.getTotalPages()) {
            final var field = "current_page";
            final var message = "Page number " + currentPage + " does not exist, request another one from the "
                    + ormPage.getTotalPages() + " available";
            throw new ResourceException(NOT_FOUND, BODY, Map.of(field, List.of(message)));
        } else {
            return ormPage;
        }
    }

    public Optional<UserProfileEntity> findById(@NotNull final UUID id) {
        return repository.findById(id);
    }

    public Optional<UserProfileEntity> findByEmail(@Email final String email) {
        return repository.findByEmail(email);
    }

    public void deleteById(@NotNull final UUID id) {
        repository.deleteById(id);
    }

}