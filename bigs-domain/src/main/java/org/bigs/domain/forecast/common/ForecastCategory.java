package org.bigs.domain.forecast.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ForecastCategory {
    POP("강수확률", "%"),
    PTY("강수형태", ""),
    PCP("1시간 강수량", ""),
    REH("습도", "%"),
    SNO("1시간 신적설", ""),
    SKY("하늘상태", ""),
    TMP("1시간 기온", "℃"),
    TMN("아침 최저기온", "℃"),
    TMX("낮 최고기운", "℃"),
    UUU("풍속(동서성분)", "m/s"),
    VVV("풍속(남북성분)", "m/s"),
    WAV("파고", "M"),
    VEC("풍향", "deg"),
    WSD("풍속", "m/s");

    private final String name, unit;
    public static String getCodeValue(String name, String value) {
        ForecastCategory c = ForecastCategory.valueOf(name);
        if(c == ForecastCategory.PTY) {
            switch (value) {
                case "0":
                    return "없음";
                case "1":
                    return "비";
                case "2":
                    return "비/눈";
                case "3":
                    return "눈";
                case "4":
                    return "소나기";
            }
        } else if(c == ForecastCategory.SKY) {
            switch(value) {
                case "1":
                    return "맑음";
                case "3":
                    return "구름많음";
                case "4":
                    return "흐림";
            }
        }
        return value;
    }


}
