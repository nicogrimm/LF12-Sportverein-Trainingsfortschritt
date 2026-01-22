package sportverein.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sportverein.service.TestDataService;

@RestController
@RequestMapping("/api/test-data")
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class TestDataController {

    private final TestDataService testDataService;

    @PostMapping("/load")
    public ResponseEntity<String> loadTestData() {
        try {
            testDataService.loadTestData();
            return ResponseEntity.ok("Test data loaded successfully!");
        } catch (Exception e) {
            log.error("Error loading test data", e);
            return ResponseEntity.internalServerError()
                    .body("Error loading test data: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearTestData() {
        try {
            testDataService.clearAllData();
            return ResponseEntity.ok("All data cleared successfully!");
        } catch (Exception e) {
            log.error("Error clearing data", e);
            return ResponseEntity.internalServerError()
                    .body("Error clearing data: " + e.getMessage());
        }
    }
}