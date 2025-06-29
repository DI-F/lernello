package ch.nova_omnia.lernello.user.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.user.model.Role;
import ch.nova_omnia.lernello.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);

    List<User> findAllByRoleOrderBySurnameAscNameAsc(Role role);

    User findByUuid(UUID uuid);

    boolean existsByUsername(String email);
}
