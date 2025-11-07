package com.connectamong.auth;

import com.connectamong.users.User;
import com.connectamong.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtService jwtService;
    
    @InjectMocks
    private AuthService authService;
    
    @Test
    void testLoginSuccess() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String hashedPassword = "$2a$10$hashedpassword";
        
        User user = User.builder()
                .id(1L)
                .email(email)
                .passwordHash(hashedPassword)
                .domain("DEVELOPER")
                .build();
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(true);
        when(jwtService.generateToken(1L, email, "DEVELOPER")).thenReturn("jwt-token");
        
        LoginRequest request = new LoginRequest(email, password);
        
        // Act
        AuthResponse response = authService.login(request);
        
        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        assertEquals("Login successful", response.message());
        
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, hashedPassword);
        verify(jwtService).generateToken(1L, email, "DEVELOPER");
    }
}
