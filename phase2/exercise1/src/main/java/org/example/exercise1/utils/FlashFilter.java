package org.example.exercise1.utils;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class FlashFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        FlashMessage flash =
                (FlashMessage) session.getAttribute("flash");

        if (flash != null) {
            req.setAttribute("flash", flash);
            session.removeAttribute("flash");
        }

        chain.doFilter(request, response);
    }
}
