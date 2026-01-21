package sportverein.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sportverein.dto.AthleteDto;
import sportverein.dto.CreateAthleteDto;
import sportverein.dto.UpdateAthleteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sportverein.model.Athlete;
import sportverein.repository.AthleteRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AthleteService {
    
    private final AthleteRepository athleteRepository;
    
    /**
     * Gibt alle Athleten zurück
     */
    public List<AthleteDto> findAll() {
        return athleteRepository.findAll().stream()
                .map(athlete -> convertToDto((Athlete) athlete))
                .collect(Collectors.toList());
    }
    
    /**
     * Gibt einen Athleten anhand der ID zurück
     */
    public Optional<AthleteDto> findById(Long id) {
        return athleteRepository.findById(id)
                .map(athlete -> convertToDto((Athlete) athlete));
    }
    
    /**
     * Erstellt einen neuen Athleten
     */
    @Transactional
    public AthleteDto create(CreateAthleteDto dto) {
        
        Athlete athlete = new Athlete();
        athlete.setFirstname(dto.getFirstname());
        athlete.setName(dto.getName());
        
        Athlete saved = athleteRepository.save(athlete);
        return convertToDto(saved);
    }
    
    /**
     * Aktualisiert einen bestehenden Athleten
     */
    @Transactional
    public Optional<AthleteDto> update(Long id, UpdateAthleteDto dto) {
        
        return athleteRepository.findById(id)
                .map(athlete -> {
                    Athlete a = (Athlete) athlete;
                    a.setFirstname(dto.getFirstname());
                    a.setName(dto.getName());
                    Athlete updated = athleteRepository.save(a);
                    return convertToDto(updated);
                });
    }
    
    /**
     * Löscht einen Athleten (nur wenn keine Trainings vorhanden)
     */
    @Transactional
    public boolean delete(Long id) {
        
        if (athleteRepository.existsById(id)) {
            athleteRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Konvertiert Entity zu DTO
     */
    private AthleteDto convertToDto(Athlete athlete) {
        AthleteDto dto = new AthleteDto();
        dto.setId(athlete.getPkAthleteId());
        dto.setFirstname(athlete.getFirstname());
        dto.setName(athlete.getName());
        return dto;
    }
}