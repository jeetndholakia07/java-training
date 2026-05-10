package com.company.orderservice.dto;

import java.util.List;

public class ProductListResponse{
    private List<ProductResponse> products;
    private List<String> missingGuids;

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }

    public List<String> getMissingGuids() {
        return missingGuids;
    }

    public void setMissingGuids(List<String> missingGuids) {
        this.missingGuids = missingGuids;
    }
}