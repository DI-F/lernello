package ch.nova_omnia.lernello.statistic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.statistic.dto.response.BlockProgressResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.LearningKitProgressResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.LearningUnitProgressResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.MultipleChoiceBlockProgressResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.QuestionBlockProgressResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.TheoryBlockProgressResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.TheoryBlockViewedResDTO;
import ch.nova_omnia.lernello.statistic.model.LearningKitProgress;
import ch.nova_omnia.lernello.statistic.model.LearningUnitProgress;
import ch.nova_omnia.lernello.statistic.model.block.BlockStatistic;
import ch.nova_omnia.lernello.statistic.model.block.TheoryBlockStatistic;
import ch.nova_omnia.lernello.statistic.model.block.quiz.MultipleChoiceBlockStatistic;
import ch.nova_omnia.lernello.statistic.model.block.quiz.QuestionBlockStatistic;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatisticMapper {

    @Mapping(source = "learningKit.uuid", target = "learningKitId")
    @Mapping(source = "user.uuid", target = "userId")
    @Mapping(source = "opened", target = "isOpened")
    @Mapping(source = "completed", target = "isCompleted")
    LearningKitProgressResDTO toLearningKitProgressResDTO(LearningKitProgress learningKitProgress);

    @Mapping(source = "learningUnit.uuid", target = "learningUnitId")
    @Mapping(source = "opened", target = "isOpened")
    @Mapping(source = "completed", target = "isCompleted")
    LearningUnitProgressResDTO toLearningUnitProgressDTO(LearningUnitProgress learningUnitProgress);

    @Mapping(source = "block.uuid", target = "blockId")
    @Mapping(source = "isViewed", target = "isViewed")
    TheoryBlockViewedResDTO toTheoryBlockViewedResDTO(TheoryBlockStatistic theoryBlockProgress);

    default BlockProgressResDTO toBlockProgressResDTO(BlockStatistic blockProgress) {
        if (blockProgress == null) {
            return null;
        }
        if (blockProgress instanceof TheoryBlockStatistic theoryBlockProgress) {
            return toTheoryBlockProgressResDTO(theoryBlockProgress);
        } else if (blockProgress instanceof QuestionBlockStatistic questionBlockProgress) {
            return toQuestionBlockProgressResDTO(questionBlockProgress);
        } else if (blockProgress instanceof MultipleChoiceBlockStatistic multipleChoiceBlockProgress) {
            return toMultipleChoiceBlockProgressResDTO(multipleChoiceBlockProgress);
        } else {
            throw new IllegalArgumentException("Unknown block progress type: " + blockProgress.getClass());
        }
    }

    @Mapping(source = "block.uuid", target = "blockId")
    MultipleChoiceBlockProgressResDTO toMultipleChoiceBlockProgressResDTO(MultipleChoiceBlockStatistic multipleChoiceBlockProgress);

    @Mapping(source = "block.uuid", target = "blockId")
    QuestionBlockProgressResDTO toQuestionBlockProgressResDTO(QuestionBlockStatistic questionBlockProgress);

    @Mapping(source = "block.uuid", target = "blockId")
    TheoryBlockProgressResDTO toTheoryBlockProgressResDTO(TheoryBlockStatistic theoryBlockProgress);
}
