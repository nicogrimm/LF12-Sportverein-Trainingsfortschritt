package com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sport")
@Getter
@NoArgsConstructor
public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sport_sport_id_seq")
    @SequenceGenerator(allocationSize = 1)
    @Column(name = "sport_id")
    int id;
    @Setter
    @Column
    String name;
    @Setter
    @Column
    String unit;

    public Sport(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }
}
