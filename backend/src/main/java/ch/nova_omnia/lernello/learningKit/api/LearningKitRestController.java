package ch.nova_omnia.lernello.learningKit.api;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ch.nova_omnia.lernello.learningKit.dto.request.CreateLearningKitDTO;
import ch.nova_omnia.lernello.learningKit.dto.request.UpdateLearningKitDTO;
import ch.nova_omnia.lernello.learningKit.dto.response.LearningKitResDTO;
import ch.nova_omnia.lernello.learningKit.mapper.LearningKitMapper;
import ch.nova_omnia.lernello.learningKit.model.LearningKit;
import ch.nova_omnia.lernello.learningKit.service.LearningKitService;
import ch.nova_omnia.lernello.learningUnit.dto.request.UpdateLearningUnitOrderDTO;
import ch.nova_omnia.lernello.learningUnit.service.LearningUnitService;
import ch.nova_omnia.lernello.statistic.model.LearningKitStatistic;
import ch.nova_omnia.lernello.statistic.repository.LearningKitStatisticRepository;
import ch.nova_omnia.lernello.user.dto.request.CreateTraineeDTO;
import ch.nova_omnia.lernello.user.dto.response.GenericSuccessDTO;
import ch.nova_omnia.lernello.user.model.Role;
import ch.nova_omnia.lernello.user.model.User;
import ch.nova_omnia.lernello.user.service.CustomUserDetailsService;
import ch.nova_omnia.lernello.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/learning-kits")
@Validated
@RequiredArgsConstructor
public class LearningKitRestController {
    private final LearningKitService learningKitService;
    private final LearningUnitService learningUnitService;
    private final LearningKitMapper learningKitMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final LearningKitStatisticRepository learningKitProgressRepository;
    private final UserService userService;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid LearningKitResDTO createLearningKit(@Valid @RequestBody CreateLearningKitDTO learningKit) {
        LearningKit entity = learningKitMapper.toEntity(learningKit);
        LearningKit savedEntity = learningKitService.save(entity);
        return learningKitMapper.toDTO(savedEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public UUID deleteLearningKit(@PathVariable UUID id) {
        List<LearningKitStatistic> progressesToDelete = learningKitProgressRepository.findAllByLearningKit_Uuid(id);
        if (!progressesToDelete.isEmpty()) {
            learningKitProgressRepository.deleteAll(progressesToDelete);
        }
        learningKitService.deleteById(id);
        return id;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public Page<LearningKitResDTO> getList(@AuthenticationPrincipal UserDetails userDetails, @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        UUID userId = customUserDetailsService.getUserIdByUsername(userDetails.getUsername());
        Page<LearningKit> kits = learningKitService.getList(pageable, userId);
        return kits.map(learningKitMapper::toDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_kits:read')")
    public @Valid LearningKitResDTO getLearningKitById(@Valid @PathVariable UUID id) {

        LearningKit learningKit = learningKitService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Learning Kit not found"));
        learningKitService.updateLearningKitStatistics(learningKit);
        return learningKitMapper.toDTO(learningKit);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid LearningKitResDTO updateLearningKit(@Valid @RequestBody UpdateLearningKitDTO updateLearningKit, @PathVariable UUID id) {
        LearningKit destination = learningKitService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Learning Kit not found"));
        learningKitService.updateLearningKitStatistics(destination);
        learningKitMapper.update(updateLearningKit, destination);
        LearningKit savedEntity = learningKitService.save(destination);
        return learningKitMapper.toDTO(savedEntity);
    }

    @DeleteMapping("/trainees/{kitId}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public UUID removeTraineeFromKit(@PathVariable UUID kitId, @RequestBody UUID userId) {
        learningKitService.removeTrainee(kitId, userId);
        return kitId;
    }

    @PostMapping("/publish/{id}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public UUID publishLearningKit(@PathVariable UUID id) {
        learningKitService.publishLearningKit(id);
        return id;
    }

    @PostMapping("/trainee/{id}")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid UUID addTrainee(
        @PathVariable UUID id, @RequestBody @Valid CreateTraineeDTO traineeDetails
    ) {
        User trainee = userService.createUser(traineeDetails.username(), traineeDetails.name(), traineeDetails.surname(), Role.TRAINEE);
        learningKitService.saveTraineeInKit(id, trainee);
        return id;
    }

    @PatchMapping("/{id}/reorder/learning-units/")
    @PreAuthorize("hasAuthority('SCOPE_kits:write')")
    public @Valid GenericSuccessDTO reorderLearningUnits(@PathVariable UUID id, @Valid @RequestBody UpdateLearningUnitOrderDTO updateLearningUnitOrderDTO) {
        learningUnitService.updateLearningUnitPosition(id, updateLearningUnitOrderDTO);
        return new GenericSuccessDTO(true);
    }
}
