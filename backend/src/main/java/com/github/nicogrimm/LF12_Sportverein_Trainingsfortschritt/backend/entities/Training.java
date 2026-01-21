package com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.flywaydb.core.internal.database.DatabaseExecutionStrategy;

import java.util.Date;

@Entity
@Table(name = "training")
@Getter
@NoArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_training_id_seq")
    @SequenceGenerator(allocationSize = 1)
    @Column(name = "training_id")
    int id;
    @Setter
    @JoinColumn(name = "athlete_id")
    @ManyToOne
    Athlete athlete;
    @Setter
    @JoinColumn(name = "sport_id")
    @ManyToOne
    Sport sport;
    @Setter
    @Column
    Date date;
    @Setter
    @Column
    float metric;

    public Training(Athlete athlete, Sport sport, float metric, Date date) {
        this.athlete = athlete;
        this.sport = sport;
        this.metric = metric;
        this.date = date;
    }
}
