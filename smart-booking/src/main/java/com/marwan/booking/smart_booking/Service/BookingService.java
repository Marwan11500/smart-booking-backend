package com.marwan.booking.smart_booking.Service;

import com.marwan.booking.smart_booking.Dto.BookingRequest;
import com.marwan.booking.smart_booking.Entity.Booking;
import com.marwan.booking.smart_booking.Entity.Resource;
import com.marwan.booking.smart_booking.Entity.User;
import com.marwan.booking.smart_booking.Repository.BookingRepository;
import com.marwan.booking.smart_booking.Repository.ResourceRepository;
import com.marwan.booking.smart_booking.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public Booking createBooking(BookingRequest request) {
        // Get currently authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));

        // Check resource availability hours
        LocalTime start = request.getStartTime().toLocalTime();
        LocalTime end = request.getEndTime().toLocalTime();

        if (start.isBefore(resource.getAvailableFrom()) || end.isAfter(resource.getAvailableTo())) {
            throw new IllegalStateException("Booking time is outside resource availability hours");
        }

        // Check max duration
        Duration duration = Duration.between(request.getStartTime(), request.getEndTime());
        if (duration.toHours() > 2) {
            throw new IllegalStateException("Bookings cannot exceed 2 hours.");
        }

        //  Prevent overlapping bookings
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                resource.getId(), request.getStartTime(), request.getEndTime());

        if (!overlapping.isEmpty()) {
            throw new IllegalStateException("Time slot is already booked for this resource.");
        }

        Booking booking = Booking.builder()
                .customer(user)
                .resource(resource)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status("PENDING")
                .build();

        return bookingRepository.save(booking);
    }

    public List<Booking> getMyBookings() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        return bookingRepository.findByCustomer(user);
    }

    @Transactional
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!"PENDING".equals(booking.getStatus())) {
            throw new IllegalStateException("Only pending bookings can be confirmed");
        }

        //  check again for overlapping confirmed bookings before confirming
        List<Booking> overlaps = bookingRepository.findOverlappingBookings(
                booking.getResource().getId(), booking.getStartTime(), booking.getEndTime());

        if (!overlaps.isEmpty()) {
            throw new IllegalStateException("Time slot already taken");
        }

        booking.setStatus("CONFIRMED");
        Booking confirmed = bookingRepository.save(booking);

        // Send notification
        notificationService.sendNotification(
                booking.getCustomer(),
                "Your booking for " + booking.getResource().getName() + " has been confirmed!"
        );

        return confirmed;
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Check ownership
        if (!booking.getCustomer().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized to cancel this booking");
        }

        // Allow cancel only if still pending
        if (!"PENDING".equals(booking.getStatus())) {
            throw new IllegalStateException("Only pending bookings can be cancelled");
        }

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // Send notification
        notificationService.sendNotification(
                user,
                "Your booking for " + booking.getResource().getName() + " has been cancelled."
        );
    }

    public List<Booking> getBookingsForUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return bookingRepository.findByCustomer(user);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsForResource(Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));

        return bookingRepository.findByResource(resource);
    }

    public List<Booking> getBookingsByResourceWithFilters(Long resourceId, String status,
                                                          LocalDateTime from, LocalDateTime to) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));

        List<Booking> bookings = bookingRepository.findByResource(resource);

        if (status != null) {
            bookings = bookings.stream()
                    .filter(b -> b.getStatus().equalsIgnoreCase(status))
                    .toList();
        }

        if (from != null && to != null) {
            bookings = bookings.stream()
                    .filter(b -> !b.getStartTime().isAfter(to) && !b.getEndTime().isBefore(from))
                    .toList();
        }

        return bookings;
    }

    @Transactional
    public Booking approveBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getStatus().equals("PENDING")) {
            throw new IllegalStateException("Only pending bookings can be approved");
        }

        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking rejectBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getStatus().equals("PENDING")) {
            throw new IllegalStateException("Only pending bookings can be rejected");
        }

        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }

    public Map<String, List<Booking>> getUserBookingHistory() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Booking> all = bookingRepository.findByCustomer(user);

        LocalDateTime now = LocalDateTime.now();

        Map<String, List<Booking>> result = new HashMap<>();
        result.put("upcoming", new ArrayList<>());
        result.put("past", new ArrayList<>());
        result.put("cancelled", new ArrayList<>());

        for (Booking booking : all) {
            if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
                result.get("cancelled").add(booking);
            } else if (booking.getEndTime().isBefore(now)) {
                result.get("past").add(booking);
            } else {
                result.get("upcoming").add(booking);
            }
        }

        return result;
    }

    public Map<String, Object> getBookingStats() {
        long total = bookingRepository.count();
        long confirmed = bookingRepository.countByStatus("CONFIRMED");
        long pending = bookingRepository.countByStatus("PENDING");
        long cancelled = bookingRepository.countByStatus("CANCELLED");

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("confirmed", confirmed);
        stats.put("pending", pending);
        stats.put("cancelled", cancelled);

        return stats;
    }


}
