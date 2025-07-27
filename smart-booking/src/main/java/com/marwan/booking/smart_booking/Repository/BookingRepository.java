package com.marwan.booking.smart_booking.Repository;

import com.marwan.booking.smart_booking.Entity.Booking;
import com.marwan.booking.smart_booking.Entity.Resource;
import com.marwan.booking.smart_booking.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer(User customer);
    List<Booking> findByResource(Resource resource);
    long countByStatus(String status);

    @Query("SELECT b FROM Booking b WHERE b.resource.id = :resourceId AND b.status = 'CONFIRMED' " +
            "AND ((:startTime BETWEEN b.startTime AND b.endTime) OR " +
            "(:endTime BETWEEN b.startTime AND b.endTime) OR " +
            "(b.startTime BETWEEN :startTime AND :endTime))")
    List<Booking> findOverlappingBookings(@Param("resourceId") Long resourceId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);


}
