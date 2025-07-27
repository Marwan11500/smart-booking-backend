package com.marwan.booking.smart_booking.Service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void send(String to, String message) {
        System.out.println("📧 Sending email to " + to + ": " + message);
        // You can integrate actual email logic later
    }
}
