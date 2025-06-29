package ch.nova_omnia.lernello.file.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.nova_omnia.lernello.file.model.File;

public interface FileRepository extends JpaRepository<File, UUID> {
    Optional<File> findByName(String name);
}