package ch.nova_omnia.lernello.block.model.quiz;

import static ch.nova_omnia.lernello.block.model.BlockType.QUESTION;

import ch.nova_omnia.lernello.learningUnit.model.LearningUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class QuestionBlock extends QuizBlock {

    @Lob
    @Column(name = "question", nullable = true)
    private String question;

    @Lob
    @Column(name = "expected_answer", nullable = true)
    private String expectedAnswer;

    public QuestionBlock(String name, int position, LearningUnit learningUnit, String question, String expectedAnswer) {
        super(QUESTION, name, position, learningUnit);
        this.question = question;
        this.expectedAnswer = expectedAnswer;
    }

    public QuestionBlock(String name, int position, LearningUnit learningUnit, String question, String expectedAnswer, int maxScore) {
        super(QUESTION, name, position, learningUnit, maxScore);
        this.question = question;
        this.expectedAnswer = expectedAnswer;
    }
}
