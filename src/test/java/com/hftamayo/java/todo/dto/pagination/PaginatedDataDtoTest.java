package com.hftamayo.java.todo.dto.pagination;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class PaginatedDataDtoTest {

    @Test
    void testDefaultConstructor() {
        PaginatedDataDto<String> dto = new PaginatedDataDto<>();
        
        assertNotNull(dto);
        assertNull(dto.getContent());
        assertNull(dto.getPagination());
    }

    @Test
    void testParameterizedConstructor() {
        List<String> content = List.of("item1", "item2", "item3");
        CursorPaginationDto pagination = new CursorPaginationDto();
        pagination.setLimit(10);
        pagination.setTotalCount(3);

        PaginatedDataDto<String> dto = new PaginatedDataDto<>(content, pagination);

        assertEquals(content, dto.getContent());
        assertEquals(pagination, dto.getPagination());
        assertEquals(3, dto.getContent().size());
        assertEquals(10, dto.getPagination().getLimit());
        assertEquals(3, dto.getPagination().getTotalCount());
    }

    @Test
    void testSettersAndGetters() {
        PaginatedDataDto<Integer> dto = new PaginatedDataDto<>();

        List<Integer> content = List.of(1, 2, 3, 4, 5);
        CursorPaginationDto pagination = new CursorPaginationDto();
        pagination.setCurrentPage(1);
        pagination.setTotalPages(2);

        dto.setContent(content);
        dto.setPagination(pagination);

        assertEquals(content, dto.getContent());
        assertEquals(pagination, dto.getPagination());
        assertEquals(5, dto.getContent().size());
        assertEquals(1, dto.getPagination().getCurrentPage());
        assertEquals(2, dto.getPagination().getTotalPages());
    }

    @Test
    void testWithEmptyContent() {
        List<String> emptyContent = new ArrayList<>();
        CursorPaginationDto pagination = new CursorPaginationDto();
        pagination.setTotalCount(0);
        pagination.setHasMore(false);

        PaginatedDataDto<String> dto = new PaginatedDataDto<>(emptyContent, pagination);

        assertTrue(dto.getContent().isEmpty());
        assertEquals(0, dto.getPagination().getTotalCount());
        assertFalse(dto.getPagination().isHasMore());
    }

    @Test
    void testWithNullPagination() {
        List<String> content = List.of("test");
        
        PaginatedDataDto<String> dto = new PaginatedDataDto<>(content, null);

        assertEquals(content, dto.getContent());
        assertNull(dto.getPagination());
    }
} 