package sportverein.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "training")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Training {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_training_id")
    private Long pkTrainingId;
    
    @Column(name = "fk_athlete_id", nullable = false)
    private Long fkAthleteId;
    
    @Column(name = "fk_sport_id", nullable = false)
    private Long fkSportId;
    
    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    
    @Column(name = "metric", nullable = false)
    private String metric;
}