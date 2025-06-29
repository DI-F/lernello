package ch.nova_omnia.lernello.statistic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.statistic.dto.response.BlockStatisticResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.LearningKitStatisticResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.LearningUnitStatisticResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.MultipleChoiceBlockStatisticResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.QuestionBlockStatisticResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.TheoryBlockStatisticResDTO;
import ch.nova_omnia.lernello.statistic.dto.response.TheoryBlockViewedResDTO;
import ch.nova_omnia.lernello.statistic.model.LearningKitStatistic;
import ch.nova_omnia.lernello.statistic.model.LearningUnitStatistic;
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
    LearningKitStatisticResDTO toLearningKitProgressResDTO(LearningKitStatistic learningKitProgress);

    @Mapping(source = "learningUnit.uuid", target = "learningUnitId")
    @Mapping(source = "opened", target = "isOpened")
    @Mapping(source = "completed", target = "isCompleted")
    LearningUnitStatisticResDTO toLearningUnitProgressDTO(LearningUnitStatistic learningUnitProgress);

    @Mapping(source = "block.uuid", target = "blockId")
    @Mapping(source = "isViewed", target = "isViewed")
    TheoryBlockViewedResDTO toTheoryBlockViewedResDTO(TheoryBlockStatistic theoryBlockProgress);

    default BlockStatisticResDTO toBlockProgressResDTO(BlockStatistic blockProgress) {
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
    MultipleChoiceBlockStatisticResDTO toMultipleChoiceBlockProgressResDTO(MultipleChoiceBlockStatistic multipleChoiceBlockProgress);

    @Mapping(source = "block.uuid", target = "blockId")
    QuestionBlockStatisticResDTO toQuestionBlockProgressResDTO(QuestionBlockStatistic questionBlockProgress);

    @Mapping(source = "block.uuid", target = "blockId")
    TheoryBlockStatisticResDTO toTheoryBlockProgressResDTO(TheoryBlockStatistic theoryBlockProgress);
}
