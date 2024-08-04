package com.github.evertonbrunosds.notes.util;

import static com.github.evertonbrunosds.notes.util.ResourceException.Type.HEADER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.data.domain.Page;

import com.github.evertonbrunosds.notes.constraint.annotation.CurrentPage;
import com.github.evertonbrunosds.notes.constraint.annotation.SizePage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;

public class Pagination {

    private static final String X_TOTAL_ELEMENTS = "X-Total-Elements";

    private static final String X_CURRENT_PAGE = "X-Current-Page";

    private static final String X_TOTAL_PAGES = "X-Total-Pages";

    private static final String X_SIZE_PAGE = "X-Size-Page";

    public static Consumer<Page<?>> in(final HttpServletResponse response) {
        return (page) -> {
            response.addHeader(X_TOTAL_ELEMENTS, Long.toString(page.getTotalElements()));
            response.addHeader(X_TOTAL_PAGES, Integer.toString(page.getTotalPages()));
            response.addHeader(X_CURRENT_PAGE, Integer.toString(page.getNumber()));
        };
    }

    public static PaginationDetails in(final HttpServletRequest request) {
        return PaginationDetails.builder()
                .currentPage(toInt(X_CURRENT_PAGE, request.getHeader(X_CURRENT_PAGE)))
                .sizePage(toInt(X_SIZE_PAGE, request.getHeader(X_SIZE_PAGE)))
                .build();
    }

    private static int toInt(final String parameter, final String value) {
        if (value == null) {
            throw new ResourceException(BAD_REQUEST, HEADER, Map.of(parameter, List.of("Must not be null")));
        } else {
            try {
                return Integer.parseInt(value);
            } catch (final Throwable throwable) {
                throw new ResourceException(BAD_REQUEST, HEADER, Map.of(parameter, List.of(throwable.getMessage())));
            }
        }
    }

    @Builder
    public record PaginationDetails(@CurrentPage int currentPage, @SizePage int sizePage) {
    }

}
