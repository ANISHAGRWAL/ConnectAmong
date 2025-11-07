package com.connectamong.auth;

import com.connectamong.users.User;
import com.connectamong.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    @Value("${app.domains}")
    private List<String> allowedDomains;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (!allowedDomains.contains(request.domain())) {
            throw new IllegalArgumentException("Invalid domain: " + request.domain());
        }
        
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .domain(request.domain())
                .build();
        
        user = userRepository.save(user);
        
        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getDomain());
        return new AuthResponse(token, "Registration successful");
    }
    
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        
        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getDomain());
        return new AuthResponse(token, "Login successful");
    }
    
    public List<String> getDomains(String query) {
        if (query == null || query.isBlank()) {
            return allowedDomains;
        }
        String lowerQuery = query.toLowerCase();
        return allowedDomains.stream()
                .filter(domain -> domain.toLowerCase().contains(lowerQuery))
                .toList();
    }
}
