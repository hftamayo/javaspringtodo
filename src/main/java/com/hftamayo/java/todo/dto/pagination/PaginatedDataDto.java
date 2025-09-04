package com.hftamayo.java.todo.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaginatedDataDto<T> {
    private List<T> content;
    private CursorPaginationDto pagination;
} 