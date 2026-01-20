package repository;

import com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.model.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    
    // Spring Data JPA generiert automatisch Implementierungen für:
    // - findAll() -> List<Athlete>
    // - findById(Long id) -> Optional<Athlete>
    // - save(Athlete athlete) -> Athlete
    // - deleteById(Long id) -> void
    // - existsById(Long id) -> boolean
    
    Optional<Athlete> findByFirstnameAndName(String firstname, String name);
}