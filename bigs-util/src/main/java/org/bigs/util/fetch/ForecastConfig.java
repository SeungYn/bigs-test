package org.bigs.util.fetch;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@NoArgsConstructor
@Component
public class ForecastConfig {
    private static final String OpenAPIProtocol = "http";
    private static final String OpenAPIHost = "apis.data.go.kr";
    @Value("${openapi.service-key}")
    private String serviceKey;

    public UriComponentsBuilder openAPIUriBuilder(){
        return UriComponentsBuilder.newInstance()
                .scheme(OpenAPIProtocol)
                .host(OpenAPIHost)
                .path("/1360000/VilageFcstInfoService_2.0/getVilageFcst")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("dataType", "JSON")
                .cloneBuilder();
    }

}
