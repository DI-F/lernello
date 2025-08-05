package ch.nova_omnia.lernello.block.dto.request.blocks;

public sealed interface BlockDTO permits MultipleChoiceBlockDTO, QuestionBlockDTO, TheoryBlockDTO {
}
