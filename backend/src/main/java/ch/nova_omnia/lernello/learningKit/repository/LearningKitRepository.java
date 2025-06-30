package ch.nova_omnia.lernello.learningKit.repository;

import ch.nova_omnia.lernello.file.model.File;
import ch.nova_omnia.lernello.learningKit.model.LearningKit;
import ch.nova_omnia.lernello.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LearningKitRepository extends JpaRepository<LearningKit, UUID> {
    Page<LearningKit> findAllByTrainees_UuidAndPublishedTrue(UUID traineeId,
                                                             Pageable pageable);

    Page<LearningKit> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<LearningKit> findAllByTraineesContains(User user);

    List<LearningKit> findAllByFilesContains(File file);

    Optional<LearningKit> findByName(String name);
}
