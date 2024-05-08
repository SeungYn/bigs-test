package org.bigs.util.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LoggingFetchInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // 요청 전에 로깅
        log.info("Request URI: {}" , request.getURI());
        // 실제 요청 실행
        ClientHttpResponse response = execution.execute(request, body);

        // 응답 후에 로깅
        log.info("Response Status: {}" , response.getStatusCode());
        // 응답 내용 로깅
        //System.out.println("Response Body: " + StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));

        return response;
    }
}
