package com.company.orderservice.dto;

import java.util.List;

public class PaginatedResponse<T>{
    private List<T> data;
    private int page;
    private int size;
    private int totalPages;
    public PaginatedResponse(List<T> data, int page, int size, int totalPages) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<T> getData() {
        return data;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
