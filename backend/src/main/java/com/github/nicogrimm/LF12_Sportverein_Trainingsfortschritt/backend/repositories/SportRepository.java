package com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.repositories;

import com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.entities.Sport;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;


public interface SportRepository extends CrudRepository<@NonNull Sport, @NonNull Integer> {

}
