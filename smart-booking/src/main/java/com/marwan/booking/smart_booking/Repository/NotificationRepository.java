package com.marwan.booking.smart_booking.Repository;

import com.marwan.booking.smart_booking.Entity.Notification;
import com.marwan.booking.smart_booking.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
