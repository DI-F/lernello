package ch.nova_omnia.lernello.user.repository;

import ch.nova_omnia.lernello.user.model.PasswordResetToken;
import ch.nova_omnia.lernello.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    void deleteAllByUser(User user); // Deletes all tokens associated with a user
}
