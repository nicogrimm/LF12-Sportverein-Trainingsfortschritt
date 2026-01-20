package com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.repository;

import com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    
    // Spring Data JPA generiert automatisch Implementierungen für:
    // - findAll() -> List<Training>
    // - findById(Long id) -> Optional<Training>
    // - save(Training training) -> Training
    // - deleteById(Long id) -> void
    // - existsById(Long id) -> boolean
    
    // Custom Query Methods basierend auf Ihrem Diagramm:
    
    /**
     * Findet alle Trainings eines bestimmten Athleten
     */
    List<Training> findByFkAthleteId(Long athleteId);
    
    /**
     * Findet alle Trainings eines bestimmten Sports
     */
    List<Training> findByFkSportId(Long sportId);
    
    /**
     * Findet Trainings eines Athleten für einen bestimmten Sport
     */
    List<Training> findByFkAthleteIdAndFkSportId(Long athleteId, Long sportId);
}