package sportverein.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sportverein.dto.CreateTrainingDto;
import sportverein.dto.TrainingDto;
import sportverein.dto.UpdateTrainingDto;
import sportverein.service.TrainingService;

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
    public ResponseEntity<List<TrainingDto>> getTrainingsForAthlete(@PathVariable int athleteId) {
        List<TrainingDto> trainings = trainingService.findByAthleteId(athleteId);
        return ResponseEntity.ok(trainings);
    }
    
    /**
     * GET /api/trainings/athlete/{athleteId}/training/{trainingId}
     * Gibt ein bestimmtes Training eines Athleten zurück
     */
    @GetMapping("/athlete/{athleteId}/training/{trainingId}")
    public ResponseEntity<TrainingDto> getTrainingForAthlete(
            @PathVariable int athleteId,
            @PathVariable int trainingId) {
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
            @PathVariable int athleteId,
            @RequestBody CreateTrainingDto dto) {
        try {
            TrainingDto created = trainingService.createForAthlete(athleteId, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * PUT /api/trainings/athlete/{athleteId}/training/{trainingId}
     * Aktualisiert ein Training eines Athleten
     */
    @PutMapping("/athlete/{athleteId}/training/{trainingId}")
    public ResponseEntity<TrainingDto> updateTrainingForAthlete(
            @PathVariable int athleteId,
            @PathVariable int trainingId,
            @RequestBody UpdateTrainingDto dto) {
        try {
            return trainingService.updateForAthlete(athleteId, trainingId, dto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE /api/trainings/athlete/{athleteId}/training/{trainingId}
     * Löscht ein Training eines Athleten
     */
    @DeleteMapping("/athlete/{athleteId}/training/{trainingId}")
    public ResponseEntity<Void> deleteTrainingForAthlete(
            @PathVariable int athleteId,
            @PathVariable int trainingId) {
        
        return trainingService.findByAthleteIdAndTrainingId(athleteId, trainingId)
                .map(training -> {
                    trainingService.delete(trainingId);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET /api/trainings
     * Gibt alle Trainings zurück (Optional - für Übersicht)
     */
    @GetMapping
    public ResponseEntity<List<TrainingDto>> getAllTrainings() {
        List<TrainingDto> trainings = trainingService.findAll();
        return ResponseEntity.ok(trainings);
    }
}