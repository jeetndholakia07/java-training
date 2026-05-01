package com.example.exercise2.exception;

public class EntityExistsException extends RuntimeException{
    private String entityName;
    private String message;

    public EntityExistsException(){}

    public EntityExistsException(String entityName, String message){
        super(message);
        this.entityName = entityName;
        this.message = message;
    }
}
