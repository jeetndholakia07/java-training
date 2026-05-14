package com.company.orderservice.exception;

public class EntityExistsException extends RuntimeException {
    private String entityName;
    private String message;

    public EntityExistsException() {
    }

    public EntityExistsException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
        this.message = message;
    }
}

