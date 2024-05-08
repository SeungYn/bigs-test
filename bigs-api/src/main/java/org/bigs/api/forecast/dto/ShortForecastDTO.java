package org.bigs.api.forecast.dto;

import lombok.*;

import java.util.List;

public class ShortForecastDTO {
    @NoArgsConstructor
    @Getter
    public static class ShortForecastRes{
        private String baseDate;
        private String category;
        private String baseTime;
        private String fcstDate;
        private String fcstTime;

        private String fcstValue;
        private Integer nx;
        private Integer ny;

        @Builder
        private ShortForecastRes(String baseDate, String category, String baseTime, String fcstDate,
                                 String fcstTime, String fcstValue, Integer nx, Integer ny){
            this.baseDate=baseDate;
            this.category=category;
            this.baseTime=baseTime;
            this.fcstDate=fcstDate;
            this.fcstTime=fcstTime;
            this.fcstValue=fcstValue;
            this.nx=nx;
            this.ny=ny;
        }

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @ToString
    public static class ShortForecastOpenAPIRes{
        public Response response;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @ToString
        public static class Response{
            public Header header;
            public Body body;
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @ToString
        public static class Header{
            public String resultCode;
            public String resultMsg;
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @ToString
        public static class Body{
            public String dataType;
            public Items items;
            public Integer pageNo;
            public Integer numOfRows;
            public Integer totalCount;
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @ToString
        public static class Items{
            public List<Item> item;
        }


        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @ToString
        public static class Item{
            public String baseDate; // 기준날짜
            private String category; // 자료구분문자

            private String baseTime; // 기준시간
            private String fcstDate; // 예보날짜
            private String fcstTime; // 예보시간

            private String fcstValue; // 코드값
            private Integer nx; // 예보지점 x 좌표
            private Integer ny; // 예보지점 y 좌표
        }
    }
}
