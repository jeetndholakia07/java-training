package com.company.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InventoryCheckItemRequest {
        @NotNull(message = "Product Guid is required.")
        @NotBlank(message = "Product Guid cannot be empty.")
        private String productGuid;
        @NotNull(message = "Qty is required.")
        @Min(value = 1, message = "Qty must be greater than 1.")
        private int requestedQty;

        public String getProductGuid() {
                return productGuid;
        }

        public void setProductGuid(String productGuid) {
                this.productGuid = productGuid;
        }

        public int getRequestedQty() {
                return requestedQty;
        }

        public void setRequestedQty(int requestedQty) {
                this.requestedQty = requestedQty;
        }
}