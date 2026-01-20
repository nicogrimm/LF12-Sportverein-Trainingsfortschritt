package controller;

import dto.AthleteDto;
import dto.CreateAthleteDto;
import dto.UpdateAthleteDto;
import service.AthleteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<AthleteDto>> getAthletes() {
        log.info("GET request to /api/athletes");
        List<AthleteDto> athletes = athleteService.findAll();
        return ResponseEntity.ok(athletes);
    }
    
    /**
     * GET /api/athletes/{id}
     * Gibt einen bestimmten Athleten zurück
     */
    @GetMapping("/{id}")
    public ResponseEntity<AthleteDto> getAthleteById(@PathVariable Long id) {
        log.info("GET request to /api/athletes/{}", id);
        return athleteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * POST /api/athletes
     * Erstellt einen neuen Athleten
     */
    @PostMapping
    public ResponseEntity<AthleteDto> createAthlete(@RequestBody CreateAthleteDto dto) {
        log.info("POST request to /api/athletes with body: {}", dto);
        AthleteDto created = athleteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * PUT /api/athletes/{id}
     * Aktualisiert einen bestehenden Athleten
     */
    @PutMapping("/{id}")
    public ResponseEntity<AthleteDto> updateAthlete(
            @PathVariable Long id,
            @RequestBody UpdateAthleteDto dto) {
        log.info("PUT request to /api/athletes/{} with body: {}", id, dto);
        return athleteService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * DELETE /api/athletes/{id}
     * Löscht einen Athleten
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAthlete(@PathVariable Long id) {
        log.info("DELETE request to /api/athletes/{}", id);
        boolean deleted = athleteService.delete(id);
        return deleted 
                ? ResponseEntity.noContent().build() 
                : ResponseEntity.notFound().build();
    }
}