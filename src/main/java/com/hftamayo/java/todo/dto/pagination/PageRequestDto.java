package com.hftamayo.java.todo.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageRequestDto {
    private int page = 0;
    private int size = 10;
    private String sort;
} 