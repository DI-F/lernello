package ch.nova_omnia.lernello.user.service;

import ch.nova_omnia.lernello.user.model.PasswordResetToken;
import ch.nova_omnia.lernello.user.model.User;
import ch.nova_omnia.lernello.user.repository.PasswordResetTokenRepository;
import ch.nova_omnia.lernello.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    @Value("${app.reset-password-url}")
    private String resetPasswordBaseUrl;

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordResetTokenRepository tokenRepository;

    private static final int EXPIRATION_MINUTES = 60;

    public void processForgotPassword(String email) {
        User user = userRepository.findByUsername(email);

        if (user == null) return;
        // Invalidate existing tokens for the user
        tokenRepository.deleteAllByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(
            UUID.randomUUID(), token, LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES), false, user
        );
        tokenRepository.save(resetToken);

        String resetLink = resetPasswordBaseUrl + "?token=" + token;
        emailService.sendPasswordResetEmail(user, resetLink);
    }
}
