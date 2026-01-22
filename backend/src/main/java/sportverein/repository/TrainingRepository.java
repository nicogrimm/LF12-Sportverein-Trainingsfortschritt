package sportverein.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sportverein.model.Training;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {
    
    // Spring Data JPA generiert automatisch Implementierungen für:
    // - findAll() -> List<Training>
    // - findById(Integer id) -> Optional<Training>
    // - save(Training training) -> Training
    // - deleteById(Integer id) -> void
    // - existsById(Integer id) -> boolean
    
    /** 
     * Findet alle Trainings eines bestimmten Athleten
     */
    List<Training> findByAthleteId(Integer athleteId);
    
    /**
     * Findet alle Trainings eines bestimmten Sports
     */
    List<Training> findBySportId(Integer sportId);
    
    /**
     * Findet Trainings eines Athleten für einen bestimmten Sport
     */
    List<Training> findByAthleteIdAndSportId(Integer athleteId, Integer sportId);
}