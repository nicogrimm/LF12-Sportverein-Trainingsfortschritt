package sportverein.model;

import java.time.OffsetDateTime;

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
    @Column(name = "training_id")
    private int trainingId;
    
    @Column(name = "athlete_id", nullable = false)
    private int athleteId;
    
    @Column(name = "sport_id", nullable = false)
    private int sportId;
    
    @Column(name = "date", nullable = false)
    private OffsetDateTime date;
    
    @Column(name = "metric", nullable = false)
    private Float metric;
}