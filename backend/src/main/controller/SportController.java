package controller;

import dto.CreateSportDto;
import dto.SportDto;
import dto.UpdateSportDto;
import service.SportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<SportDto>> getSports() {
        log.info("GET request to /api/sports");
        List<SportDto> sports = sportService.findAll();
        return ResponseEntity.ok(sports);
    }
    
    /**
     * GET /api/sports/{id}
     * Gibt einen bestimmten Sport zurück
     */
    @GetMapping("/{id}")
    public ResponseEntity<SportDto> getSportById(@PathVariable Long id) {
        log.info("GET request to /api/sports/{}", id);
        return sportService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * POST /api/sports
     * Erstellt einen neuen Sport
     */
    @PostMapping
    public ResponseEntity<SportDto> createSport(@RequestBody CreateSportDto dto) {
        log.info("POST request to /api/sports with body: {}", dto);
        SportDto created = sportService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * PUT /api/sports/{id}
     * Aktualisiert einen bestehenden Sport
     */
    @PutMapping("/{id}")
    public ResponseEntity<SportDto> updateSport(
            @PathVariable Long id,
            @RequestBody UpdateSportDto dto) {
        log.info("PUT request to /api/sports/{} with body: {}", id, dto);
        return sportService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * DELETE /api/sports/{id}
     * Löscht einen Sport
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSport(@PathVariable Long id) {
        log.info("DELETE request to /api/sports/{}", id);
        boolean deleted = sportService.delete(id);
        return deleted 
                ? ResponseEntity.noContent().build() 
                : ResponseEntity.notFound().build();
    }
}