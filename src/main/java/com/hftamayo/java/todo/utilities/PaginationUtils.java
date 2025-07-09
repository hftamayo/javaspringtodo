package com.hftamayo.java.todo.utilities;

import com.hftamayo.java.todo.dto.pagination.CursorPaginationDto;
import org.springframework.data.domain.Page;
import java.util.Base64;

public class PaginationUtils {

    /**
     * Convert Spring Data Page to CursorPaginationDto
     */
    public static CursorPaginationDto toCursorPagination(Page<?> page, String sort) {
        int currentPage = page.getNumber() + 1; // Convert from 0-based to 1-based
        int totalPages = page.getTotalPages();
        boolean isFirstPage = page.isFirst();
        boolean isLastPage = page.isLast();
        boolean hasPrev = page.hasPrevious();
        boolean hasMore = page.hasNext();
        
        // Generate cursors (simplified implementation)
        String nextCursor = hasMore ? encodeCursor(page.getNumber() + 1, page.getSize()) : null;
        String prevCursor = hasPrev ? encodeCursor(page.getNumber() - 1, page.getSize()) : null;
        
        return new CursorPaginationDto(
            nextCursor,
            prevCursor,
            page.getSize(),
            page.getTotalElements(),
            hasMore,
            currentPage,
            totalPages,
            sort != null ? sort : "desc",
            hasPrev,
            isFirstPage,
            isLastPage
        );
    }
    
    /**
     * Encode cursor from page and size
     */
    private static String encodeCursor(int page, int size) {
        String cursorData = page + ":" + System.currentTimeMillis() + ":";
        return Base64.getEncoder().encodeToString(cursorData.getBytes());
    }
    
    /**
     * Decode cursor to get page number
     */
    public static int decodeCursorPage(String cursor) {
        try {
            String decoded = new String(Base64.getDecoder().decode(cursor));
            return Integer.parseInt(decoded.split(":")[0]);
        } catch (Exception e) {
            return 0; // Default to first page if cursor is invalid
        }
    }
} 