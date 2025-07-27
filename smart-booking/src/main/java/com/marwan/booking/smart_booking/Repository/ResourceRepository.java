package com.marwan.booking.smart_booking.Repository;

import com.marwan.booking.smart_booking.Entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByOwnerId(Long ownerID);
    @Query("SELECT r FROM Resource r " +
            "WHERE (:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:type IS NULL OR LOWER(r.type) LIKE LOWER(CONCAT('%', :type, '%')))")
    List<Resource> searchResources(
            @Param("name") String name,
            @Param("type") String type
    );

}
