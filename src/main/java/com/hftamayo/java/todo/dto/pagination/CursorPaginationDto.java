package com.hftamayo.java.todo.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CursorPaginationDto {
    private String nextCursor;
    private String prevCursor;
    private int limit;
    private long totalCount;
    private boolean hasMore;
    private int currentPage;
    private int totalPages;
    private String order;
    private boolean hasPrev;
    private boolean isFirstPage;
    private boolean isLastPage;


} 