package com.motorny.repositories;

import com.motorny.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "SELECT l FROM Location l WHERE l.courier.id = :courierId")
    List<Location> getLocationByCourierId(@Param("courierId") Long courierId);
}
