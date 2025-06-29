package ch.nova_omnia.lernello.statistic.api;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.nova_omnia.lernello.statistic.dto.request.CheckMultipleChoiceAnswerDTO;
import ch.nova_omnia.lernello.statistic.dto.request.CheckQuestionAnswerDTO;
import ch.nova_omnia.lernello.statistic.dto.request.LearningKitOpened;
import ch.nova_omnia.lernello.statistic.dto.request.LearningUnitOpenedDTO;
import ch.nova_omnia.lernello.statistic.dto.request.TheoryBlockViewedDTO;
import ch.nova_omnia.lernello.statistic.dto.response.LearningKitStatisticResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.LearningUnitStatisticResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.MultipleChoiceAnswerValidationResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.QuestionAnswerValidationResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.TheoryBlockViewedResDTO;
import ch.nova_omnia.lernello.statistic.mapper.StatisticMapper;
import ch.nova_omnia.lernello.statistic.model.LearningKitStatistic;
import ch.nova_omnia.lernello.statistic.model.LearningUnitStatistic;
import ch.nova_omnia.lernello.statistic.model.block.TheoryBlockStatistic;
import ch.nova_omnia.lernello.statistic.service.StatisticService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/progress")
@Validated
public class StatisticRestController {
    private final StatisticService progressService;
    private final StatisticMapper progressMapper;

    @PostMapping("/learning-kit/opened")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public LearningKitStatisticResDTO markLearningKitOpened(
                                                           @RequestBody @Valid LearningKitOpened dto, @AuthenticationPrincipal UserDetails userDetails) {
        LearningKitStatistic progress = progressService.markLearningKitOpened(dto, userDetails);
        return progressMapper.toLearningKitProgressResDTO(progress);
    }

    @PostMapping("/learning-unit/opened")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningUnitStatisticResDTO markLearningUnitOpened(
                                                                 @RequestBody @Valid LearningUnitOpenedDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        LearningUnitStatistic unitProgress = progressService.markLearningUnitOpened(dto, userDetails);
        return progressMapper.toLearningUnitProgressDTO(unitProgress);
    }

    @PostMapping("/block/multiple-choice/check")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid MultipleChoiceAnswerValidationResDTO checkMultipleChoiceAnswer(
                                                                                 @RequestBody @Valid CheckMultipleChoiceAnswerDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        return progressService.checkMultipleChoiceAnswer(dto, userDetails);
    }

    @PostMapping("/block/question/check")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid QuestionAnswerValidationResDTO checkQuestionAnswer(
                                                                     @RequestBody @Valid CheckQuestionAnswerDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        return progressService.checkQuestionAnswer(dto, userDetails);
    }

    @PostMapping("/block/theory/viewed")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid TheoryBlockViewedResDTO markTheoryBlockViewed(
                                                                @RequestBody @Valid TheoryBlockViewedDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        TheoryBlockStatistic theoryBlockProgress = progressService.markTheoryBlockViewed(dto, userDetails);
        return progressMapper.toTheoryBlockViewedResDTO(theoryBlockProgress);
    }

    @GetMapping("/learning-kit/{learningKitId}")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningKitStatisticResDTO getLearningKitProgress(
                                                                   @PathVariable UUID learningKitId, @AuthenticationPrincipal UserDetails userDetails) {
        LearningKitStatistic progress = progressService.getLearningKitProgress(learningKitId, userDetails);
        return progressMapper.toLearningKitProgressResDTO(progress);
    }

    @GetMapping("/learning-unit/{learningUnitId}")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningUnitStatisticResDTO getLearningUnitProgress(
                                                                  @PathVariable UUID learningUnitId, @AuthenticationPrincipal UserDetails userDetails) {
        LearningUnitStatistic progress = progressService.getLearningUnitProgress(learningUnitId, userDetails);
        return progressMapper.toLearningUnitProgressDTO(progress);
    }

    @GetMapping("/learning-kit/{learningKitId}/trainees-progress")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid List<LearningKitStatisticResDTO> getLearningKitProgressForAllTrainees(
                                                                                           @PathVariable UUID learningKitId) {
        List<LearningKitStatistic> progresses = progressService.getLearningKitProgressForAllTrainees(learningKitId);
        return progresses.stream().map(progressMapper::toLearningKitProgressResDTO).collect(Collectors.toList());
    }
}
