package com.company.inventoryservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuthFeignInterceptor implements RequestInterceptor {

    @Value("${GATEWAY_SECRET}")
    private String gatewaySecret;

    @Override
    public void apply(RequestTemplate template) {
        RequestAttributes requestAttributes =
                RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {

            HttpServletRequest request =
                    ((ServletRequestAttributes) requestAttributes)
                            .getRequest();

            String authHeader =
                    request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authHeader != null) {
                template.header(HttpHeaders.AUTHORIZATION, authHeader);
            }

            String role = request.getHeader("X-Role");
            String id = request.getHeader("X-ID");

            if (role != null) {
                template.header("X-Role", role);
            }

            if (id != null) {
                template.header("X-ID", id);
            }
        }

        template.header("X-Gateway-Secret", gatewaySecret);
    }
}