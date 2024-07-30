package com.github.evertonbrunosds.notes.service;

import static com.github.evertonbrunosds.notes.util.ResourceException.Type.BODY;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.github.evertonbrunosds.notes.orm.UserProfileORM;
import com.github.evertonbrunosds.notes.repository.UserProfileRepository;
import com.github.evertonbrunosds.notes.util.ResourceException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserProfileService {

    private static void addConflict(final String field, final ResourceException hasException) {
        hasException.getContent().computeIfAbsent(field, key -> {
            return new LinkedList<>();
        }).add("Not available, try another one");
    }

    private static Optional<UserProfileORM> verifyConflict(
            final String field,
            final String value,
            final UserProfileORM ormParam,
            final Function<String, Optional<UserProfileORM>> finder,
            final ResourceException hasException) {
        return finder.apply(value).filter(ormElement -> {
            if (!ormElement.getId().equals(ormParam.getId())) {
                addConflict(field, hasException);
                return true;
            } else {
                return false;
            }
        });
    }

    private final UserProfileRepository repository;

    public UserProfileORM save(final UserProfileORM ormParam) {
        final var hasException = new ResourceException(CONFLICT, BODY);
        Stream.of(
                verifyConflict("email", ormParam.getEmail(), ormParam, repository::findByEmail, hasException),
                verifyConflict("userName", ormParam.getUserName(), ormParam, repository::findByUserName, hasException))
                .flatMap(Optional::stream).findAny();
        if (!hasException.getContent().isEmpty()) {
            throw hasException;
        } else {
            return repository.save(ormParam);
        }
    }

    public Page<UserProfileORM> findByUserNameContaining(final String userName, final int currentPage, final int sizePage) {
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

    public Optional<UserProfileORM> findById(final UUID id) {
        return repository.findById(id);
    }

    public Optional<UserProfileORM> findByEmail(final String email) {
        return repository.findByEmail(email);
    }

    public void deleteById(final UUID id) {
        repository.deleteById(id);
    }

}