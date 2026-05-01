package com.example.exercise2.exception;

public class EntityNotFoundException extends RuntimeException{
    private String entityName;
    private String message;

    public EntityNotFoundException(){}

    public EntityNotFoundException(String entityName, String message){
        super(message);
        this.entityName = entityName;
        this.message = message;
    }
}
