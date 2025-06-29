package ch.nova_omnia.lernello.block.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.nova_omnia.lernello.block.model.Block;
import ch.nova_omnia.lernello.block.model.TranslatedBlock;

public interface TranslatedBlockRepository extends JpaRepository<TranslatedBlock, UUID> {
    List<TranslatedBlock> findByOriginalBlock(Block originalBlock);

    List<TranslatedBlock> findByOriginalBlockIn(List<Block> blocks);
}
