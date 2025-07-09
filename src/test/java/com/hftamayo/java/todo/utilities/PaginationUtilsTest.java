package com.hftamayo.java.todo.utilities;

import com.hftamayo.java.todo.dto.pagination.CursorPaginationDto;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PaginationUtilsTest {

    @Test
    void testToCursorPagination_WithContent() {
        // Arrange
        List<String> content = List.of("item1", "item2", "item3");
        Page<String> page = new PageImpl<>(content, PageRequest.of(1, 3), 10);
        String sort = "desc";

        // Act
        CursorPaginationDto result = PaginationUtils.toCursorPagination(page, sort);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getLimit());
        assertEquals(10, result.getTotalCount());
        assertEquals(2, result.getCurrentPage()); // 0-based to 1-based conversion
        assertEquals(4, result.getTotalPages()); // 10 items / 3 per page = 4 pages
        assertEquals("desc", result.getOrder());
        assertTrue(result.isHasMore());
        assertTrue(result.isHasPrev());
        assertFalse(result.isFirstPage());
        assertFalse(result.isLastPage());
        assertNotNull(result.getNextCursor());
        assertNotNull(result.getPrevCursor());
    }

    @Test
    void testToCursorPagination_FirstPage() {
        // Arrange
        List<String> content = List.of("item1", "item2");
        Page<String> page = new PageImpl<>(content, PageRequest.of(0, 2), 5);
        String sort = "asc";

        // Act
        CursorPaginationDto result = PaginationUtils.toCursorPagination(page, sort);

        // Assert
        assertEquals(1, result.getCurrentPage());
        assertEquals(3, result.getTotalPages());
        assertTrue(result.isFirstPage());
        assertFalse(result.isHasPrev());
        assertTrue(result.isHasMore());
        assertNotNull(result.getNextCursor());
        assertNull(result.getPrevCursor());
        assertEquals("asc", result.getOrder());
    }

    @Test
    void testToCursorPagination_LastPage() {
        // Arrange
        List<String> content = List.of("item1");
        Page<String> page = new PageImpl<>(content, PageRequest.of(2, 2), 5);
        String sort = null;

        // Act
        CursorPaginationDto result = PaginationUtils.toCursorPagination(page, sort);

        // Assert
        assertEquals(3, result.getCurrentPage());
        assertEquals(3, result.getTotalPages());
        assertFalse(result.isFirstPage());
        assertTrue(result.isHasPrev());
        assertFalse(result.isHasMore());
        assertNull(result.getNextCursor());
        assertNotNull(result.getPrevCursor());
        assertEquals("desc", result.getOrder()); // default when sort is null
    }

    @Test
    void testToCursorPagination_EmptyPage() {
        // Arrange
        List<String> content = List.of();
        Page<String> page = new PageImpl<>(content, PageRequest.of(0, 10), 0);

        // Act
        CursorPaginationDto result = PaginationUtils.toCursorPagination(page, "desc");

        // Assert
        assertEquals(1, result.getCurrentPage());
        assertEquals(0, result.getTotalPages());
        assertEquals(0, result.getTotalCount());
        assertTrue(result.isFirstPage());
        assertTrue(result.isLastPage());
        assertFalse(result.isHasMore());
        assertFalse(result.isHasPrev());
        assertNull(result.getNextCursor());
        assertNull(result.getPrevCursor());
    }

    @Test
    void testDecodeCursorPage_ValidCursor() {
        // Arrange
        String validCursor = "MQ=="; // Base64 for "1:"

        // Act
        int result = PaginationUtils.decodeCursorPage(validCursor);

        // Assert
        assertEquals(1, result);
    }

    @Test
    void testDecodeCursorPage_InvalidCursor() {
        // Arrange
        String invalidCursor = "invalid_base64";

        // Act
        int result = PaginationUtils.decodeCursorPage(invalidCursor);

        // Assert
        assertEquals(0, result); // Should return default value
    }

    @Test
    void testDecodeCursorPage_NullCursor() {
        // Act
        int result = PaginationUtils.decodeCursorPage(null);

        // Assert
        assertEquals(0, result); // Should return default value
    }

    @Test
    void testDecodeCursorPage_EmptyCursor() {
        // Act
        int result = PaginationUtils.decodeCursorPage("");

        // Assert
        assertEquals(0, result); // Should return default value
    }
} 