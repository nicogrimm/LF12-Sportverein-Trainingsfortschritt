package sportverein.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sportverein.model.Athlete;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Integer> {
    
    // Spring Data JPA generiert automatisch Implementierungen für:
    // - findAll() -> List<Athlete>
    // - findById(Integer id) -> Optional<Athlete>
    // - save(Athlete athlete) -> Athlete
    // - deleteById(Integer id) -> void
    // - existsById(Integer id) -> boolean
    
    Optional<Athlete> findByFirstnameAndName(String firstname, String name);
}