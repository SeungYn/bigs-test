package org.bigs.domain.forecast.repository;

import org.bigs.domain.forecast.entity.ShortForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShortForecastRepository extends JpaRepository<ShortForecast, Long> {

    @Query(value="SELECT * FROM short_forecast WHERE base_date = :baseDate AND base_time = :baseTime AND category = :category AND fcst_date = :fcstDate AND fcst_time = :fcstTime AND nx = :nx AND ny = :ny"
            , nativeQuery = true)
    Optional<ShortForecast> findByShortForecast(@Param("baseDate") String baseDate, @Param("baseTime") String baseTime, @Param("category") String category, @Param("fcstDate") String fcstDate, @Param("fcstTime") String fcstTime, @Param("nx") Integer nx, @Param("ny") Integer ny);

    List<ShortForecast> findByBaseDateAndBaseTime(String baseDate, String baseTime);
}
