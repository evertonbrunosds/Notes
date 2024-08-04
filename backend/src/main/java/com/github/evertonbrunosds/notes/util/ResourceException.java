package com.github.evertonbrunosds.notes.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResourceException extends RuntimeException {

    private final @NonNull HttpStatus status;

    private final @NonNull Type type;

    private final Map<String, List<String>> content;

    public ResourceException(final HttpStatus status, final Type type) {
        this.status = status;
        this.type = type;
        this.content = new HashMap<>();
    }

    public enum Type {

        BODY, HEADER, PARAMETER, INTERNAL;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }

    }

}
