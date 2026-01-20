package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "athlete")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Athlete {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_athlete_id")
    private Long pkAthleteId;
    
    @Column(name = "firstname", nullable = false)
    private String firstname;
    
    @Column(name = "name", nullable = false)
    private String name;
}