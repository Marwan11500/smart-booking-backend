package com.marwan.booking.smart_booking.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponse {
    private String token;
    private boolean requires2FA;
}
