package org.example.exercise1.utils;

import jakarta.servlet.http.HttpServletRequest;

public class Toast {
    public static void setMessage(HttpServletRequest req, String type, String msg) {
        req.getSession().setAttribute("flash",
                new FlashMessage(type, msg));
    }
}
