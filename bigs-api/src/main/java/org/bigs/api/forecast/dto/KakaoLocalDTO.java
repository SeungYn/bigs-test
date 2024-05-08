package org.bigs.api.forecast.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class KakaoLocalDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KakaoLocalAPIRes {
        public List<Documents> documents;
        public Meta meta;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Documents{
            private Address address;
            private String address_name;
            private String address_type;
            private RoadAddress road_address;
            private Double x;
            private Double y;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Address{
            String address_name;
            String b_code;
            String h_code;
            String main_address_no;
            String mountain_yn;
            String region_1depth_name;
            String region_2depth_name;
            String region_3depth_h_name;
            String region_3depth_name;
            String sub_address_no;
            Double x;
            Double y;
        }

        @Data
        public static class RoadAddress{
            String address_name;
            String region_1depth_name;
            String region_2depth_name;
            String region_3depth_name;
            String road_name;
            String underground_yn;
            String main_building_no;
            String sub_building_no;
            String building_name;
            String zone_no;
            String y;
            String x;

        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Meta{
            String is_end;
            String pageable_count;
            String total_count;
        }
    }

}
