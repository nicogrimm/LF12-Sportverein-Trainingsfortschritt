package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    
    // Optional: JPA-Beziehungen
    @ManyToOne
    @JoinColumn(name = "fk_athlete_id", insertable = false, updatable = false)
    private Athlete athlete;
    
    @ManyToOne
    @JoinColumn(name = "fk_sport_id", insertable = false, updatable = false)
    private Sport sport;
}