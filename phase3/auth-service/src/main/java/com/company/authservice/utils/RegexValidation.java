package com.company.authservice.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RegexValidation {
    private final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%])[A-Za-z\\d@#$%]{8,}$";

    public boolean validateEmail(String text){
        return Pattern.matches(emailRegex,text);
    }

    public boolean validatePassword(String text){
        return Pattern.matches(passwordRegex,text);
    }
}
