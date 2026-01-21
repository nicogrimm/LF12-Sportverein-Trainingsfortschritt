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
    public ResponseEntity<List<SportDto>> getSports() {
        List<SportDto> sports = sportService.findAll();
        return ResponseEntity.ok(sports);
    }
    
    /**
     * GET /api/sports/{id}
     * Gibt einen bestimmten Sport zurück
     */
    @GetMapping("/{id}")
    public ResponseEntity<SportDto> getSportById(@PathVariable Long id) {
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
        boolean deleted = sportService.delete(id);
        return deleted 
                ? ResponseEntity.noContent().build() 
                : ResponseEntity.notFound().build();
    }
}