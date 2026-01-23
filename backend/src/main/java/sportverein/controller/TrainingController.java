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
import sportverein.dto.ErrorResponse;
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
    public ResponseEntity<?> getTrainingsForAthlete(@PathVariable int athleteId) {
        try {
            List<TrainingDto> trainings = trainingService.findByAthleteId(athleteId);
            log.info("Retrieved {} trainings for athlete {}", trainings.size(), athleteId);
            return ResponseEntity.ok(trainings);
        } catch (Exception e) {
            log.error("Error retrieving trainings for athlete {}", athleteId, e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Abrufen der Trainings für Athlet " + athleteId + ": " + e.getMessage(),
                "/api/trainings/athlete/" + athleteId
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * GET /api/trainings/sport/{sportId}
     * Gibt alle Trainings einer Sportart zurück
     */
    @GetMapping("/sport/{sportId}")
    public ResponseEntity<?> getTrainingsForSport(@PathVariable int sportId) {
        try {
            List<TrainingDto> trainings = trainingService.findBySportId(sportId);
            log.info("Retrieved {} trainings for sport {}", trainings.size(), sportId);
            return ResponseEntity.ok(trainings);
        } catch (Exception e) {
            log.error("Error retrieving trainings for sport {}", sportId, e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Abrufen der Trainings für Sport " + sportId + ": " + e.getMessage(),
                "/api/trainings/sport/" + sportId
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * GET /api/trainings/athlete/{athleteId}/training/{trainingId}
     * Gibt ein bestimmtes Training eines Athleten zurück
     */
    @GetMapping("/athlete/{athleteId}/training/{trainingId}")
    public ResponseEntity<?> getTrainingForAthlete(
            @PathVariable int athleteId,
            @PathVariable int trainingId) {
        try {
            return trainingService.findByAthleteIdAndTrainingId(athleteId, trainingId)
                    .map(training -> {
                        log.info("Retrieved training {} for athlete {}", trainingId, athleteId);
                        return ResponseEntity.ok((Object) training);
                    })
                    .orElseGet(() -> {
                        log.warn("Training {} for athlete {} not found", trainingId, athleteId);
                        ErrorResponse error = new ErrorResponse(
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            "Training mit ID " + trainingId + " für Athlet " + athleteId + " wurde nicht gefunden",
                            "/api/trainings/athlete/" + athleteId + "/training/" + trainingId
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                    });
        } catch (Exception e) {
            log.error("Error retrieving training {} for athlete {}", trainingId, athleteId, e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Abrufen des Trainings: " + e.getMessage(),
                "/api/trainings/athlete/" + athleteId + "/training/" + trainingId
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * POST /api/trainings/athlete/{athleteId}
     * Erstellt ein neues Training für einen Athleten
     */
    @PostMapping("/athlete/{athleteId}")
    public ResponseEntity<?> createTrainingForAthlete(
            @PathVariable int athleteId,
            @RequestBody CreateTrainingDto dto) {
        try {
            if (dto.getSportId() <= 0) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Ungültige Sportart-ID",
                    "/api/trainings/athlete/" + athleteId
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            if (dto.getDate() == null) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Das Trainingsdatum darf nicht leer sein",
                    "/api/trainings/athlete/" + athleteId
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            if (dto.getMetric() <= 0) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Der Metrikwert muss größer als 0 sein",
                    "/api/trainings/athlete/" + athleteId
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            TrainingDto created = trainingService.createForAthlete(athleteId, dto);
            log.info("Created training {} for athlete {}", created.getId(), athleteId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid data for creating training for athlete {}: {}", athleteId, e.getMessage());
            ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Ungültige Daten: " + e.getMessage(),
                "/api/trainings/athlete/" + athleteId
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            log.error("Error creating training for athlete {}", athleteId, e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Erstellen des Trainings: " + e.getMessage(),
                "/api/trainings/athlete/" + athleteId
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * PUT /api/trainings/athlete/{athleteId}/training/{trainingId}
     * Aktualisiert ein Training eines Athleten
     */
    @PutMapping("/athlete/{athleteId}/training/{trainingId}")
    public ResponseEntity<?> updateTrainingForAthlete(
            @PathVariable int athleteId,
            @PathVariable int trainingId,
            @RequestBody UpdateTrainingDto dto) {
        try {
            if (dto.getSportId() <= 0) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Ungültige Sportart-ID",
                    "/api/trainings/athlete/" + athleteId + "/training/" + trainingId
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            if (dto.getMetric() <= 0) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Der Metrikwert muss größer als 0 sein",
                    "/api/trainings/athlete/" + athleteId + "/training/" + trainingId
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            return trainingService.updateForAthlete(athleteId, trainingId, dto)
                    .map(training -> {
                        log.info("Updated training {} for athlete {}", trainingId, athleteId);
                        return ResponseEntity.ok((Object) training);
                    })
                    .orElseGet(() -> {
                        log.warn("Training {} for athlete {} not found for update", trainingId, athleteId);
                        ErrorResponse error = new ErrorResponse(
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            "Training mit ID " + trainingId + " für Athlet " + athleteId + " wurde nicht gefunden und kann nicht aktualisiert werden",
                            "/api/trainings/athlete/" + athleteId + "/training/" + trainingId
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                    });
        } catch (IllegalArgumentException e) {
            log.warn("Invalid data for updating training {} for athlete {}: {}", trainingId, athleteId, e.getMessage());
            ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Ungültige Daten: " + e.getMessage(),
                "/api/trainings/athlete/" + athleteId + "/training/" + trainingId
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            log.error("Error updating training {} for athlete {}", trainingId, athleteId, e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Aktualisieren des Trainings: " + e.getMessage(),
                "/api/trainings/athlete/" + athleteId + "/training/" + trainingId
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * DELETE /api/trainings/athlete/{athleteId}/training/{trainingId}
     * Löscht ein Training eines Athleten
     */
    @DeleteMapping("/athlete/{athleteId}/training/{trainingId}")
    public ResponseEntity<?> deleteTrainingForAthlete(
            @PathVariable int athleteId,
            @PathVariable int trainingId) {
        try {
            return trainingService.findByAthleteIdAndTrainingId(athleteId, trainingId)
                    .map(training -> {
                        trainingService.delete(trainingId);
                        log.info("Deleted training {} for athlete {}", trainingId, athleteId);
                        return ResponseEntity.noContent().<Object>build();
                    })
                    .orElseGet(() -> {
                        log.warn("Training {} for athlete {} not found for deletion", trainingId, athleteId);
                        ErrorResponse error = new ErrorResponse(
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            "Training mit ID " + trainingId + " für Athlet " + athleteId + " wurde nicht gefunden und kann nicht gelöscht werden",
                            "/api/trainings/athlete/" + athleteId + "/training/" + trainingId
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                    });
        } catch (Exception e) {
            log.error("Error deleting training {} for athlete {}", trainingId, athleteId, e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Löschen des Trainings: " + e.getMessage(),
                "/api/trainings/athlete/" + athleteId + "/training/" + trainingId
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * GET /api/trainings
     * Gibt alle Trainings zurück (Optional - für Übersicht)
     */
    @GetMapping
    public ResponseEntity<?> getAllTrainings() {
        try {
            List<TrainingDto> trainings = trainingService.findAll();
            log.info("Retrieved {} trainings", trainings.size());
            return ResponseEntity.ok(trainings);
        } catch (Exception e) {
            log.error("Error retrieving all trainings", e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Abrufen aller Trainings: " + e.getMessage(),
                "/api/trainings"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}