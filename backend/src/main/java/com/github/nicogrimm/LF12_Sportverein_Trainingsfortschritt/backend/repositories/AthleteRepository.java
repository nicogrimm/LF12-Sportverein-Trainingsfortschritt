package com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.repositories;

import com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.entities.Athlete;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

public interface AthleteRepository extends CrudRepository<@NonNull Athlete, @NonNull Integer> {

}
