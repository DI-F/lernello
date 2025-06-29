package ch.nova_omnia.lernello.statistic.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.nova_omnia.lernello.block.model.Block;
import ch.nova_omnia.lernello.statistic.model.block.BlockStatistic;
import ch.nova_omnia.lernello.user.model.User;

@Repository
public interface BlockStatisticRepository extends JpaRepository<BlockStatistic, UUID> {
    Optional<BlockStatistic> findByUserAndBlock(User user, Block block);

    Optional<BlockStatistic> findByUser_UuidAndBlock_Uuid(UUID userId, UUID blockId);

    List<BlockStatistic> findByBlock_Uuid(UUID blockId);

    List<BlockStatistic> findByLearningUnitProgress_Uuid(UUID learningUnitProgressUuid);

    void deleteByBlockIn(List<Block> blocks);
}
