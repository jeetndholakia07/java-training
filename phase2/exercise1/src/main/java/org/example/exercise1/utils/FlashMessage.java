package org.example.exercise1.utils;

import java.io.Serializable;

public class FlashMessage implements Serializable {
    private final String type;
    private final String message;

    public FlashMessage(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}