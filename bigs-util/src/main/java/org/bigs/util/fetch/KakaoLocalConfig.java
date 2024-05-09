package org.bigs.util.fetch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;



@Component
public class KakaoLocalConfig {
    private static final String OpenAPIProtocol = "http";
    private static final String OpenAPIHost = "dapi.kakao.com";
    @Value("${kakaoapi.service-key}")
    private String serviceKey;

    public UriComponents openAPIUriBuilder(String local){
        return UriComponentsBuilder.newInstance()
                .scheme(OpenAPIProtocol)
                .host(OpenAPIHost)
                .path("/v2/local/search/address.json")
                .queryParam("query", local)
                .build();
    }

    public HttpHeaders getKakaoRequestHeader(){
        HttpHeaders kakaoHeader = new org.springframework.http.HttpHeaders();
        kakaoHeader.set("Authorization", serviceKey);
        kakaoHeader.setContentType(MediaType.APPLICATION_JSON);
        return kakaoHeader;

    }
}
