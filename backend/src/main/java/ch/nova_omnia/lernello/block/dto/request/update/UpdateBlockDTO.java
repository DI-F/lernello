package ch.nova_omnia.lernello.block.dto.request.update;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ch.nova_omnia.lernello.block.dto.request.create.CreateMultipleChoiceBlockDTO;
import ch.nova_omnia.lernello.block.dto.request.create.CreateQuestionBlockDTO;
import ch.nova_omnia.lernello.block.dto.request.create.CreateTheoryBlockDTO;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateMultipleChoiceBlockDTO.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = CreateQuestionBlockDTO.class, name = "QUESTION"),
        @JsonSubTypes.Type(value = CreateTheoryBlockDTO.class, name = "THEORY")
})

public sealed interface UpdateBlockDTO
        permits UpdateMultipleChoiceBlockDTO, UpdateQuestionBlockDTO, UpdateTheoryBlockDTO {
}
