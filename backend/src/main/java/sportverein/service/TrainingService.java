package sportverein.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sportverein.dto.CreateTrainingDto;
import sportverein.dto.TrainingDto;
import sportverein.dto.UpdateTrainingDto;
import sportverein.model.Training;
import sportverein.repository.AthleteRepository;
import sportverein.repository.SportRepository;
import sportverein.repository.TrainingRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingService {
    
    private final TrainingRepository trainingRepository;
    private final AthleteRepository athleteRepository;
    private final SportRepository sportRepository;
    
    /**
     * Gibt alle Trainings zurück
     */
    public List<TrainingDto> findAll() {
        return trainingRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Gibt alle Trainings eines Athleten zurück
     */
    public List<TrainingDto> findByAthleteId(Long athleteId) {
        return trainingRepository.findByAthleteId(athleteId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Gibt ein Training anhand der ID zurück
     */
    public Optional<TrainingDto> findById(Long trainingId) {
        return trainingRepository.findById(trainingId)
                .map(this::convertToDto);
    }
    
    /**
     * Gibt ein spezifisches Training eines Athleten zurück
     */
    public Optional<TrainingDto> findByAthleteIdAndTrainingId(Long athleteId, Long trainingId) {
        return trainingRepository.findById(trainingId)
                .filter(training -> training.getAthleteId().equals(athleteId))
                .map(this::convertToDto);
    }
    
    /**
     * Erstellt ein neues Training für einen Athleten
     */
    @Transactional
    public TrainingDto createForAthlete(Long athleteId, CreateTrainingDto dto) {
        
        // Validierung: Athlete muss existieren
        if (!athleteRepository.existsById(athleteId)) {
            throw new IllegalArgumentException("Athlete with id " + athleteId + " not found");
        }
        
        // Validierung: Sport muss existieren
        if (!sportRepository.existsById(dto.getSportId())) {
            throw new IllegalArgumentException("Sport with id " + dto.getSportId() + " not found");
        }
        
        Training training = new Training();
        training.setAthleteId(athleteId);
        training.setSportId(dto.getSportId());
        training.setDate(dto.getDate());
        training.setMetric(dto.getMetric());
        
        Training saved = trainingRepository.save(training);
        return convertToDto(saved);
    }
    
    /**
     * Aktualisiert ein Training eines Athleten
     */
    @Transactional
    public Optional<TrainingDto> updateForAthlete(Long athleteId, Long trainingId, UpdateTrainingDto dto) {
        
        return trainingRepository.findById(trainingId)
                .filter(training -> training.getAthleteId().equals(athleteId))
                .map(training -> {
                    // Validierung: Sport muss existieren
                    if (!sportRepository.existsById(dto.getSportId())) {
                        throw new IllegalArgumentException("Sport with id " + dto.getSportId() + " not found");
                    }
                    
                    training.setSportId(dto.getSportId());
                    training.setDate(dto.getDate());
                    training.setMetric(dto.getMetric());
                    
                    Training updated = trainingRepository.save(training);
                    return convertToDto(updated);
                });
    }
    
    /**
     * Löscht ein Training
     */
    @Transactional
    public boolean delete(Long trainingId) {
        
        if (trainingRepository.existsById(trainingId)) {
            trainingRepository.deleteById(trainingId);
            return true;
        }
        return false;
    }
    
    /**
     * Konvertiert Entity zu DTO
     */
    private TrainingDto convertToDto(Training training) {
        TrainingDto dto = new TrainingDto();
        dto.setId(training.getTrainingId());
        dto.setAthleteId(training.getAthleteId());
        dto.setSportId(training.getSportId());
        dto.setDate(training.getDate());
        dto.setMetric(training.getMetric());
        return dto;
    }
}