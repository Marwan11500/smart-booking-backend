package com.marwan.booking.smart_booking.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TwoFAService {
    private final EmailService emailService;
    private final Map<String, String> userCodes = new HashMap<>();

    public void sendCodeToEmail(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        userCodes.put(email, code);
        emailService.send(email, "Your verification code is: " + code);
    }

    public boolean verifyCode(String email, String code) {
        return code.equals(userCodes.get(email));
    }
}
