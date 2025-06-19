package com.example.demo.controller.customer.api;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegistrationDTO;
import com.example.demo.security.JwtService;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RegistrationApiController {

    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public RegistrationApiController(CustomerService customerService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // ✅ Rejestracja (POST /api/auth/register)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationDTO registrationDTO) {
        customerService.registerCustomer(registrationDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        String token = jwtService.generateToken(authentication.getName());

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
