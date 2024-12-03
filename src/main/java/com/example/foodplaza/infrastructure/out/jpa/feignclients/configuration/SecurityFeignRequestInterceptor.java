package com.example.foodplaza.infrastructure.out.jpa.feignclients.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SecurityFeignRequestInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        String token = getBearerTokenHeader();
        if (token != null) {
            template.header(AUTHORIZATION_HEADER, token);
        }
    }

    public static String getBearerTokenHeader() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader(AUTHORIZATION_HEADER);
            if (token != null && !token.isEmpty()) {
                return token;
            }
        }
        throw new IllegalStateException("Authorization header is missing in the current request");
    }
}
