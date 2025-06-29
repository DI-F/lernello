package ch.nova_omnia.lernello.block.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import ch.nova_omnia.lernello.block.dto.response.BlockResDTO;
import ch.nova_omnia.lernello.block.dto.response.MultipleChoiceBlockResDTO;
import ch.nova_omnia.lernello.block.dto.response.QuestionBlockResDTO;
import ch.nova_omnia.lernello.block.dto.response.TheoryBlockResDTO;
import ch.nova_omnia.lernello.block.model.Block;
import ch.nova_omnia.lernello.block.model.TheoryBlock;
import ch.nova_omnia.lernello.block.model.quiz.MultipleChoiceBlock;
import ch.nova_omnia.lernello.block.model.quiz.QuestionBlock;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BlockMapper {

    MultipleChoiceBlockResDTO toMultipleChoiceBlockResDTO(MultipleChoiceBlock multipleChoiceBlock);

    TheoryBlockResDTO toTheoryBlockResDTO(TheoryBlock theoryBlock);

    QuestionBlockResDTO toQuestionBlockResDTO(QuestionBlock questionBlock);

    TheoryBlock toTheoryBlock(TheoryBlockResDTO theoryBlock);

    default BlockResDTO toBlockResDTO(Block block) {
        if (block instanceof TheoryBlock) {
            return toTheoryBlockResDTO((TheoryBlock) block);
        } else if (block instanceof MultipleChoiceBlock) {
            return toMultipleChoiceBlockResDTO((MultipleChoiceBlock) block);
        } else if (block instanceof QuestionBlock) {
            return toQuestionBlockResDTO((QuestionBlock) block);
        } else {
            throw new IllegalArgumentException("Unsupported block type: " + block.getClass().getName());
        }
    }
}
