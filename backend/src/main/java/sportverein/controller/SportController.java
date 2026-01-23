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
import sportverein.dto.CreateSportDto;
import sportverein.dto.ErrorResponse;
import sportverein.dto.SportDto;
import sportverein.dto.UpdateSportDto;
import sportverein.service.SportService;

@Slf4j
@RestController
@RequestMapping("/api/sports")
@CrossOrigin
@RequiredArgsConstructor
public class SportController {
    
    private final SportService sportService;
    
    /**
     * GET /api/sports
     * Gibt alle Sports zurück
     */
    @GetMapping
    public ResponseEntity<?> getSports() {
        try {
            List<SportDto> sports = sportService.findAll();
            log.info("Retrieved {} sports", sports.size());
            return ResponseEntity.ok(sports);
        } catch (Exception e) {
            log.error("Error retrieving sports", e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Abrufen der Sportarten: " + e.getMessage(),
                "/api/sports"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * GET /api/sports/{id}
     * Gibt einen bestimmten Sport zurück
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSportById(@PathVariable int id) {
        try {
            return sportService.findById(id)
                    .map(sport -> {
                        log.info("Retrieved sport with id {}", id);
                        return ResponseEntity.ok((Object) sport);
                    })
                    .orElseGet(() -> {
                        log.warn("Sport with id {} not found", id);
                        ErrorResponse error = new ErrorResponse(
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            "Sportart mit ID " + id + " wurde nicht gefunden",
                            "/api/sports/" + id
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                    });
        } catch (Exception e) {
            log.error("Error retrieving sport with id {}", id, e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Abrufen der Sportart: " + e.getMessage(),
                "/api/sports/" + id
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * POST /api/sports
     * Erstellt einen neuen Sport
     */
    @PostMapping
    public ResponseEntity<?> createSport(@RequestBody CreateSportDto dto) {
        try {
            if (dto.getName() == null || dto.getName().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Der Name der Sportart darf nicht leer sein",
                    "/api/sports"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            if (dto.getUnit() == null || dto.getUnit().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Die Einheit der Sportart darf nicht leer sein",
                    "/api/sports"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            SportDto created = sportService.create(dto);
            log.info("Created sport with id {}", created.getSportId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            log.error("Error creating sport", e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Erstellen der Sportart: " + e.getMessage(),
                "/api/sports"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * PUT /api/sports/{id}
     * Aktualisiert einen bestehenden Sport
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSport(
            @PathVariable int id,
            @RequestBody UpdateSportDto dto) {
        try {
            if (dto.getName() != null && dto.getName().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Der Name der Sportart darf nicht leer sein",
                    "/api/sports/" + id
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            if (dto.getUnit() != null && dto.getUnit().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Die Einheit der Sportart darf nicht leer sein",
                    "/api/sports/" + id
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            return sportService.update(id, dto)
                    .map(sport -> {
                        log.info("Updated sport with id {}", id);
                        return ResponseEntity.ok((Object) sport);
                    })
                    .orElseGet(() -> {
                        log.warn("Sport with id {} not found for update", id);
                        ErrorResponse error = new ErrorResponse(
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            "Sportart mit ID " + id + " wurde nicht gefunden und kann nicht aktualisiert werden",
                            "/api/sports/" + id
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                    });
        } catch (Exception e) {
            log.error("Error updating sport with id {}", id, e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Aktualisieren der Sportart: " + e.getMessage(),
                "/api/sports/" + id
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * DELETE /api/sports/{id}
     * Löscht einen Sport
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSport(@PathVariable int id) {
        try {
            boolean deleted = sportService.delete(id);
            if (deleted) {
                log.info("Deleted sport with id {}", id);
                return ResponseEntity.noContent().build();
            } else {
                log.warn("Sport with id {} not found for deletion", id);
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    "Sportart mit ID " + id + " wurde nicht gefunden und kann nicht gelöscht werden",
                    "/api/sports/" + id
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            log.error("Error deleting sport with id {}", id, e);
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Fehler beim Löschen der Sportart: " + e.getMessage(),
                "/api/sports/" + id
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}