package ch.nova_omnia.lernello.statistic.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.learningUnit.model.LearningUnit;
import ch.nova_omnia.lernello.statistic.model.LearningUnitProgress;
import ch.nova_omnia.lernello.user.model.User;

@Repository
public interface LearningUnitStatisticRepository extends JpaRepository<LearningUnitProgress, UUID> {
    Optional<LearningUnitProgress> findByUserAndLearningUnit(User user, LearningUnit learningUnit);
    Optional<LearningUnitProgress> findByUser_UuidAndLearningUnit_Uuid(UUID userId, UUID learningUnitId);
    List<LearningUnitProgress> findAllByUser_UuidAndLearningKitProgress_LearningKit_Uuid(UUID userId, UUID learningKitId);
    List<LearningUnitProgress> findAllByLearningUnit_Uuid(UUID learningUnitId);
}
