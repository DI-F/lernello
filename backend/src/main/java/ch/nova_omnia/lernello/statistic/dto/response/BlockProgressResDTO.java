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
    @JsonSubTypes.Type(value = TheoryBlockProgressResDTO.class, name = "THEORY"),
    @JsonSubTypes.Type(value = MultipleChoiceBlockProgressResDTO.class, name = "MULTIPLE_CHOICE"),
    @JsonSubTypes.Type(value = QuestionBlockProgressResDTO.class, name = "QUESTION")
})
public sealed interface BlockProgressResDTO permits TheoryBlockProgressResDTO, MultipleChoiceBlockProgressResDTO, QuestionBlockProgressResDTO {
    UUID blockId();
    BlockType blockType();
}
