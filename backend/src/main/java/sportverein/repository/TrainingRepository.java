package sportverein.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sportverein.model.Training;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    
    // Spring Data JPA generiert automatisch Implementierungen für:
    // - findAll() -> List<Training>
    // - findById(Long id) -> Optional<Training>
    // - save(Training training) -> Training
    // - deleteById(Long id) -> void
    // - existsById(Long id) -> boolean
    
    /** 
     * Findet alle Trainings eines bestimmten Athleten
     */
    List<Training> findByAthleteId(Long athleteId);
    
    /**
     * Findet alle Trainings eines bestimmten Sports
     */
    List<Training> findBySportId(Long sportId);
    
    /**
     * Findet Trainings eines Athleten für einen bestimmten Sport
     */
    List<Training> findByAthleteIdAndSportId(Long athleteId, Long sportId);
}