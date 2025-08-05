package ch.nova_omnia.lernello.learningUnit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.nova_omnia.lernello.block.dto.request.blocks.BlockDTO;
import ch.nova_omnia.lernello.block.service.BlockService;
import ch.nova_omnia.lernello.learningUnit.dto.request.SaveLearningUnitDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.block.model.Block;
import ch.nova_omnia.lernello.block.model.TranslatedBlock;
import ch.nova_omnia.lernello.block.repository.BlockRepository;
import ch.nova_omnia.lernello.block.repository.TranslatedBlockRepository;
import ch.nova_omnia.lernello.block.service.AIBlockService;
import ch.nova_omnia.lernello.learningKit.model.LearningKit;
import ch.nova_omnia.lernello.learningKit.repository.LearningKitRepository;
import ch.nova_omnia.lernello.learningUnit.dto.request.GenerateLearningUnitDTO;
import ch.nova_omnia.lernello.learningUnit.dto.request.UpdateLearningUnitOrderDTO;
import ch.nova_omnia.lernello.learningUnit.model.LearningUnit;
import ch.nova_omnia.lernello.learningUnit.repository.LearningUnitRepository;
import ch.nova_omnia.lernello.statistic.model.LearningUnitStatistic;
import ch.nova_omnia.lernello.statistic.repository.BlockStatisticRepository;
import ch.nova_omnia.lernello.statistic.repository.LearningUnitStatisticRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningUnitService {
    private final LearningUnitRepository learningUnitRepository;
    private final LearningKitRepository learningKitRepository;
    private final LearningUnitStatisticRepository learningUnitProgressRepository;
    private final TranslatedBlockRepository translatedBlockRepository;
    private final BlockRepository blockRepository;
    private final AIBlockService aiBlockService;
    private final BlockStatisticRepository blockProgressRepository;
    private final BlockService blockService;

    @Transactional
    public LearningUnit createLearningUnit(LearningUnit learningUnit, UUID learningKitId) {
        LearningKit learningKit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new RuntimeException("LearningKit not found with ID: " + learningKitId));
        learningUnit.setPosition(learningKit.getLearningUnits().size());
        return learningUnitRepository.save(learningUnit);
    }

    public Optional<LearningUnit> findById(UUID id) {
        return learningUnitRepository.findById(id);
    }

    public List<LearningUnit> findAll() {
        return learningUnitRepository.findAll();
    }

    @Transactional
    public LearningUnit saveLearningUnit(UUID id, SaveLearningUnitDTO learningUnit) {
        LearningUnit existingLearningUnit = learningUnitRepository.findById(id).orElseThrow(() -> new RuntimeException("Learning Unit not found with ID: " + id));
        existingLearningUnit.setName(learningUnit.name());
        existingLearningUnit.setPosition(learningUnit.position());
        existingLearningUnit.setBlocks(new ArrayList<>());
        for (BlockDTO blockDTO : learningUnit.blocks()) {
            Block block = blockService.saveBlock(existingLearningUnit, blockDTO);
            existingLearningUnit.getBlocks().add(block);
        }
        learningUnitRepository.save(existingLearningUnit);
        return existingLearningUnit;
    }

    @Transactional
    public void updateLearningUnitPosition(UUID learningKitId, UpdateLearningUnitOrderDTO updateLearningUnitOrderDTO) {
        List<UUID> learningUnitIds = updateLearningUnitOrderDTO.learningUnitUuidsInOrder();
        List<LearningUnit> learningUnits = learningUnitRepository.findLearningUnitAsc(learningKitId);

        for (int i = 0; i < learningUnitIds.size(); i++) {
            UUID expectedId = learningUnitIds.get(i);
            LearningUnit learningUnit = learningUnits.stream().filter(lu -> lu.getUuid().equals(expectedId)).findFirst().orElseThrow(() -> new RuntimeException("Learning Unit not found: " + expectedId));

            learningUnitRepository.updatePosition(i, learningUnit.getUuid());
        }
    }

    @Transactional
    public LearningUnit renameLearningUnit(UUID id, String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("New name cannot be null or blank");
        }
        LearningUnit learningUnit = learningUnitRepository.findById(id).orElseThrow(() -> new RuntimeException("Learning Unit not found with ID: " + id));
        learningUnit.setName(newName);
        return learningUnitRepository.save(learningUnit);
    }

    @Transactional
    public void deleteById(UUID id) {
        List<LearningUnitStatistic> progressesToDelete = learningUnitProgressRepository.findAllByLearningUnit_Uuid(id);
        learningUnitProgressRepository.deleteAll(progressesToDelete);
        learningUnitRepository.deleteById(id);
    }

    @Transactional
    public LearningUnit generateLearningUnitWithAI(GenerateLearningUnitDTO generateLearningUnitDTO, UUID learningUnitId) {
        LearningUnit learningUnit = getLearningUnit(learningUnitId);

        List<Block> oldBlocks = new ArrayList<>(learningUnit.getBlocks());
        List<TranslatedBlock> translations = translatedBlockRepository.findByOriginalBlockIn(oldBlocks);
        blockProgressRepository.deleteByBlockIn(oldBlocks);
        translatedBlockRepository.deleteAll(translations);

        learningUnit.getBlocks().clear();
        blockRepository.deleteAll(oldBlocks);

        List<Block> blocks = aiBlockService.generateBlocksAI(generateLearningUnitDTO);

        for (Block block : blocks) {
            block.setLearningUnit(learningUnit);
            learningUnit.getBlocks().add(block);
        }

        return learningUnitRepository.save(learningUnit);
    }

    private LearningUnit getLearningUnit(UUID id) {
        Optional<LearningUnit> optionalLearningUnit = findById(id);
        if (optionalLearningUnit.isEmpty()) {
            throw new IllegalArgumentException("Learning unit with id " + id + " not found");
        }
        return optionalLearningUnit.get();
    }
}
