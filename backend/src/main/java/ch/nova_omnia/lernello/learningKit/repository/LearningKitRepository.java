package ch.nova_omnia.lernello.learningKit.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.file.model.File;
import ch.nova_omnia.lernello.learningKit.model.LearningKit;
import ch.nova_omnia.lernello.user.model.User;

@Repository
public interface LearningKitRepository extends JpaRepository<LearningKit, UUID> {
    Page<LearningKit> findAllByTrainees_UuidAndPublishedTrue(UUID traineeId,
                                                                 Pageable pageable);

    Page<LearningKit> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<LearningKit> findAllByTraineesContains(User user);

    List<LearningKit> findAllByFilesContains(File file);
}
