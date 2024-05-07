package org.bigs.domain.forecast.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name="short_forecast")
@Entity
public class ShortForecast {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    public String baseDate;
    private String category;
    private String baseTime;
    private String fcstDate;
    private String fcstTime;

    private String fcstValue;
    private Integer nx;
    private Integer ny;

    @Builder
    public ShortForecast(String baseDate, String category, String baseTime, String fcstDate, String fcstTime, String fcstValue, Integer nx, Integer ny){
        this.baseDate = baseDate;
        this.category = category;
        this.baseTime = baseTime;
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.fcstValue = fcstValue;
        this.nx = nx;
        this.ny = ny;

    }

    public void updateFcstValue(String fcstValue){
        this.fcstValue = fcstValue;
    }
}
