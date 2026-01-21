package sportverein.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sportverein.model.Athlete;

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