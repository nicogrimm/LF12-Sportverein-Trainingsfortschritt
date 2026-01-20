package service;

import dto.AthleteDto;
import dto.CreateAthleteDto;
import dto.UpdateAthleteDto;
import model.Athlete;
import repository.AthleteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AthleteService {
    
    private final AthleteRepository athleteRepository;
    
    /**
     * Gibt alle Athleten zurück
     */
    public List<AthleteDto> findAll() {
        log.info("Fetching all athletes");
        return athleteRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Gibt einen Athleten anhand der ID zurück
     */
    public Optional<AthleteDto> findById(Long id) {
        log.info("Fetching athlete with id: {}", id);
        return athleteRepository.findById(id)
                .map(this::convertToDto);
    }
    
    /**
     * Erstellt einen neuen Athleten
     */
    @Transactional
    public AthleteDto create(CreateAthleteDto dto) {
        log.info("Creating new athlete: {}", dto);
        
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
        log.info("Updating athlete with id: {}", id);
        
        return athleteRepository.findById(id)
                .map(athlete -> {
                    athlete.setFirstname(dto.getFirstname());
                    athlete.setName(dto.getName());
                    Athlete updated = athleteRepository.save(athlete);
                    return convertToDto(updated);
                });
    }
    
    /**
     * Löscht einen Athleten (nur wenn keine Trainings vorhanden)
     */
    @Transactional
    public boolean delete(Long id) {
        log.info("Deleting athlete with id: {}", id);
        
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