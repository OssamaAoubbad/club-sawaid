package net.aoubbad.clubsawaid.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.aoubbad.clubsawaid.repository.EmailVerificationTokenRepository;
import net.aoubbad.clubsawaid.repository.PasswordResetTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupService {

    private final EmailVerificationTokenRepository emailTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void nettoyerTokensExpires() {
        LocalDateTime now = LocalDateTime.now();

        emailTokenRepository.supprimerTokensExpires(now);
        passwordResetTokenRepository.supprimerTokensExpires(now);

        log.info("[TokenCleanup] Expired tokens removed at {}", now);
    }
}
