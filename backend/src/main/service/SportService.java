package service;

import dto.CreateSportDto;
import dto.SportDto;
import dto.UpdateSportDto;
import model.Sport;
import repository.SportRepository;
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
public class SportService {
    
    private final SportRepository sportRepository;
    
    /**
     * Gibt alle Sports zurück
     */
    public List<SportDto> findAll() {
        log.info("Fetching all sports");
        return sportRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Gibt einen Sport anhand der ID zurück
     */
    public Optional<SportDto> findById(Long id) {
        log.info("Fetching sport with id: {}", id);
        return sportRepository.findById(id)
                .map(this::convertToDto);
    }
    
    /**
     * Erstellt einen neuen Sport
     */
    @Transactional
    public SportDto create(CreateSportDto dto) {
        log.info("Creating new sport: {}", dto);
        
        Sport sport = new Sport();
        sport.setName(dto.getName());
        sport.setUnit(dto.getUnit());
        
        Sport saved = sportRepository.save(sport);
        return convertToDto(saved);
    }
    
    /**
     * Aktualisiert einen bestehenden Sport
     */
    @Transactional
    public Optional<SportDto> update(Long id, UpdateSportDto dto) {
        log.info("Updating sport with id: {}", id);
        
        return sportRepository.findById(id)
                .map(sport -> {
                    sport.setName(dto.getName());
                    sport.setUnit(dto.getUnit());
                    Sport updated = sportRepository.save(sport);
                    return convertToDto(updated);
                });
    }
    
    /**
     * Löscht einen Sport
     */
    @Transactional
    public boolean delete(Long id) {
        log.info("Deleting sport with id: {}", id);
        
        if (sportRepository.existsById(id)) {
            sportRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Konvertiert Entity zu DTO
     */
    private SportDto convertToDto(Sport sport) {
        SportDto dto = new SportDto();
        dto.setId(sport.getPkSportId());
        dto.setName(sport.getName());
        dto.setUnit(sport.getUnit());
        return dto;
    }
}