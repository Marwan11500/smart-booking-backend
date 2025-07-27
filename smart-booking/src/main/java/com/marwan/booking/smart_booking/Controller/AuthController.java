package com.marwan.booking.smart_booking.Controller;

import com.marwan.booking.smart_booking.Dto.AuthRequest;
import com.marwan.booking.smart_booking.Dto.AuthResponse;
import com.marwan.booking.smart_booking.Dto.RegisterRequest;
import com.marwan.booking.smart_booking.Dto.TwoFARequest;
import com.marwan.booking.smart_booking.Entity.User;
import com.marwan.booking.smart_booking.Repository.UserRepository;
import com.marwan.booking.smart_booking.Service.AuthService;
import com.marwan.booking.smart_booking.Service.TwoFAService;
import com.marwan.booking.smart_booking.Service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<AuthResponse> verify2FA(@RequestBody TwoFARequest request) {
        return ResponseEntity.ok(authService.verifyTwoFactor(request));
    }

}
