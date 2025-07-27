package com.marwan.booking.smart_booking.Dto;

import lombok.Data;

@Data
public class TwoFARequest {
    private String email;
    private String code;
}
