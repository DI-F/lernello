package ch.nova_omnia.lernello.statistic.model.block;

import ch.nova_omnia.lernello.block.model.TheoryBlock;
import ch.nova_omnia.lernello.statistic.model.LearningUnitStatistic;
import ch.nova_omnia.lernello.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("THEORY")
public class TheoryBlockStatistic extends BlockStatistic {

    @Column(name = "is_viewed")
    private Boolean isViewed = false;

    public TheoryBlockStatistic(User user, TheoryBlock block, LearningUnitStatistic unitProgress) {
        super(user, block, unitProgress);
    }

    public TheoryBlockStatistic(User user, TheoryBlock block, Boolean isViewed, LearningUnitStatistic unitProgress) {
        super(user, block, unitProgress);
        this.isViewed = isViewed;
    }
}
