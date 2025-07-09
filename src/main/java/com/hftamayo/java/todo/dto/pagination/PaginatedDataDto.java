package com.hftamayo.java.todo.dto.pagination;

import java.util.List;

public class PaginatedDataDto<T> {
    private List<T> content;
    private CursorPaginationDto pagination;

    public PaginatedDataDto() {}

    public PaginatedDataDto(List<T> content, CursorPaginationDto pagination) {
        this.content = content;
        this.pagination = pagination;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public CursorPaginationDto getPagination() {
        return pagination;
    }

    public void setPagination(CursorPaginationDto pagination) {
        this.pagination = pagination;
    }
} 