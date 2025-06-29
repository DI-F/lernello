package ch.nova_omnia.lernello.block.dto.response;

public sealed interface BlockResDTO permits MultipleChoiceBlockResDTO, QuestionBlockResDTO, TheoryBlockResDTO {
}
