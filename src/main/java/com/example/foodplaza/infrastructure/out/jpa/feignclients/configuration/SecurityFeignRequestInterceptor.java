package com.example.foodplaza.infrastructure.out.jpa.feignclients.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SecurityFeignRequestInterceptor implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SecurityFeignRequestInterceptor.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        String bearerToken = getBearerTokenHeader();
        if (bearerToken != null) {
            template.header(AUTHORIZATION_HEADER, bearerToken);
        } else {
            log.warn("Authorization header is missing in the current request context.");
        }
    }

    private String getBearerTokenHeader() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            String token = servletRequestAttributes.getRequest().getHeader(AUTHORIZATION_HEADER);
            log.info("Extracted Authorization header: {}", token);
            return token;
        }
        log.warn("RequestAttributes is null or not an instance of ServletRequestAttributes.");
        return null;
    }
}
