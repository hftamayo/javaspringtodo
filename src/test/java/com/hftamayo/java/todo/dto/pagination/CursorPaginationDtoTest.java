package com.hftamayo.java.todo.dto.pagination;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CursorPaginationDtoTest {

    @Test
    void testDefaultConstructor() {
        CursorPaginationDto dto = new CursorPaginationDto();
        
        assertNotNull(dto);
        assertNull(dto.getNextCursor());
        assertNull(dto.getPrevCursor());
        assertEquals(0, dto.getLimit());
        assertEquals(0, dto.getTotalCount());
        assertFalse(dto.isHasMore());
        assertEquals(0, dto.getCurrentPage());
        assertEquals(0, dto.getTotalPages());
        assertNull(dto.getOrder());
        assertFalse(dto.isHasPrev());
        assertFalse(dto.isFirstPage());
        assertFalse(dto.isLastPage());
    }

    @Test
    void testParameterizedConstructor() {
        String nextCursor = "next123";
        String prevCursor = "prev456";
        int limit = 10;
        long totalCount = 100;
        boolean hasMore = true;
        int currentPage = 2;
        int totalPages = 10;
        String order = "desc";
        boolean hasPrev = true;
        boolean isFirstPage = false;
        boolean isLastPage = false;

        CursorPaginationDto dto = new CursorPaginationDto(
            nextCursor, prevCursor, limit, totalCount, hasMore, 
            currentPage, totalPages, order, hasPrev, isFirstPage, isLastPage
        );

        assertEquals(nextCursor, dto.getNextCursor());
        assertEquals(prevCursor, dto.getPrevCursor());
        assertEquals(limit, dto.getLimit());
        assertEquals(totalCount, dto.getTotalCount());
        assertEquals(hasMore, dto.isHasMore());
        assertEquals(currentPage, dto.getCurrentPage());
        assertEquals(totalPages, dto.getTotalPages());
        assertEquals(order, dto.getOrder());
        assertEquals(hasPrev, dto.isHasPrev());
        assertEquals(isFirstPage, dto.isFirstPage());
        assertEquals(isLastPage, dto.isLastPage());
    }

    @Test
    void testSettersAndGetters() {
        CursorPaginationDto dto = new CursorPaginationDto();

        dto.setNextCursor("next123");
        dto.setPrevCursor("prev456");
        dto.setLimit(15);
        dto.setTotalCount(150);
        dto.setHasMore(true);
        dto.setCurrentPage(3);
        dto.setTotalPages(15);
        dto.setOrder("asc");
        dto.setHasPrev(true);
        dto.setFirstPage(false);
        dto.setLastPage(false);

        assertEquals("next123", dto.getNextCursor());
        assertEquals("prev456", dto.getPrevCursor());
        assertEquals(15, dto.getLimit());
        assertEquals(150, dto.getTotalCount());
        assertTrue(dto.isHasMore());
        assertEquals(3, dto.getCurrentPage());
        assertEquals(15, dto.getTotalPages());
        assertEquals("asc", dto.getOrder());
        assertTrue(dto.isHasPrev());
        assertFalse(dto.isFirstPage());
        assertFalse(dto.isLastPage());
    }
} 