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
import sportverein.dto.AthleteDto;
import sportverein.dto.CreateAthleteDto;
import sportverein.dto.ErrorResponse;
import sportverein.dto.UpdateAthleteDto;
import sportverein.service.AthleteService;

@Slf4j
@RestController
@RequestMapping("/api/athletes")
@CrossOrigin
@RequiredArgsConstructor
public class AthleteController {
    
    private final AthleteService athleteService;

    /**
     * GET /api/athletes
     * Gibt alle Athleten zurück
     */
    @GetMapping
    public ResponseEntity<?> getAthletes() {
        try {
            List<AthleteDto> athletes = athleteService.findAll();
            log.info("Retrieved {} athletes", athletes.size());
            return ResponseEntity.ok(athletes);
        } catch (Exception e) {
            log.error("Error retrieving athletes", e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Abrufen der Athleten: " + e.getMessage(),
                "/api/athletes"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * GET /api/athletes/{id}
     * Gibt einen bestimmten Athleten zurück
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAthleteById(@PathVariable int id) {
        try {
            return athleteService.findById(id)
                    .map(athlete -> {
                        log.info("Retrieved athlete with id {}", id);
                        return ResponseEntity.ok((Object) athlete);
                    })
                    .orElseGet(() -> {
                        log.warn("Athlete with id {} not found", id);
                        ErrorResponse error = new ErrorResponse(
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            "Athlet mit ID " + id + " wurde nicht gefunden",
                            "/api/athletes/" + id
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                    });
        } catch (Exception e) {
            log.error("Error retrieving athlete with id {}", id, e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Abrufen des Athleten: " + e.getMessage(),
                "/api/athletes/" + id
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * POST /api/athletes
     * Erstellt einen neuen Athleten
     */
    @PostMapping
    public ResponseEntity<?> createAthlete(@RequestBody CreateAthleteDto dto) {
        try {
            if (dto.getFirstname() == null || dto.getFirstname().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Der Vorname des Athleten darf nicht leer sein",
                    "/api/athletes"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            if (dto.getName() == null || dto.getName().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Der Nachname des Athleten darf nicht leer sein",
                    "/api/athletes"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            AthleteDto created = athleteService.create(dto);
            log.info("Created athlete with id {}", created.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            log.error("Error creating athlete", e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Erstellen des Athleten: " + e.getMessage(),
                "/api/athletes"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * PUT /api/athletes/{id}
     * Aktualisiert einen bestehenden Athleten
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAthlete(
            @PathVariable int id,
            @RequestBody UpdateAthleteDto dto) {
        try {
            if (dto.getFirstname() != null && dto.getFirstname().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Der Vorname des Athleten darf nicht leer sein",
                    "/api/athletes/" + id
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            if (dto.getName() != null && dto.getName().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Der Nachname des Athleten darf nicht leer sein",
                    "/api/athletes/" + id
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            return athleteService.update(id, dto)
                    .map(athlete -> {
                        log.info("Updated athlete with id {}", id);
                        return ResponseEntity.ok((Object) athlete);
                    })
                    .orElseGet(() -> {
                        log.warn("Athlete with id {} not found for update", id);
                        ErrorResponse error = new ErrorResponse(
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            "Athlet mit ID " + id + " wurde nicht gefunden und kann nicht aktualisiert werden",
                            "/api/athletes/" + id
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                    });
        } catch (Exception e) {
            log.error("Error updating athlete with id {}", id, e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Aktualisieren des Athleten: " + e.getMessage(),
                "/api/athletes/" + id
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * DELETE /api/athletes/{id}
     * Löscht einen Athleten
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAthlete(@PathVariable int id) {
        try {
            boolean deleted = athleteService.delete(id);
            if (deleted) {
                log.info("Deleted athlete with id {}", id);
                return ResponseEntity.noContent().build();
            } else {
                log.warn("Athlete with id {} not found for deletion", id);
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    "Athlet mit ID " + id + " wurde nicht gefunden und kann nicht gelöscht werden",
                    "/api/athletes/" + id
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            log.error("Error deleting athlete with id {}", id, e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Löschen des Athleten: " + e.getMessage(),
                "/api/athletes/" + id
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}