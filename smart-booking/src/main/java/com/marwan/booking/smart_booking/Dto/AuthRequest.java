package com.marwan.booking.smart_booking.Dto;


import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
