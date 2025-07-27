package com.marwan.booking.smart_booking.Controller;

import com.marwan.booking.smart_booking.Entity.Notification;
import com.marwan.booking.smart_booking.Entity.User;
import com.marwan.booking.smart_booking.Repository.UserRepository;
import com.marwan.booking.smart_booking.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Notification>> getMyNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        return ResponseEntity.ok(notificationService.getNotificationsForUser(user));
    }
}
