package ch.nova_omnia.lernello.statistic.model.block.quiz;

import ch.nova_omnia.lernello.block.model.Block;
import ch.nova_omnia.lernello.statistic.model.LearningUnitStatistic;
import ch.nova_omnia.lernello.statistic.model.block.BlockStatistic;
import ch.nova_omnia.lernello.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class QuizBlockStatistic extends BlockStatistic {

    @Column(name = "score_reached", nullable = true)
    private Integer scoreReached;

    @Column(name = "is_correct", nullable = true)
    private Boolean isCorrect;

    public QuizBlockStatistic(User user, Block block, LearningUnitStatistic learningUnitProgress, Integer scoreReached, Boolean isCorrect) {
        super(user, block, learningUnitProgress);
        this.scoreReached = scoreReached;
        this.isCorrect = isCorrect;
    }

    public QuizBlockStatistic(User user, Block block, LearningUnitStatistic learningUnitProgress, Boolean isCorrect) {
        super(user, block, learningUnitProgress);
        this.isCorrect = isCorrect;
    }

    public QuizBlockStatistic(User user, Block block, LearningUnitStatistic learningUnitProgress) {
        super(user, block, learningUnitProgress);
    }
}
