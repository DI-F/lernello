package ch.nova_omnia.lernello.statistic.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.learningUnit.model.LearningUnit;
import ch.nova_omnia.lernello.statistic.model.LearningUnitStatistic;
import ch.nova_omnia.lernello.user.model.User;

@Repository
public interface LearningUnitStatisticRepository extends JpaRepository<LearningUnitStatistic, UUID> {
    Optional<LearningUnitStatistic> findByUserAndLearningUnit(User user, LearningUnit learningUnit);
    Optional<LearningUnitStatistic> findByUser_UuidAndLearningUnit_Uuid(UUID userId, UUID learningUnitId);
    List<LearningUnitStatistic> findAllByUser_UuidAndLearningKitProgress_LearningKit_Uuid(UUID userId, UUID learningKitId);
    List<LearningUnitStatistic> findAllByLearningUnit_Uuid(UUID learningUnitId);
}
