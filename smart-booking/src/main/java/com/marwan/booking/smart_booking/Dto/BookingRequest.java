package com.marwan.booking.smart_booking.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    private Long resourceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
