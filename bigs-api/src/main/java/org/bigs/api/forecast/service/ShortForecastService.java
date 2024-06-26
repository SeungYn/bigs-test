package org.bigs.api.forecast.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigs.api.forecast.dto.KakaoLocalDTO;
import org.bigs.api.forecast.dto.ShortForecastDTO;
import org.bigs.domain.forecast.common.ForecastCategory;
import org.bigs.domain.forecast.entity.Location;
import org.bigs.domain.forecast.entity.ShortForecast;
import org.bigs.domain.forecast.repository.LocationRepository;
import org.bigs.domain.forecast.repository.ShortForecastRepository;
import org.bigs.util.common.GeoUtil;
import org.bigs.util.fetch.ForecastConfig;
import org.bigs.util.fetch.KakaoLocalConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShortForecastService {
    public static final String[] TIME_LIST = {"0200", "0500", "0800", "1100","1400","1700","2000","2300"};
    private final RestTemplate restTemplate;
    private final ShortForecastRepository shortForecastRepository;
    private final LocationRepository locationRepository;
    private final ForecastConfig forecastConfig;
    private final KakaoLocalConfig kakaoLocalConfig;

    public List<ShortForecastDTO.ShortForecastRes> getMunchungroShortForecast(){

        LocalDateTime now = LocalDateTime.now();
        String[] formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HHmm")).split(" ");

        // 현재 날짜만 가져오도록 필터 및 단위 추가
        List<ShortForecastDTO.ShortForecastRes> shortForecasts = shortForecastRepository
                .findByBaseDateAndBaseTimeAndNxAndNy(formatedNow[0], findTime(Integer.parseInt(formatedNow[1])), 62, 130)
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
        UriComponentsBuilder builder = forecastConfig.openAPIUriBuilder()
                .queryParam("numOfRows", "266")
                .queryParam("pageNo", "1")
                .queryParam("base_date", formatedNow[0])
                .queryParam("base_time", baseTime)
                .queryParam("nx", "62")
                .queryParam("ny", "130");
        UriComponents uri = builder.build(true); // build시 true를 설정하면 인코딩을 진행하지 않음

        ShortForecastDTO.ShortForecastOpenAPIRes shortForecastResp;

        try{
            ResponseEntity<ShortForecastDTO.ShortForecastOpenAPIRes> res = restTemplate.exchange(uri.toUri(), HttpMethod.GET, request, ShortForecastDTO.ShortForecastOpenAPIRes.class );
            shortForecastResp = res.getBody();

        }catch(Exception e){
            log.info("openapi 요청 에러 발생! {}" , e);
            ResponseEntity<String> res = restTemplate.exchange(uri.toUri(), HttpMethod.GET, request, String.class );
            log.info("openapi 요청 string 결과:: {}", res);
            return false;
        }


        if(!shortForecastResp.response.header.resultCode.equals("00")){
            log.info("api 요청에러 에러코드::: {}", shortForecastResp.response.header.resultCode);
            log.info("api 요청에러 에러결과::: {}", shortForecastResp.response);
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

    public List<ShortForecastDTO.ShortForecastRes> getLocalShortForecast(String local){
        // 카카오 api를 이용해 위도 경도 불러오기
        // 위도: latitude y축, 경도: longitude x축
        double[] searchLocalGeo = getGeolocationByKakaoAPI(local);
        double searchLocalX = searchLocalGeo[0];
        double searchLocalY = searchLocalGeo[1];


        // 위도, 경도로 nx, ny 좌표 구하기
        int[] targetNxNy = findLocationNxNy(searchLocalX, searchLocalY);
        int targetNx = targetNxNy[0];
        int targetNy = targetNxNy[1];

        LocalDateTime now = LocalDateTime.now();
        String[] formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HHmm")).split(" ");

        // 현재 날짜만 가져오도록 필터 및 단위 추가
        List<ShortForecastDTO.ShortForecastRes> shortForecasts = shortForecastRepository
                .findByBaseDateAndBaseTimeAndNxAndNy(formatedNow[0], findTime(Integer.parseInt(formatedNow[1])), targetNx, targetNy)
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

    public Boolean loadLocalForecast(String local){
        // 카카오 api를 이용해 위도 경도 불러오기
        // 위도: latitude y축, 경도: longitude x축
        double[] searchLocalGeo = getGeolocationByKakaoAPI(local);
        double searchLocalX = searchLocalGeo[0];
        double searchLocalY = searchLocalGeo[1];


        // 위도, 경도로 nx, ny 좌표 구하기
        int[] targetNxNy = findLocationNxNy(searchLocalX, searchLocalY);
        int targetNx = targetNxNy[0];
        int targetNy = targetNxNy[1];

        LocalDateTime now = LocalDateTime.now();
        String[] formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMdd HHmm")).split(" ");

        String baseTime = findTime(Integer.parseInt(formatedNow[1]));
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request = new HttpEntity<>(headers);
        UriComponentsBuilder builder = forecastConfig.openAPIUriBuilder()
                .queryParam("numOfRows", "266")
                .queryParam("pageNo", "1")
                .queryParam("base_date", formatedNow[0])
                .queryParam("base_time", baseTime)
                .queryParam("nx", targetNx)
                .queryParam("ny", targetNy);
        UriComponents uri = builder.build(true); // build시 true를 설정하면 인코딩을 진행하지 않음

        ShortForecastDTO.ShortForecastOpenAPIRes shortForecastResp;

        try{
            ResponseEntity<ShortForecastDTO.ShortForecastOpenAPIRes> res = restTemplate.exchange(uri.toUri(), HttpMethod.GET, request, ShortForecastDTO.ShortForecastOpenAPIRes.class );
            shortForecastResp = res.getBody();

        }catch(Exception e){
            log.info("openapi 요청 에러 발생! {}" , e);
            ResponseEntity<String> res = restTemplate.exchange(uri.toUri(), HttpMethod.GET, request, String.class );
            log.info("openapi 요청 string 결과:: {}", res);
            return false;
        }


        if(!shortForecastResp.response.header.resultCode.equals("00")){
            log.info("api 요청에러 에러코드::: {}", shortForecastResp.response.header.resultCode);
            log.info("api 요청에러 에러결과::: {}", shortForecastResp.response);
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

    public double[] getGeolocationByKakaoAPI(String local){
        HttpHeaders kakaoHeaders = kakaoLocalConfig.getKakaoRequestHeader();
        UriComponents kakaoUrl = kakaoLocalConfig.openAPIUriBuilder(local);
        HttpEntity<KakaoLocalDTO.KakaoLocalAPIRes> kakaoHttpEntity = new HttpEntity<>(kakaoHeaders);

        KakaoLocalDTO.KakaoLocalAPIRes kakaoLocalRes;
        try{
            ResponseEntity<KakaoLocalDTO.KakaoLocalAPIRes> localResEntity = restTemplate.exchange(kakaoUrl.encode().toUri(), HttpMethod.GET, kakaoHttpEntity, KakaoLocalDTO.KakaoLocalAPIRes.class);
            kakaoLocalRes = localResEntity.getBody();
        }catch(RestClientException e){
            throw new Error("서버문제 발생!");
        }

        if(kakaoLocalRes.documents.size() == 0) throw new Error("해당 위치 정보를 찾을 수 없습니다!! 검색된 지역명::: " + local );

        double searchLocalX = kakaoLocalRes.documents.get(0).getX();
        double searchLocalY = kakaoLocalRes.documents.get(0).getY();

        return new double[]{searchLocalX,searchLocalY };
    }

    public int[] findLocationNxNy(double searchLocalX, double searchLocalY){
        double[] yRange = GeoUtil.calculateLatitudeRange(searchLocalY);
        double[] xRange = GeoUtil.calculateLongitudeRange(searchLocalY, searchLocalX);
        List<Location> locationCandidates = locationRepository.findCandidatesByLatitudeAndLongitude(yRange[1], yRange[0],xRange[1], xRange[0]).stream()
                .sorted((a,b)-> {
                    double distanceA = GeoUtil.calculateDistance(searchLocalY, searchLocalX, a.getLongitude(), a.getLatitude());
                    double distanceB = GeoUtil.calculateDistance(searchLocalY, searchLocalX, b.getLongitude(), b.getLatitude());
                    return Double.compare(distanceA, distanceB);
                } ).toList();

        int targetNx = locationCandidates.get(0).getGridX();
        int targetNy = locationCandidates.get(0).getGridY();

        return new int[] {targetNx, targetNy};
    }

    public String findTime(int now){
        int end = TIME_LIST.length - 1;
        int start = 0;
        String result = "0200";

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
