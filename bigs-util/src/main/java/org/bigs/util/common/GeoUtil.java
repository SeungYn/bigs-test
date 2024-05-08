package org.bigs.util.common;

public class GeoUtil {

    // 지구의 반지름 (미터)
    private static final double EARTH_RADIUS = 6371e3; // meters

    // 두 지점 간의 거리를 계산하는 메서드
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    // 1.6km 범위 내의 최대 및 최소 위도를 계산하는 메서드
    public static double[] calculateLatitudeRange(double latitude) {
        double distance = 1.6 * 1000; // 1.6km를 미터 단위로 변환
        double latitudeDelta = Math.toDegrees(distance / EARTH_RADIUS);
        return new double[] {latitude - latitudeDelta, latitude + latitudeDelta};
    }

    // 1.6km 범위 내의 최대 및 최소 경도를 계산하는 메서드
    public static double[] calculateLongitudeRange(double latitude, double longitude) {
        double distance = 1.6 * 1000; // 1.6km를 미터 단위로 변환
        double longitudeDelta = Math.toDegrees(distance / (EARTH_RADIUS * Math.cos(Math.toRadians(latitude))));
        return new double[] {longitude - longitudeDelta, longitude + longitudeDelta};
    }


}