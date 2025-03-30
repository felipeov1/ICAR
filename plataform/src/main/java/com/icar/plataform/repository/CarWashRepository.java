package com.icar.plataform.repository;

import com.icar.plataform.domain.model.CarWash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarWashRepository extends JpaRepository<CarWash, UUID> {

    @Modifying
    @Query("UPDATE CarWash c SET c.averageRating = :rating WHERE c.id = :id")
    void updateRating(@Param("id") UUID id, @Param("rating") Double rating);

    @Query(value = """
        SELECT * FROM car_wash c 
        WHERE ST_DWithin(
            ST_MakePoint(:longitude, :latitude)::geography,
            ST_MakePoint(c.longitude, c.latitude)::geography,
            :radius * 1000)
        """, nativeQuery = true)
    List<CarWash> findByLocationNear(
            @Param("latitude") Double lat,
            @Param("longitude") Double lon,
            @Param("radius") Double radiusKm);


}