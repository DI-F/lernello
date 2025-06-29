package ch.nova_omnia.lernello.statistic.dto.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ch.nova_omnia.lernello.block.model.BlockType;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "blockType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = TheoryBlockStatisticResDTO.class, name = "THEORY"),
    @JsonSubTypes.Type(value = MultipleChoiceBlockStatisticResDTO.class, name = "MULTIPLE_CHOICE"),
    @JsonSubTypes.Type(value = QuestionBlockStatisticResDTO.class, name = "QUESTION")
})
public sealed interface BlockStatisticResDTO permits TheoryBlockStatisticResDTO, MultipleChoiceBlockStatisticResDTO, QuestionBlockStatisticResDTO {
    UUID blockId();
    BlockType blockType();
}
