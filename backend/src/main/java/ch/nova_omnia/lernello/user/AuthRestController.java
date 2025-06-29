package ch.nova_omnia.lernello.user;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.user.dto.request.UserLoginDTO;
import ch.nova_omnia.lernello.user.dto.response.LoggedInUserDTO;
import ch.nova_omnia.lernello.user.mapper.UserLoginMapper;
import ch.nova_omnia.lernello.user.model.User;
import ch.nova_omnia.lernello.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller for handling authentication requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthRestController {
    private final UserService userService;
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
}
