package org.bigs.util.fetch;

import lombok.RequiredArgsConstructor;
import org.bigs.util.interceptor.LoggingFetchInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
public class RestTemplateConfig {
    private final LoggingFetchInterceptor loggingFetchInterceptor;

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(loggingFetchInterceptor));
        return restTemplate;
    }
}
