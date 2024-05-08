package org.bigs.api.forecast.service;

import lombok.RequiredArgsConstructor;
import org.bigs.api.forecast.dto.ShortForecastDTO;
import org.bigs.domain.forecast.common.ForecastCategory;
import org.bigs.domain.forecast.entity.ShortForecast;
import org.bigs.domain.forecast.repository.ShortForecastRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ShortForecastService {
    public static final String[] TIME_LIST = {"0200", "0500", "0800", "1100","1400","1700","2000","2300"};
    private final RestTemplate restTemplate;
    private final ShortForecastRepository shortForecastRepository;

    public List<ShortForecastDTO.ShortForecastRes> getMunchungroShortForecast(){
        LocalDateTime now = LocalDateTime.now();
        String[] formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HHmm")).split(" ");
        System.out.println(formatedNow[0] +", " + formatedNow[1] + ", " + findTime(Integer.parseInt(formatedNow[1])));
        // 현재 날짜만 가져오도록 필터 및 단위 추가
        List<ShortForecastDTO.ShortForecastRes> shortForecasts = shortForecastRepository
                .findByBaseDateAndBaseTime(formatedNow[0], findTime(Integer.parseInt(formatedNow[1])))
                .stream()
                .filter(item -> item.getFcstDate().equals(formatedNow[0]))
                .map(item -> ShortForecastDTO.ShortForecastRes.builder()
                        .baseTime(item.getBaseTime())
                        .fcstValue(ForecastCategory.getCodeValue(item.getCategory(), item.getFcstValue()) + ForecastCategory.valueOf(item.getCategory()).getUnit())
                        .baseDate(item.getBaseDate())
                        .nx(item.getNx())
                        .ny(item.getNy())
                        .category(ForecastCategory.valueOf(item.getCategory()).getName())
                        .fcstDate(item.getFcstDate())
                        .fcstTime(item.getFcstTime())
                        .build())
                .toList();


        return shortForecasts;
    }

    public Boolean loadMunchungroForecast(){
        LocalDateTime now = LocalDateTime.now();
        String[] formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HHmm")).split(" ");
        String baseTime = findTime(Integer.parseInt(formatedNow[1]));
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("apis.data.go.kr")
                .path("/1360000/VilageFcstInfoService_2.0/getVilageFcst")
                .queryParam("ServiceKey", "9SpFR2/nELWRsR606KznXxUyxq/fKoZfiJgVrdOFHfhHtGbErI9MhPVJVsTblmwXHbpaYMCj/hSZs0vxEgZj9g==")
                .queryParam("numOfRows", "266")
                .queryParam("pageNo", "1")
                .queryParam("base_date", formatedNow[0])
                .queryParam("base_time", baseTime)
                .queryParam("nx", "61")
                .queryParam("ny", "131")
                .queryParam("dataType", "JSON");

        UriComponents uri = builder.build();
        System.out.println("uri:::: " + uri.toUri()); // toUri는 encode를 하지 않으면 인코딩이 되지 않는 uri를반환해줌
        HttpEntity<String> request2 = new HttpEntity<>("requestDto", headers);

        ShortForecastDTO.ShortForecastOpenAPIRes shortForecastResp;
        try{
            ResponseEntity<ShortForecastDTO.ShortForecastOpenAPIRes> res = restTemplate.exchange(uri.toUri(), HttpMethod.GET, request, ShortForecastDTO.ShortForecastOpenAPIRes.class );
            System.out.println("res::" + res);
            shortForecastResp = res.getBody();

        }catch(Exception e){
            System.out.println("요청에 에러가 발생! " + e);
            ResponseEntity<String> res = restTemplate.exchange(uri.toUri(), HttpMethod.GET, request, String.class );
            System.out.println("string 결과::"+res);
            return false;
        }


        if(!shortForecastResp.response.header.resultCode.equals("00")){
            System.out.println("api 요청에러 에러코드:::"+ shortForecastResp.response.header.resultCode);
            System.out.println(shortForecastResp.response);
            return false;
        }


        shortForecastResp.response.body.items.item.forEach(item -> {
            ShortForecast shortForecast = shortForecastRepository.findByShortForecast(item.getBaseDate(), item.getBaseTime(),item.getCategory(), item.getFcstDate(), item.getFcstTime(), item.getNx(), item.getNy()).orElse(null);
            if(shortForecast == null){
                ShortForecast forecastObj = ShortForecast.builder()
                        .nx(item.getNx())
                        .ny(item.getNy())
                        .fcstDate(item.getFcstDate())
                        .fcstTime(item.getFcstTime())
                        .fcstValue(item.getFcstValue())
                        .baseDate(item.getBaseDate())
                        .baseTime(item.getBaseTime())
                        .category(item.getCategory())
                        .build();
                shortForecastRepository.save(forecastObj);
            }else{
                shortForecast.updateFcstValue(item.getFcstValue());
                shortForecastRepository.save(shortForecast);
            }


        });

        return true;
    }


    public String findTime(int now){
        int end = TIME_LIST.length - 1;
        int start = 0;
        String result = "";

        while(start <= end){
            int mid = (start + end) / 2;
            int midValue = Integer.parseInt(TIME_LIST[mid]);
            if(midValue > now){
                end = mid - 1;
            }else{
                result = TIME_LIST[mid];
                start = mid + 1;
            }
        }
        return result;
    }
}
