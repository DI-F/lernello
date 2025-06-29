package ch.nova_omnia.lernello.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import ch.nova_omnia.lernello.block.model.TheoryBlock;
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
import ch.nova_omnia.lernello.statistic.model.block.TheoryBlockStatistic;
import ch.nova_omnia.lernello.statistic.model.block.quiz.MultipleChoiceBlockStatistic;
import ch.nova_omnia.lernello.statistic.model.block.quiz.QuestionBlockStatistic;
import ch.nova_omnia.lernello.statistic.repository.BlockStatisticRepository;
import ch.nova_omnia.lernello.statistic.repository.LearningKitStatisticRepository;
import ch.nova_omnia.lernello.statistic.repository.LearningUnitStatisticRepository;
import ch.nova_omnia.lernello.statistic.service.StatisticService;
import ch.nova_omnia.lernello.user.model.User;
import ch.nova_omnia.lernello.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class StatisticServiceTest {

    @Mock
    LearningKitStatisticRepository kitProgressRepo;
    @Mock
    LearningUnitStatisticRepository unitProgressRepo;
    @Mock
    BlockStatisticRepository blockProgressRepo;
    @Mock
    LearningKitRepository kitRepo;
    @Mock
    LearningUnitRepository unitRepo;
    @Mock
    BlockRepository blockRepo;
    @Mock
    UserService userService;
    @Mock
    AIBlockService aiService;
    @Mock
    UserDetails userDetails;

    @InjectMocks
    StatisticService service;

    User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setUuid(UUID.randomUUID());
    }

    @Test
    void getLearningKitProgressShouldReturnExisting() {
        UUID kitId = UUID.randomUUID();
        LearningKit kit = new LearningKit();
        kit.setUuid(kitId);
        LearningKitStatistic progress = new LearningKitStatistic(user, kit);

        when(userService.getUserFromUserDetails(userDetails)).thenReturn(user);
        when(kitProgressRepo.findByUser_UuidAndLearningKit_Uuid(user.getUuid(), kitId)).thenReturn(Optional.of(progress));

        LearningKitStatistic result = service.getLearningKitProgress(kitId, userDetails);

        assertThat(result).isEqualTo(progress);
    }

    @Test
    void getLearningKitProgressShouldCreateIfMissing() {
        UUID kitId = UUID.randomUUID();
        LearningKit kit = new LearningKit();
        kit.setUuid(kitId);

        when(userService.getUserFromUserDetails(userDetails)).thenReturn(user);
        when(kitProgressRepo.findByUser_UuidAndLearningKit_Uuid(user.getUuid(), kitId)).thenReturn(Optional.empty());
        when(kitRepo.findById(kitId)).thenReturn(Optional.of(kit));

        LearningKitStatistic result = service.getLearningKitProgress(kitId, userDetails);

        assertThat(result.getLearningKit()).isEqualTo(kit);
        assertThat(result.getUser()).isEqualTo(user);
    }

    @Test
    void getLearningUnitProgressShouldReturnExisting() {
        UUID unitId = UUID.randomUUID();
        LearningUnit unit = new LearningUnit();
        unit.setUuid(unitId);
        LearningUnitStatistic progress = new LearningUnitStatistic(user, unit);

        when(userService.getUserFromUserDetails(userDetails)).thenReturn(user);
        when(unitProgressRepo.findByUser_UuidAndLearningUnit_Uuid(user.getUuid(), unitId)).thenReturn(Optional.of(progress));

        LearningUnitStatistic result = service.getLearningUnitProgress(unitId, userDetails);

        assertThat(result).isEqualTo(progress);
    }

    @Test
    void getLearningUnitProgressShouldCreateIfMissing() {
        UUID unitId = UUID.randomUUID();
        LearningUnit unit = new LearningUnit();
        unit.setUuid(unitId);

        when(userService.getUserFromUserDetails(userDetails)).thenReturn(user);
        when(unitProgressRepo.findByUser_UuidAndLearningUnit_Uuid(user.getUuid(), unitId)).thenReturn(Optional.empty());
        when(unitRepo.findById(unitId)).thenReturn(Optional.of(unit));

        LearningUnitStatistic result = service.getLearningUnitProgress(unitId, userDetails);

        assertThat(result.getLearningUnit()).isEqualTo(unit);
        assertThat(result.getUser()).isEqualTo(user);
    }

    @Test
    void getLearningKitProgressForAllTraineesShouldReturnList() {
        UUID kitId = UUID.randomUUID();
        User trainee1 = new User();
        trainee1.setUuid(UUID.randomUUID());
        User trainee2 = new User();
        trainee2.setUuid(UUID.randomUUID());

        LearningKit kit = new LearningKit();
        kit.setUuid(kitId);
        kit.setTrainees(List.of(trainee1, trainee2));
        kit.setLearningUnits(Collections.emptyList());

        when(kitRepo.findById(kitId)).thenReturn(Optional.of(kit));
        when(kitProgressRepo.findByUser_UuidAndLearningKit_Uuid(any(), eq(kitId))).thenReturn(Optional.empty());
        when(kitProgressRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        List<LearningKitStatistic> result = service.getLearningKitProgressForAllTrainees(kitId);

        assertThat(result).hasSize(2);
    }

    @Test
    void markLearningKitOpenedShouldSaveProgress() {
        UUID kitId = UUID.randomUUID();
        LearningKit kit = new LearningKit();
        kit.setUuid(kitId);
        kit.setLearningUnits(Collections.emptyList());

        LearningKitStatistic progress = new LearningKitStatistic(user, kit);
        progress.setOpened(false);

        when(userService.getUserFromUserDetails(userDetails)).thenReturn(user);
        when(kitRepo.findById(kitId)).thenReturn(Optional.of(kit));
        when(kitProgressRepo.findByUser_UuidAndLearningKit_Uuid(user.getUuid(), kitId)).thenReturn(Optional.of(progress));

        LearningKitOpened dto = new LearningKitOpened(kitId);

        LearningKitStatistic result = service.markLearningKitOpened(dto, userDetails);

        assertThat(result.isOpened()).isTrue();
        verify(kitProgressRepo, atLeastOnce()).save(any());
    }

    @Test
    void markLearningUnitOpenedShouldSaveProgress() {
        UUID unitId = UUID.randomUUID();
        UUID kitId = UUID.randomUUID();

        LearningKit kit = new LearningKit();
        kit.setUuid(kitId);
        kit.setLearningUnits(Collections.emptyList());

        LearningUnit unit = new LearningUnit();
        unit.setUuid(unitId);
        unit.setLearningKit(kit);
        unit.setBlocks(Collections.emptyList());

        LearningKitStatistic kitProgress = new LearningKitStatistic(user, kit);
        LearningUnitStatistic unitProgress = new LearningUnitStatistic(user, unit);

        when(userService.getUserFromUserDetails(userDetails)).thenReturn(user);
        when(unitRepo.findById(unitId)).thenReturn(Optional.of(unit));
        when(kitProgressRepo.findByUser_UuidAndLearningKit_Uuid(user.getUuid(), kitId)).thenReturn(Optional.of(kitProgress));
        when(unitProgressRepo.findByUser_UuidAndLearningUnit_Uuid(user.getUuid(), unitId)).thenReturn(Optional.of(unitProgress));

        LearningUnitOpenedDTO dto = new LearningUnitOpenedDTO(unitId);

        LearningUnitStatistic result = service.markLearningUnitOpened(dto, userDetails);

        assertThat(result.isOpened()).isTrue();
        verify(unitProgressRepo, atLeastOnce()).save(any());
    }

    @Test
    void checkMultipleChoiceAnswerShouldReturnCorrectness() {
        UUID blockId = UUID.randomUUID();

        MultipleChoiceBlock block = new MultipleChoiceBlock();
        block.setUuid(blockId);
        block.setCorrectAnswers(List.of("A", "B"));

        LearningUnit unit = new LearningUnit();
        unit.setBlocks(List.of(block));
        LearningKit kit = new LearningKit();
        unit.setLearningKit(kit);
        block.setLearningUnit(unit);

        LearningKitStatistic kitProgress = new LearningKitStatistic(user, kit);
        LearningUnitStatistic unitProgress = new LearningUnitStatistic(user, unit);
        MultipleChoiceBlockStatistic mcProgress = new MultipleChoiceBlockStatistic(user, block, unitProgress);

        when(userService.getUserFromUserDetails(userDetails)).thenReturn(user);
        when(blockRepo.findById(blockId)).thenReturn(Optional.of(block));
        when(kitProgressRepo.findByUser_UuidAndLearningKit_Uuid(user.getUuid(), kit.getUuid())).thenReturn(Optional.of(kitProgress));
        when(unitProgressRepo.findByUser_UuidAndLearningUnit_Uuid(user.getUuid(), unit.getUuid())).thenReturn(Optional.of(unitProgress));
        when(blockProgressRepo.findByUser_UuidAndBlock_Uuid(user.getUuid(), blockId)).thenReturn(Optional.of(mcProgress));

        CheckMultipleChoiceAnswerDTO dto = new CheckMultipleChoiceAnswerDTO(blockId, List.of("A", "B"));

        MultipleChoiceAnswerValidationResDTO result = service.checkMultipleChoiceAnswer(dto, userDetails);

        assertThat(result.isCorrect()).isTrue();
        verify(blockProgressRepo, atLeastOnce()).save(any());
    }

    @Test
    void checkQuestionAnswerShouldReturnCorrectness() {
        UUID blockId = UUID.randomUUID();

        QuestionBlock block = new QuestionBlock();
        block.setUuid(blockId);
        block.setExpectedAnswer("42");

        LearningUnit unit = new LearningUnit();
        unit.setBlocks(List.of(block));
        LearningKit kit = new LearningKit();
        unit.setLearningKit(kit);
        block.setLearningUnit(unit);

        LearningKitStatistic kitProgress = new LearningKitStatistic(user, kit);
        LearningUnitStatistic unitProgress = new LearningUnitStatistic(user, unit);
        QuestionBlockStatistic qProgress = new QuestionBlockStatistic(user, block, unitProgress);

        when(userService.getUserFromUserDetails(userDetails)).thenReturn(user);
        when(blockRepo.findById(blockId)).thenReturn(Optional.of(block));
        when(kitProgressRepo.findByUser_UuidAndLearningKit_Uuid(user.getUuid(), kit.getUuid())).thenReturn(Optional.of(kitProgress));
        when(unitProgressRepo.findByUser_UuidAndLearningUnit_Uuid(user.getUuid(), unit.getUuid())).thenReturn(Optional.of(unitProgress));
        when(blockProgressRepo.findByUser_UuidAndBlock_Uuid(user.getUuid(), blockId)).thenReturn(Optional.of(qProgress));
        when(aiService.checkQuestionAnswerWithAI("42", "42")).thenReturn(true);

        CheckQuestionAnswerDTO dto = new CheckQuestionAnswerDTO(blockId, "42");

        QuestionAnswerValidationResDTO result = service.checkQuestionAnswer(dto, userDetails);

        assertThat(result.isCorrect()).isTrue();
        verify(blockProgressRepo).save(any());
    }

    @Test
    void markTheoryBlockViewedShouldSaveProgress() {
        UUID blockId = UUID.randomUUID();

        TheoryBlock block = new TheoryBlock();
        block.setUuid(blockId);

        LearningUnit unit = new LearningUnit();
        unit.setBlocks(List.of(block));
        LearningKit kit = new LearningKit();
        unit.setLearningKit(kit);
        block.setLearningUnit(unit);

        LearningKitStatistic kitProgress = new LearningKitStatistic(user, kit);
        LearningUnitStatistic unitProgress = new LearningUnitStatistic(user, unit);
        TheoryBlockStatistic theoryProgress = new TheoryBlockStatistic(user, block, unitProgress);

        when(userService.getUserFromUserDetails(userDetails)).thenReturn(user);
        when(blockRepo.findById(blockId)).thenReturn(Optional.of(block));
        when(kitProgressRepo.findByUser_UuidAndLearningKit_Uuid(user.getUuid(), kit.getUuid())).thenReturn(Optional.of(kitProgress));
        when(unitProgressRepo.findByUser_UuidAndLearningUnit_Uuid(user.getUuid(), unit.getUuid())).thenReturn(Optional.of(unitProgress));
        when(blockProgressRepo.findByUser_UuidAndBlock_Uuid(user.getUuid(), blockId)).thenReturn(Optional.of(theoryProgress));

        TheoryBlockViewedDTO dto = new TheoryBlockViewedDTO(blockId);

        TheoryBlockStatistic result = service.markTheoryBlockViewed(dto, userDetails);

        assertThat(result.getIsViewed()).isTrue();
        verify(blockProgressRepo).save(theoryProgress);
    }
}
