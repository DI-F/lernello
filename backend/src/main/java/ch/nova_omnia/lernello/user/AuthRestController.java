package ch.nova_omnia.lernello.user;

import ch.nova_omnia.lernello.user.dto.request.ForgotPasswordDTO;
import ch.nova_omnia.lernello.user.dto.request.UserLoginDTO;
import ch.nova_omnia.lernello.user.dto.response.LoggedInUserDTO;
import ch.nova_omnia.lernello.user.mapper.UserLoginMapper;
import ch.nova_omnia.lernello.user.model.User;
import ch.nova_omnia.lernello.user.service.PasswordResetService;
import ch.nova_omnia.lernello.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthRestController {
    private final UserService userService;
    private final PasswordResetService passwordResetService;
    private final UserLoginMapper userLoginMapper;

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param userLoginDTO The user to authenticate.
     * @return The JWT token.
     */
    @PostMapping("/signin")
    public @Valid LoggedInUserDTO signin(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        User authenticatedUser = userService.authenticate(userLoginDTO.username(), userLoginDTO.password());
        return userLoginMapper.toDTO(authenticatedUser);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordDTO request) {
        passwordResetService.processForgotPassword(request.getEmail());

        // Always same response, no indication if user exists, for user-leakage prevention
        return ResponseEntity.ok().build();
    }
}
