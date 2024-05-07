package org.bigs.fetch;

import lombok.RequiredArgsConstructor;
import org.bigs.interceptor.LoggingFetchInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RestTemplate {
    private final LoggingFetchInterceptor loggingFetchInterceptor;

    @Bean
    public 
}
