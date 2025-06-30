package ch.nova_omnia.lernello.model.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ch.nova_omnia.lernello.PostgresTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ch.nova_omnia.lernello.user.model.Role;
import ch.nova_omnia.lernello.user.model.User;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(PostgresTestConfig.class)
public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "Frodo", "Baggins", "password123", "en", Role.TRAINEE);
    }

    @Test
    void testConstructor() {
        assertEquals("test@example.com", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("en", user.getLocale());
        assertEquals(Role.TRAINEE, user.getRole());
    }


    @Test
    void testChangedPasswordDefault() {
        assertFalse(user.isChangedPassword());
    }

    @Test
    void testRoleCannotBeChanged() {
        Role initialRole = user.getRole();
        // Since the role is final, there is no setter for it.
        // We can only verify that the role remains unchanged.
        assertEquals(Role.TRAINEE, initialRole);

        // Verify that the role is still the same
        assertEquals(initialRole, user.getRole());
    }

}
