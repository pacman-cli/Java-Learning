package com.pacman.safetymap.modules.place.repository;

import com.pacman.safetymap.modules.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByType(String type);

    List<Place> findByNameContainingIgnoreCase(String name);


    //    ✅ This uses the Haversine formula in SQL to calculate distance (in km).
//            ✅ 6371 = Earth’s radius in kilometers.
    @Query(value = """
              SELECT * FROM places p
              WHERE (
              6371 * acos (
              cos(radians(:lat)) * cos(radians(p.latitude)) *
              cos(radians(p.longitude) - radians(:lon)) +
              sin(radians(:lat)) * sin(radians(p.latitude))
              )
             ) < :radius
            """, nativeQuery = true
    )
    List<Place> findNearbyPlaces(
            @Param("lat") double latitude,
            @Param("lon") double longitude,
            @Param("radius") double radiusInKm
    );
}
