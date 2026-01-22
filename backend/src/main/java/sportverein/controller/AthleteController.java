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
    public ResponseEntity<List<AthleteDto>> getAthletes() {
        List<AthleteDto> athletes = athleteService.findAll();
        return ResponseEntity.ok(athletes);
    }
    
    /**
     * GET /api/athletes/{id}
     * Gibt einen bestimmten Athleten zurück
     */
    @GetMapping("/{id}")
    public ResponseEntity<AthleteDto> getAthleteById(@PathVariable int id) {
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
        AthleteDto created = athleteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * PUT /api/athletes/{id}
     * Aktualisiert einen bestehenden Athleten
     */
    @PutMapping("/{id}")
    public ResponseEntity<AthleteDto> updateAthlete(
            @PathVariable int id,
            @RequestBody UpdateAthleteDto dto) {
        return athleteService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * DELETE /api/athletes/{id}
     * Löscht einen Athleten
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAthlete(@PathVariable int id) {
        boolean deleted = athleteService.delete(id);
        return deleted 
                ? ResponseEntity.noContent().build() 
                : ResponseEntity.notFound().build();
    }
}