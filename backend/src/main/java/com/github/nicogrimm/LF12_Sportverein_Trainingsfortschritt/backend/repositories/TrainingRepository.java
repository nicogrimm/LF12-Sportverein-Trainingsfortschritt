package com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.repositories;

import com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.entities.Athlete;
import com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.entities.Sport;
import com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.entities.Training;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface TrainingRepository extends CrudRepository<@NonNull Training, @NonNull Integer> {
    Training findByAthlete(@NonNull Athlete athlete);
    Training findBySport(@NonNull Sport sport);
    Training findByDate(@NonNull Date date);
}
