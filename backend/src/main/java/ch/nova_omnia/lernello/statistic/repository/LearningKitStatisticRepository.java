package ch.nova_omnia.lernello.statistic.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.learningKit.model.LearningKit;
import ch.nova_omnia.lernello.statistic.model.LearningKitStatistic;
import ch.nova_omnia.lernello.user.model.User;

@Repository
public interface LearningKitStatisticRepository extends JpaRepository<LearningKitStatistic, UUID> {
    Optional<LearningKitStatistic> findByUserAndLearningKit(User user, LearningKit learningKit);
    Optional<LearningKitStatistic> findByUser_UuidAndLearningKit_Uuid(UUID userId, UUID learningKitId);
    List<LearningKitStatistic> findAllByLearningKit_Uuid(UUID id);
}
