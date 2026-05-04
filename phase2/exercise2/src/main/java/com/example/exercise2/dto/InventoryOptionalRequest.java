package com.example.exercise2.dto;
import jakarta.validation.constraints.Min;

public class InventoryOptionalRequest {
        private String description;
        @Min(value = 1, message = "Price must be atleast 1.")
        private Double price;
        @Min(value = 1, message = "Available units must be atleast 1.")
        private Integer availableUnits;

        public Double getPrice() {
            return price;
        }

        public Integer getAvailableUnits() {
            return availableUnits;
        }

        public String getDescription() {
            return description;
        }

        public void setAvailableUnits(Integer availableUnits) {
            this.availableUnits = availableUnits;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
}
