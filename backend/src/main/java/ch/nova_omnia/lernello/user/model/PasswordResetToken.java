package ch.nova_omnia.lernello.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "password_reset_tokens")
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    private UUID uuid;

    private String token;
    private LocalDateTime expiresAt;
    private boolean used = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
