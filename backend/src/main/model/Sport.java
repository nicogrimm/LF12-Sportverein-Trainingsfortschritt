package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sport")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_sport_id")
    private Long pkSportId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "unit", nullable = false)
    private String unit;
}