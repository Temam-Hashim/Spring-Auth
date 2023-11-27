package com.security.auth.controller;


import com.security.auth.DTO.AuthenticationRequest;
import com.security.auth.config.JwtUtils;
import com.security.auth.entity.Users;
import com.security.auth.exception.BadRequestException;
import com.security.auth.exception.NotFoundException;
import com.security.auth.exception.UnAuthorizedException;
import com.security.auth.repositary.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class authController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) {

        try {
            // Attempt authentication
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Retrieve user by email
            var authenticatedUser = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("No User with this Email"));

            // Check if the entered password matches the user's password
            if (!passwordEncoder.matches(request.getPassword(), authenticatedUser.getPassword())) {
                throw new UnAuthorizedException("Invalid Credentials Provided");
            }


            // Generate a refresh token
            String refreshToken = jwtUtils.generateRefreshToken(authenticatedUser);
            // Generate JWT token
            var jwtToken = jwtUtils.generateToken(authenticatedUser);

            // Build response
            Map<String, String> map = new HashMap<>();
            map.put("accessToken", jwtToken);
            map.put("refreshToken", refreshToken);

            // Return successful response
            return ResponseEntity.ok(map);
        } catch (AuthenticationException e) {
            // Handle authentication failure
            throw new UnAuthorizedException("Invalid Email or Password");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (StringUtils.isBlank(refreshToken)) {
            throw new BadRequestException("Refresh token is missing.");
        }

        String username = jwtUtils.extractUsername(refreshToken);
        var userDetails = userRepository.findByEmail(username).orElseThrow(()->new NotFoundException("No user found with this email"));

        if (jwtUtils.isRefreshTokenValid(refreshToken, userDetails)) {
            String newAccessToken = jwtUtils.generateToken(userDetails);
            Map<String, String> map = new HashMap<>();
            map.put("accessToken", newAccessToken);
            return ResponseEntity.ok(map);
        } else {
            throw new UnAuthorizedException("Invalid or expired refresh token.");
        }
    }



}
