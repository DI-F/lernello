package ch.nova_omnia.lernello.block.model.quiz;

import ch.nova_omnia.lernello.block.model.Block;
import ch.nova_omnia.lernello.block.model.BlockType;
import ch.nova_omnia.lernello.learningUnit.model.LearningUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class QuizBlock extends Block {

    @Column(name = "max_score", nullable = true)
    private Integer maxScore;

    public QuizBlock(BlockType type, String name, int position, LearningUnit learningUnit) {
        super(type, name, position, learningUnit);
    }

    public QuizBlock(BlockType type, String name, int position, LearningUnit learningUnit, int maxScore) {
        super(type, name, position, learningUnit);
        this.maxScore = maxScore;
    }
}
