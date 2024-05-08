package org.bigs.domain.forecast.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name="location")
public class Location {
    @Id
    @Column(name="administrative_district_code")
    private Long administrativeDistrictCode;

    @Column(name="classification")
    private String classification;

    @Column(name="level1")
    private String level1;

    @Column(name="level2")
    private String level2;

    @Column(name="level3")
    private String level3;
    //latitude_hour,latitude_minutes,latitude_seconds,longitude,latitude
    @Column(name="grid_x")
    private Integer gridX;

    @Column(name="grid_y")
    private Integer gridY;

    @Column(name="longitude_hour")
    private Integer longitudeHour;

    @Column(name="longitude_minutes")
    private Integer longitudeMinutes;

    @Column(name="longitude_seconds")
    private Double longitudeSeconds;

    @Column(name="latitude_hour")
    private Integer latitudeHour;

    @Column(name="latitude_minutes")
    private Integer latitudeMinutes;

    @Column(name="latitude_seconds")
    private Double latitudeSeconds;

    @Column(name="longitude")
    private Double longitude;

    @Column(name="latitude")
    private Double latitude;
}
