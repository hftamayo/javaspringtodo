package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.pagination.CursorPaginationDto;

public class PaginatorHelper {
    CursorPaginationDto createEmptyPaginationInfo(int page, int size, String sort) {
        return createPaginationInfo(page, size, sort, 0L, 0);
    }

    CursorPaginationDto createPaginationInfo(int currentPage, int limit, String order, long totalCount, int totalPages) {
        CursorPaginationDto paginationInfo = new CursorPaginationDto();
        paginationInfo.setNextCursor(null);
        paginationInfo.setPrevCursor(null);
        paginationInfo.setLimit(limit);
        paginationInfo.setTotalCount(totalCount);
        paginationInfo.setHasMore(totalCount > limit);
        paginationInfo.setCurrentPage(currentPage);
        paginationInfo.setTotalPages(totalPages);
        paginationInfo.setOrder(order);
        paginationInfo.setHasPrev(currentPage > 0);
        paginationInfo.setFirstPage(currentPage == 0);
        paginationInfo.setLastPage(currentPage == totalPages - 1 || totalPages <= 1);
        return paginationInfo;
    }    
}
