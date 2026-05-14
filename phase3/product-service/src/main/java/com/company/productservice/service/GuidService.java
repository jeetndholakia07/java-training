package com.company.productservice.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GuidService {
    public GuidService() {
    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public boolean verifyUUID(String text) {
        try {
            UUID.fromString(text);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
