package com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "athlete")
@Getter
@NoArgsConstructor
public class Athlete {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "athlete_athlete_id_seq")
    @SequenceGenerator(allocationSize = 1)
    @Column(name = "athlete_id")
    int id;
    @Setter
    @Column
    String firstname;
    @Setter
    @Column
    String name;

    public Athlete(String firstname, String name) {
        this.firstname = firstname;
        this.name = name;
    }
}
