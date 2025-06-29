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
import ch.nova_omnia.lernello.statistic.dto.response.LearningKitProgressResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.LearningUnitProgressResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.MultipleChoiceAnswerValidationResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.QuestionAnswerValidationResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.TheoryBlockViewedResDTO;
import ch.nova_omnia.lernello.statistic.mapper.StatisticMapper;
import ch.nova_omnia.lernello.statistic.model.LearningKitProgress;
import ch.nova_omnia.lernello.statistic.model.LearningUnitProgress;
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
    public LearningKitProgressResDTO markLearningKitOpened(
                                                           @RequestBody @Valid LearningKitOpened dto, @AuthenticationPrincipal UserDetails userDetails) {
        LearningKitProgress progress = progressService.markLearningKitOpened(dto, userDetails);
        return progressMapper.toLearningKitProgressResDTO(progress);
    }

    @PostMapping("/learning-unit/opened")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningUnitProgressResDTO markLearningUnitOpened(
                                                                 @RequestBody @Valid LearningUnitOpenedDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        LearningUnitProgress unitProgress = progressService.markLearningUnitOpened(dto, userDetails);
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
    public @Valid LearningKitProgressResDTO getLearningKitProgress(
                                                                   @PathVariable UUID learningKitId, @AuthenticationPrincipal UserDetails userDetails) {
        LearningKitProgress progress = progressService.getLearningKitProgress(learningKitId, userDetails);
        return progressMapper.toLearningKitProgressResDTO(progress);
    }

    @GetMapping("/learning-unit/{learningUnitId}")
    @PreAuthorize("hasAuthority('SCOPE_progress:read')")
    public @Valid LearningUnitProgressResDTO getLearningUnitProgress(
                                                                  @PathVariable UUID learningUnitId, @AuthenticationPrincipal UserDetails userDetails) {
        LearningUnitProgress progress = progressService.getLearningUnitProgress(learningUnitId, userDetails);
        return progressMapper.toLearningUnitProgressDTO(progress);
    }

    @GetMapping("/learning-kit/{learningKitId}/trainees-progress")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid List<LearningKitProgressResDTO> getLearningKitProgressForAllTrainees(
                                                                                           @PathVariable UUID learningKitId) {
        List<LearningKitProgress> progresses = progressService.getLearningKitProgressForAllTrainees(learningKitId);
        return progresses.stream().map(progressMapper::toLearningKitProgressResDTO).collect(Collectors.toList());
    }
}
