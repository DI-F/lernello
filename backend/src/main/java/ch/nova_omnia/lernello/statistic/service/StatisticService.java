package ch.nova_omnia.lernello.statistic.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ch.nova_omnia.lernello.block.model.Block;
import ch.nova_omnia.lernello.block.model.BlockType;
import ch.nova_omnia.lernello.block.model.TheoryBlock;
import ch.nova_omnia.lernello.block.model.TranslatedBlock;
import ch.nova_omnia.lernello.block.model.quiz.MultipleChoiceBlock;
import ch.nova_omnia.lernello.block.model.quiz.QuestionBlock;
import ch.nova_omnia.lernello.block.repository.BlockRepository;
import ch.nova_omnia.lernello.block.service.AIBlockService;
import ch.nova_omnia.lernello.learningKit.model.LearningKit;
import ch.nova_omnia.lernello.learningKit.repository.LearningKitRepository;
import ch.nova_omnia.lernello.learningUnit.model.LearningUnit;
import ch.nova_omnia.lernello.learningUnit.repository.LearningUnitRepository;
import ch.nova_omnia.lernello.statistic.dto.request.CheckMultipleChoiceAnswerDTO;
import ch.nova_omnia.lernello.statistic.dto.request.CheckQuestionAnswerDTO;
import ch.nova_omnia.lernello.statistic.dto.request.LearningKitOpened;
import ch.nova_omnia.lernello.statistic.dto.request.LearningUnitOpenedDTO;
import ch.nova_omnia.lernello.statistic.dto.request.TheoryBlockViewedDTO;
import ch.nova_omnia.lernello.statistic.dto.response.MultipleChoiceAnswerValidationResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.QuestionAnswerValidationResDTO;
import ch.nova_omnia.lernello.statistic.model.LearningKitStatistic;
import ch.nova_omnia.lernello.statistic.model.LearningUnitStatistic;
import ch.nova_omnia.lernello.statistic.model.block.BlockStatistic;
import ch.nova_omnia.lernello.statistic.model.block.TheoryBlockStatistic;
import ch.nova_omnia.lernello.statistic.model.block.quiz.MultipleChoiceBlockStatistic;
import ch.nova_omnia.lernello.statistic.model.block.quiz.QuestionBlockStatistic;
import ch.nova_omnia.lernello.statistic.repository.BlockStatisticRepository;
import ch.nova_omnia.lernello.statistic.repository.LearningKitStatisticRepository;
import ch.nova_omnia.lernello.statistic.repository.LearningUnitStatisticRepository;
import ch.nova_omnia.lernello.user.model.User;
import ch.nova_omnia.lernello.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final LearningKitStatisticRepository learningKitProgressRepository;
    private final LearningUnitStatisticRepository learningUnitProgressRepository;
    private final BlockStatisticRepository blockProgressRepository;
    private final LearningKitRepository learningKitRepository;
    private final LearningUnitRepository learningUnitRepository;
    private final BlockRepository blockRepository;
    private final UserService userService;
    private final AIBlockService aiBlockService;

    @Transactional
    public LearningKitStatistic markLearningKitOpened(LearningKitOpened dto, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        updateLearningKitProgressPercentage(
                getOrCreateLearningKitProgress(user, learningKitRepository.findById(dto.learningKitId()).orElseThrow(() -> new IllegalArgumentException("LearningKit not found with id: " + dto.learningKitId()))));
        LearningKit learningKit = learningKitRepository.findById(dto.learningKitId()).orElseThrow(() -> new IllegalArgumentException("LearningKit not found with id: " + dto.learningKitId()));

        LearningKitStatistic progress = getOrCreateLearningKitProgress(user, learningKit);
        if (!progress.isOpened()) {
            progress.setOpened(true);
        }
        progress.setLastOpenedAt(ZonedDateTime.now());
        learningKitProgressRepository.save(progress);
        return progress;
    }

    @Transactional
    public LearningUnitStatistic markLearningUnitOpened(LearningUnitOpenedDTO dto, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        LearningUnit learningUnit = learningUnitRepository.findById(dto.learningUnitId()).orElseThrow(() -> new IllegalArgumentException("LearningUnit not found with id: " + dto.learningUnitId()));

        LearningKit learningKit = learningUnit.getLearningKit();
        if (learningKit == null) {
            throw new IllegalStateException("LearningUnit " + learningUnit.getUuid() + " is not associated with a LearningKit.");
        }

        LearningKitStatistic kitProgress = getOrCreateLearningKitProgress(user, learningKit);
        if (!kitProgress.isOpened()) {
            kitProgress.setOpened(true);
        }
        kitProgress.setLastOpenedAt(ZonedDateTime.now());

        LearningUnitStatistic unitProgress = getOrCreateLearningUnitProgress(user, learningUnit, kitProgress);
        if (!unitProgress.isOpened()) {
            unitProgress.setOpened(true);
        }
        unitProgress.setLastOpenedAt(ZonedDateTime.now());

        updateLearningUnitProgressPercentage(unitProgress);
        updateLearningKitProgressPercentage(kitProgress);

        return unitProgress;
    }

    @Transactional
    public MultipleChoiceAnswerValidationResDTO checkMultipleChoiceAnswer(CheckMultipleChoiceAnswerDTO dto, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Block block = blockRepository.findById(dto.blockId()).orElseThrow(() -> new IllegalArgumentException("Block not found with id: " + dto.blockId()));

        MultipleChoiceBlock mcBlock;
        if (!(block instanceof MultipleChoiceBlock)) {
            if (block instanceof TranslatedBlock translatedBlock && block.getType().equals(BlockType.MULTIPLE_CHOICE)) {
                mcBlock = (MultipleChoiceBlock) translatedBlock.getOriginalBlock();
            } else {
                throw new IllegalArgumentException("Block is not a Multiple Choice Block");
            }
        } else {
            mcBlock = (MultipleChoiceBlock) block;
        }

        LearningUnit learningUnit = mcBlock.getLearningUnit();
        if (learningUnit == null) {
            throw new IllegalStateException("Block " + mcBlock.getUuid() + " is not associated with a Learning Unit");
        }
        LearningUnitStatistic unitProgress = getOrCreateLearningUnitProgress(user, learningUnit, getOrCreateLearningKitProgress(user, learningUnit.getLearningKit()));
        BlockStatistic blockProgress = getOrCreateBlockProgress(user, mcBlock, unitProgress);

        if (blockProgress instanceof MultipleChoiceBlockStatistic mcProgress) {
            mcProgress.setLastAnswers(new ArrayList<>(dto.answers()));
        }

        boolean isCorrect = new HashSet<>(dto.answers()).equals(new HashSet<>(mcBlock.getCorrectAnswers()));
        if (block instanceof TranslatedBlock translatedBlock && block.getType().equals(BlockType.MULTIPLE_CHOICE)) {
            isCorrect = new HashSet<>(dto.answers()).equals(new HashSet<>(translatedBlock.getCorrectAnswers()));
        }

        if (blockProgress instanceof MultipleChoiceBlockStatistic mcProgress) {
            mcProgress.setIsCorrect(isCorrect);
            mcProgress.setLastAnswers(new ArrayList<>(dto.answers()));
        }

        blockProgressRepository.save(blockProgress);
        updateLearningUnitProgressPercentage(unitProgress);
        if (unitProgress.getLearningKitProgress() != null) {
            updateLearningKitProgressPercentage(unitProgress.getLearningKitProgress());
        }
        return new MultipleChoiceAnswerValidationResDTO(dto.blockId(), isCorrect);
    }

    @Transactional
    public QuestionAnswerValidationResDTO checkQuestionAnswer(CheckQuestionAnswerDTO dto, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Block block = blockRepository.findById(dto.blockId()).orElseThrow(() -> new IllegalArgumentException("Block not found with id: " + dto.blockId()));

        QuestionBlock qBlock;
        if (!(block instanceof QuestionBlock)) {
            if (block instanceof TranslatedBlock translatedBlock && block.getType().equals(BlockType.QUESTION)) {
                qBlock = (QuestionBlock) translatedBlock.getOriginalBlock();
            } else {
                throw new IllegalArgumentException("Block is not a Question Block");
            }
        } else {
            qBlock = (QuestionBlock) block;
        }

        LearningUnit learningUnit = qBlock.getLearningUnit();
        if (learningUnit == null) {
            throw new IllegalStateException("Block " + qBlock.getUuid() + " is not associated with a Learning Unit");
        }
        LearningUnitStatistic unitProgress = getOrCreateLearningUnitProgress(user, learningUnit, getOrCreateLearningKitProgress(user, learningUnit.getLearningKit()));
        BlockStatistic blockProgress = getOrCreateBlockProgress(user, qBlock, unitProgress);

        if (blockProgress instanceof QuestionBlockStatistic mcProgress) {
            mcProgress.setLastAnswer(dto.answer());
        }

        boolean isCorrect = aiBlockService.checkQuestionAnswerWithAI(dto.answer(), qBlock.getExpectedAnswer());

        if (blockProgress instanceof QuestionBlockStatistic qProgress) {
            qProgress.setIsCorrect(isCorrect);
            qProgress.setLastAnswer(dto.answer());
        }

        blockProgressRepository.save(blockProgress);
        updateLearningUnitProgressPercentage(unitProgress);
        if (unitProgress.getLearningKitProgress() != null) {
            updateLearningKitProgressPercentage(unitProgress.getLearningKitProgress());
        }
        return new QuestionAnswerValidationResDTO(dto.blockId(), isCorrect);
    }

    @Transactional
    public TheoryBlockStatistic markTheoryBlockViewed(TheoryBlockViewedDTO dto, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Block block = blockRepository.findById(dto.blockId()).orElseThrow(() -> new IllegalArgumentException("Block not found with id: " + dto.blockId()));

        TheoryBlock theoryBlock;
        if (!(block instanceof TheoryBlock)) {
            if (block instanceof TranslatedBlock translatedBlock && block.getType().equals(BlockType.THEORY)) {
                theoryBlock = (TheoryBlock) translatedBlock.getOriginalBlock();
            } else {
                throw new IllegalArgumentException("Block is not a Theory Block");
            }
        } else {
            theoryBlock = (TheoryBlock) block;
        }

        LearningUnit learningUnit = theoryBlock.getLearningUnit();
        if (learningUnit == null) {
            throw new IllegalStateException("Block " + theoryBlock.getUuid() + " is not associated with a Learning Unit");
        }

        LearningKit learningKit = learningUnit.getLearningKit();
        if (learningKit == null) {
            throw new IllegalStateException("LearningUnit " + learningUnit.getUuid() + " is not associated with a LearningKit.");
        }

        LearningKitStatistic kitProgress = getOrCreateLearningKitProgress(user, learningKit);
        LearningUnitStatistic unitProgress = getOrCreateLearningUnitProgress(user, learningUnit, kitProgress);

        BlockStatistic blockProgress = getOrCreateBlockProgress(user, theoryBlock, unitProgress);

        if (blockProgress instanceof TheoryBlockStatistic theoryBlockProgress) {
            theoryBlockProgress.setIsViewed(true);
        }

        blockProgressRepository.save(blockProgress);

        updateLearningUnitProgressPercentage(unitProgress);
        if (unitProgress.getLearningKitProgress() != null) {
            updateLearningKitProgressPercentage(unitProgress.getLearningKitProgress());
        }

        if (blockProgress instanceof TheoryBlockStatistic) {
            return (TheoryBlockStatistic) blockProgress;
        }
        throw new IllegalStateException("BlockProgress is not of type TheoryBlockProgress");
    }

    public LearningKitStatistic getLearningKitProgress(UUID learningKitId, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Optional<LearningKitStatistic> optProgress = learningKitProgressRepository.findByUser_UuidAndLearningKit_Uuid(user.getUuid(), learningKitId);

        return optProgress.orElseGet(() -> {
            LearningKit kit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new IllegalArgumentException("LearningKit not found with id: " + learningKitId));
            return new LearningKitStatistic(user, kit);
        });
    }

    @Transactional
    public List<LearningKitStatistic> getLearningKitProgressForAllTrainees(UUID learningKitId) {
        LearningKit learningKit = learningKitRepository.findById(learningKitId).orElseThrow(() -> new IllegalArgumentException("LearningKit not found with id: " + learningKitId));

        List<User> trainees = learningKit.getTrainees();
        List<LearningKitStatistic> allProgresses = new ArrayList<>();

        for (User trainee : trainees) {
            LearningKitStatistic progress = getOrCreateLearningKitProgress(trainee, learningKit);
            allProgresses.add(progress);
        }
        return allProgresses;
    }

    public LearningUnitStatistic getLearningUnitProgress(UUID learningUnitId, UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Optional<LearningUnitStatistic> optProgress = learningUnitProgressRepository.findByUser_UuidAndLearningUnit_Uuid(user.getUuid(), learningUnitId);

        return optProgress.orElseGet(() -> {
            LearningUnit unit = learningUnitRepository.findById(learningUnitId).orElseThrow(() -> new IllegalArgumentException("LearningUnit not found with id: " + learningUnitId));
            return new LearningUnitStatistic(user, unit);
        });
    }

    private LearningKitStatistic getOrCreateLearningKitProgress(User user, LearningKit learningKit) {
        return learningKitProgressRepository.findByUser_UuidAndLearningKit_Uuid(user.getUuid(), learningKit.getUuid()).orElseGet(() -> {
            LearningKitStatistic newKitProgress = new LearningKitStatistic(user, learningKit);
            LearningKitStatistic savedKitProgress = learningKitProgressRepository.save(newKitProgress);

            for (LearningUnit learningUnit : learningKit.getLearningUnits()) {
                getOrCreateLearningUnitProgress(user, learningUnit, savedKitProgress);
            }
            return savedKitProgress;
        });
    }

    private LearningUnitStatistic getOrCreateLearningUnitProgress(User user, LearningUnit learningUnit, LearningKitStatistic kitProgress) {
        return learningUnitProgressRepository.findByUser_UuidAndLearningUnit_Uuid(user.getUuid(), learningUnit.getUuid()).orElseGet(() -> {
            LearningUnitStatistic newProgress = new LearningUnitStatistic(user, learningUnit);

            if (kitProgress != null) {
                newProgress.setLearningKitProgress(kitProgress);
            }

            LearningUnitStatistic savedUnitProgress = learningUnitProgressRepository.save(newProgress);

            for (Block block : learningUnit.getBlocks()) {
                getOrCreateBlockProgress(user, block, savedUnitProgress);
            }
            if (kitProgress != null) {
                kitProgress.addLearningUnitProgress(savedUnitProgress);
            }
            return savedUnitProgress;
        });
    }

    private BlockStatistic getOrCreateBlockProgress(User user, Block block, LearningUnitStatistic unitProgress) {
        final Block originalBlock = (block instanceof TranslatedBlock translatedBlock) ? translatedBlock.getOriginalBlock() : block;

        return blockProgressRepository.findByUser_UuidAndBlock_Uuid(user.getUuid(), originalBlock.getUuid()).orElseGet(() -> {
            BlockStatistic newBlockProgress = switch (originalBlock) {
                case TheoryBlock _ -> new TheoryBlockStatistic(user, (TheoryBlock) originalBlock, unitProgress);
                case MultipleChoiceBlock _ -> new MultipleChoiceBlockStatistic(user, (MultipleChoiceBlock) originalBlock, unitProgress);
                case QuestionBlock _ -> new QuestionBlockStatistic(user, (QuestionBlock) originalBlock, unitProgress);
                default -> throw new IllegalArgumentException("Unsupported block type for progress tracking: " + originalBlock.getName());
            };
            unitProgress.addBlockProgress(newBlockProgress);
            return blockProgressRepository.save(newBlockProgress);
        });
    }

    private void updateLearningUnitProgressPercentage(LearningUnitStatistic unitProgress) {
        LearningUnit learningUnit = unitProgress.getLearningUnit();
        if (learningUnit == null) return;

        List<Block> blocksInUnit = learningUnit.getBlocks();
        if (blocksInUnit.isEmpty()) {
            unitProgress.setProgressPercentage(100);
            if (!unitProgress.isCompleted()) {
                unitProgress.setCompleted(true);
                unitProgress.setCompletedAt(ZonedDateTime.now());
            }
            learningUnitProgressRepository.save(unitProgress);
            return;
        }

        List<BlockStatistic> currentBlockProgresses = blockProgressRepository.findByLearningUnitProgress_Uuid(unitProgress.getUuid());

        long completedBlocksCount = currentBlockProgresses.stream().filter(this::isBlockProgressConsideredComplete).count();

        int percentage = (int) Math.round(((double) completedBlocksCount / blocksInUnit.size()) * 100);
        unitProgress.setProgressPercentage(percentage);

        if (percentage == 100 && !unitProgress.isCompleted()) {
            unitProgress.setCompleted(true);
            unitProgress.setCompletedAt(ZonedDateTime.now());
        } else if (percentage < 100 && unitProgress.isCompleted()) {
            unitProgress.setCompleted(false);
            unitProgress.setCompletedAt(null);
        }
        learningUnitProgressRepository.save(unitProgress);
    }

    private void updateLearningKitProgressPercentage(LearningKitStatistic kitProgress) {
        LearningKit learningKit = kitProgress.getLearningKit();
        if (learningKit == null) return;

        List<LearningUnit> learningUnitsInKit = learningKit.getLearningUnits();
        if (learningUnitsInKit.isEmpty()) {
            kitProgress.setProgressPercentage(100);
            if (!kitProgress.isCompleted()) {
                kitProgress.setCompleted(true);
                kitProgress.setCompletedAt(ZonedDateTime.now());
            }
            learningKitProgressRepository.save(kitProgress);
            return;
        }

        long totalBlocksInKit = 0;
        long totalCompletedBlocksInKit = 0;

        User user = kitProgress.getUser();

        for (LearningUnit learningUnit : learningUnitsInKit) {
            totalBlocksInKit += learningUnit.getBlocks().size();
            LearningUnitStatistic learningUnitProgress = getOrCreateLearningUnitProgress(user, learningUnit, kitProgress);

            List<BlockStatistic> blockProgressesInLup = blockProgressRepository.findByLearningUnitProgress_Uuid(learningUnitProgress.getUuid());

            totalCompletedBlocksInKit += blockProgressesInLup.stream().filter(this::isBlockProgressConsideredComplete).count();
        }

        if (totalBlocksInKit == 0) {
            kitProgress.setProgressPercentage(100);
        } else {
            int percentage = (int) Math.round(((double) totalCompletedBlocksInKit / totalBlocksInKit) * 100);
            kitProgress.setProgressPercentage(percentage);
        }

        if (kitProgress.getProgressPercentage() == 100 && !kitProgress.isCompleted()) {
            kitProgress.setCompleted(true);
            kitProgress.setCompletedAt(ZonedDateTime.now());
        } else if (kitProgress.getProgressPercentage() < 100 && kitProgress.isCompleted()) {
            kitProgress.setCompleted(false);
            kitProgress.setCompletedAt(null);
        }
        learningKitProgressRepository.save(kitProgress);
    }

    private boolean isBlockProgressConsideredComplete(BlockStatistic blockProgress) {
        Block actualBlockEntity = blockProgress.getBlock();

        switch (blockProgress) {
            case MultipleChoiceBlockStatistic mcProgress when actualBlockEntity instanceof MultipleChoiceBlock mcBlockDefinition -> {
                List<String> lastAnswers = mcProgress.getLastAnswers();
                List<String> correctAnswers = mcBlockDefinition.getCorrectAnswers();

                return correctAnswers != null && !correctAnswers.isEmpty() && lastAnswers != null && new HashSet<>(lastAnswers).equals(new HashSet<>(correctAnswers));
            }
            case QuestionBlockStatistic qProgress when actualBlockEntity instanceof QuestionBlock qBlockDefinition -> {
                String lastAnswer = qProgress.getLastAnswer();
                String expectedAnswer = qBlockDefinition.getExpectedAnswer();

                return lastAnswer != null && expectedAnswer != null && lastAnswer.equalsIgnoreCase(expectedAnswer);
            }
            case TheoryBlockStatistic theoryBlockProgress -> {
                return theoryBlockProgress.getIsViewed();
            }
            default -> {
            }
        }
        return false;
    }
}
