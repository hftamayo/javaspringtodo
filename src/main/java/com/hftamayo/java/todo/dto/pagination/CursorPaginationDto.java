package com.hftamayo.java.todo.dto.pagination;

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

    public CursorPaginationDto() {}

    public CursorPaginationDto(String nextCursor, String prevCursor, int limit, long totalCount, 
                              boolean hasMore, int currentPage, int totalPages, String order, 
                              boolean hasPrev, boolean isFirstPage, boolean isLastPage) {
        this.nextCursor = nextCursor;
        this.prevCursor = prevCursor;
        this.limit = limit;
        this.totalCount = totalCount;
        this.hasMore = hasMore;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.order = order;
        this.hasPrev = hasPrev;
        this.isFirstPage = isFirstPage;
        this.isLastPage = isLastPage;
    }

    // Getters and Setters
    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public String getPrevCursor() {
        return prevCursor;
    }

    public void setPrevCursor(String prevCursor) {
        this.prevCursor = prevCursor;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public boolean isHasPrev() {
        return hasPrev;
    }

    public void setHasPrev(boolean hasPrev) {
        this.hasPrev = hasPrev;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }
} 