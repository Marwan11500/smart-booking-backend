package com.marwan.booking.smart_booking.Service;

import com.marwan.booking.smart_booking.Dto.AuthRequest;
import com.marwan.booking.smart_booking.Dto.AuthResponse;
import com.marwan.booking.smart_booking.Dto.RegisterRequest;
import com.marwan.booking.smart_booking.Dto.TwoFARequest;
import com.marwan.booking.smart_booking.Entity.Role;
import com.marwan.booking.smart_booking.Entity.User;
import com.marwan.booking.smart_booking.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email already in use");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole()))
                .twoFactorEnabled(true)
                .twoFactorVerified(false)
                .build();

        userRepository.save(user);
        return new AuthResponse(null,false);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        System.out.println("User Email: " + user.getEmail());
        System.out.println("2FA Enabled: " + user.isTwoFactorEnabled());

        if (user.isTwoFactorEnabled()) {
            String code = "super1";
            user.setTwoFactorCode(code);
            userRepository.save(user);
            //emailService.send(user.getEmail(), "Your 2FA code is: " + code);
            return new AuthResponse(null, true);
        }


        String token = generateTokenForUser(user);
        return new AuthResponse(token, false);
    }


    @Transactional
    public AuthResponse verifyTwoFactor(TwoFARequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!"super1".equals(user.getTwoFactorCode())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid 2FA code");
        }

        user.setTwoFactorVerified(true);
        user.setTwoFactorCode(null);
        userRepository.save(user);

        String token = generateTokenForUser(user);
        return new AuthResponse(token, false);
    }

    public String generateTokenForUser(User user) {
        return jwtService.generateToken(new UserDetailsImpl(user));
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        return String.valueOf(100000 + random.nextInt(900000));
    }
}
