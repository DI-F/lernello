package ch.nova_omnia.lernello.statistic.model.block.quiz;

import ch.nova_omnia.lernello.block.model.quiz.QuestionBlock;
import ch.nova_omnia.lernello.statistic.model.LearningUnitStatistic;
import ch.nova_omnia.lernello.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DiscriminatorValue("QUESTION")
public class QuestionBlockStatistic extends QuizBlockStatistic {

    @Column(name = "score_reached", nullable = true)
    private Integer scoreReached;

    @Lob
    @Column(name = "last_answer", columnDefinition = "TEXT", nullable = true)
    private String lastAnswer;

    public QuestionBlockStatistic(User user, QuestionBlock block, String lastAnswer, LearningUnitStatistic unitProgress, Boolean isCorrect) {
        super(user, block, unitProgress, isCorrect);
        this.lastAnswer = lastAnswer;
    }

    public QuestionBlockStatistic(User user, QuestionBlock block, String lastAnswer, LearningUnitStatistic unitProgress, Integer scoreReached, Boolean isCorrect) {
        super(user, block, unitProgress, isCorrect);
        this.scoreReached = scoreReached;
        this.lastAnswer = lastAnswer;
    }

    public QuestionBlockStatistic(User user, QuestionBlock block, LearningUnitStatistic unitProgress) {
        super(user, block, unitProgress);
    }
}
