package ch.nova_omnia.lernello.folder.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.folder.model.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, UUID> {
}
