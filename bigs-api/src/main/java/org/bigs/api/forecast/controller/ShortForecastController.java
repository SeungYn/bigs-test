package org.bigs.api.forecast.controller;

import lombok.RequiredArgsConstructor;
import org.bigs.api.forecast.dto.ShortForecastDTO;
import org.bigs.api.forecast.service.ShortForecastService;
import org.bigs.domain.forecast.entity.ShortForecast;
import org.bigs.util.common.CommonResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/forecast")
@RestController
public class ShortForecastController {
    private final ShortForecastService shortForecastService;
    @GetMapping("/short-munchungro")
    public ResponseEntity<List<ShortForecastDTO.ShortForecastRes>> getMunchungroShortForecast(){
        List<ShortForecastDTO.ShortForecastRes> shortForecasts = shortForecastService.getMunchungroShortForecast();

        if(shortForecasts.size() == 0) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.status(HttpStatus.OK).body(shortForecasts);
    }

    @PostMapping("/short-munchungro")
    public ResponseEntity<CommonResponse> postMunchungroShortForecast(){
        Boolean res = shortForecastService.loadMunchungroForecast();
        if(!res) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("잠시 후 다시 시도해주세요")
                .success(false)
                .data(null)
                .build());

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("저장 완료!")
                .success(true)
                .data(null)
                .build());
    }

    // kakaoapi를 사용해 해당 지역의 위도, 경도를 구한 후
    // location db에서 nx, ny를 구한 후 해당 좌표의 날씨를 가져오는 메서드 구현 중...
    @GetMapping("/short-local/{location}")
    public ResponseEntity<?> getLocalSHortForecast(@PathVariable("location") String location){
        shortForecastService.getLocalShortForecast(location);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
