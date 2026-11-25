package net.aoubbad.clubsawaid.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.aoubbad.clubsawaid.dto.AuthLoginRequest;
import net.aoubbad.clubsawaid.dto.AuthRegisterRequest;
import net.aoubbad.clubsawaid.dto.AuthResponse;
import net.aoubbad.clubsawaid.dto.EmailVerificationRequest;
import net.aoubbad.clubsawaid.dto.MessageResponse;
import net.aoubbad.clubsawaid.dto.PasswordResetConfirmationRequest;
import net.aoubbad.clubsawaid.dto.PasswordResetRequest;
import net.aoubbad.clubsawaid.dto.ResendVerificationRequest;
import net.aoubbad.clubsawaid.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
        String message = authService.inscrire(request);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        AuthResponse response = authService.connecter(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody EmailVerificationRequest request) {
        authService.verifierEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Void> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        authService.renvoyerEmailVerification(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> requestReset(@Valid @RequestBody PasswordResetRequest request) {
        authService.demanderReinitialisationMotDePasse(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<Void> confirmReset(@Valid @RequestBody PasswordResetConfirmationRequest request) {
        authService.reinitialiserMotDePasse(request);
        return ResponseEntity.ok().build();
    }
}
