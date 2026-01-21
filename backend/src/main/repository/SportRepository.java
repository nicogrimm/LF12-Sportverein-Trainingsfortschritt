package com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.repository;

import com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {
    
    // Spring Data JPA generiert automatisch Implementierungen für:
    // - findAll() -> List<Sport>
    // - findById(Long id) -> Optional<Sport>
    // - save(Sport sport) -> Sport
    // - deleteById(Long id) -> void
    // - existsById(Long id) -> boolean
    
    Optional<Sport> findByName(String name);
}