package controller;

import dto.CreateTrainingDto;
import dto.TrainingDto;
import dto.UpdateTrainingDto;
import service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/trainings")
@CrossOrigin
@RequiredArgsConstructor
public class TrainingController {
    
    private final TrainingService trainingService;
    
    /**
     * GET /api/trainings/athlete/{athleteId}
     * Gibt alle Trainings eines Athleten zurück
     */
    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<List<TrainingDto>> getTrainingsForAthlete(@PathVariable Long athleteId) {
        log.info("GET request to /api/trainings/athlete/{}", athleteId);
        List<TrainingDto> trainings = trainingService.findByAthleteId(athleteId);
        return ResponseEntity.ok(trainings);
    }
    
    /**
     * GET /api/trainings/athlete/{athleteId}/training/{trainingId}
     * Gibt ein bestimmtes Training eines Athleten zurück
     */
    @GetMapping("/athlete/{athleteId}/training/{trainingId}")
    public ResponseEntity<TrainingDto> getTrainingForAthlete(
            @PathVariable Long athleteId,
            @PathVariable Long trainingId) {
        log.info("GET request to /api/trainings/athlete/{}/training/{}", athleteId, trainingId);
        return trainingService.findByAthleteIdAndTrainingId(athleteId, trainingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * POST /api/trainings/athlete/{athleteId}
     * Erstellt ein neues Training für einen Athleten
     */
    @PostMapping("/athlete/{athleteId}")
    public ResponseEntity<TrainingDto> createTrainingForAthlete(
            @PathVariable Long athleteId,
            @RequestBody CreateTrainingDto dto) {
        log.info("POST request to /api/trainings/athlete/{} with body: {}", athleteId, dto);
        try {
            TrainingDto created = trainingService.createForAthlete(athleteId, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            log.error("Error creating training: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * PUT /api/trainings/athlete/{athleteId}/training/{trainingId}
     * Aktualisiert ein Training eines Athleten
     */
    @PutMapping("/athlete/{athleteId}/training/{trainingId}")
    public ResponseEntity<TrainingDto> updateTrainingForAthlete(
            @PathVariable Long athleteId,
            @PathVariable Long trainingId,
            @RequestBody UpdateTrainingDto dto) {
        log.info("PUT request to /api/trainings/athlete/{}/training/{} with body: {}", 
                athleteId, trainingId, dto);
        try {
            return trainingService.updateForAthlete(athleteId, trainingId, dto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            log.error("Error updating training: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * GET /api/trainings
     * Gibt alle Trainings zurück (Optional - für Übersicht)
     */
    @GetMapping
    public ResponseEntity<List<TrainingDto>> getAllTrainings() {
        log.info("GET request to /api/trainings");
        List<TrainingDto> trainings = trainingService.findAll();
        return ResponseEntity.ok(trainings);
    }
}