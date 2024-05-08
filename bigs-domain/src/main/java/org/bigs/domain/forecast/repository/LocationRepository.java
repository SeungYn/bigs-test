package org.bigs.domain.forecast.repository;

import org.bigs.domain.forecast.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query(value="SELECT * " +
            "FROM location " +
            "WHERE latitude  <= :latitudeMax " +
            "AND latitude >= :latitudeMin " +
            "AND longitude <= :longitudeMax " +
            "AND longitude >= :longitudeMin"
            , nativeQuery = true)
    List<Location> findCandidatesByLatitudeAndLongitude(@Param("latitudeMax") double latitudeMax,
                                                        @Param("latitudeMin") double latitudeMin,
                                                        @Param("longitudeMax") double longitudeMax,
                                                        @Param("longitudeMin") double longitudeMin);
}
