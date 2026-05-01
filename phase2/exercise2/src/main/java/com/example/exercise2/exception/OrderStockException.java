package com.example.exercise2.exception;

public class OrderStockException extends RuntimeException{
    private String message;
    public OrderStockException(){}
    public OrderStockException(String message){
        super(message);
        this.message = message;
    }
}
