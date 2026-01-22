package sportverein.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sportverein.dto.CreateSportDto;
import sportverein.dto.SportDto;
import sportverein.dto.UpdateSportDto;
import sportverein.model.Sport;
import sportverein.repository.SportRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SportService {
    
    private final SportRepository sportRepository;
    
    /**
     * Gibt alle Sports zurück
     */
    public List<SportDto> findAll() {
        return sportRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Gibt einen Sport anhand der ID zurück
     */
    public Optional<SportDto> findById(int id) {
        return sportRepository.findById(id)
                .map(this::convertToDto);
    }
    
    /**
     * Erstellt einen neuen Sport
     */
    @Transactional
    public SportDto create(CreateSportDto dto) {
        
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
    public Optional<SportDto> update(int id, UpdateSportDto dto) {
        
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
    public boolean delete(int id) {
        
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
        dto.setId(sport.getSportId());
        dto.setName(sport.getName());
        dto.setUnit(sport.getUnit());
        return dto;
    }
}