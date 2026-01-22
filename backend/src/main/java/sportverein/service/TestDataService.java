package sportverein.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sportverein.model.Athlete;
import sportverein.model.Sport;
import sportverein.model.Training;
import sportverein.repository.AthleteRepository;
import sportverein.repository.SportRepository;
import sportverein.repository.TrainingRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestDataService {

    private final AthleteRepository athleteRepository;
    private final SportRepository sportRepository;
    private final TrainingRepository trainingRepository;

    @Transactional
    public void loadTestData() {
        log.info("Starting to load test data...");

        // 1. Sports erstellen
        Sport laufen = new Sport();
        laufen.setName("Laufen");
        laufen.setUnit("km");
        laufen = sportRepository.save(laufen);
        log.info("Created sport: {}", laufen);

        Sport schwimmen = new Sport();
        schwimmen.setName("Schwimmen");
        schwimmen.setUnit("m");
        schwimmen = sportRepository.save(schwimmen);
        log.info("Created sport: {}", schwimmen);

        Sport radfahren = new Sport();
        radfahren.setName("Radfahren");
        radfahren.setUnit("km");
        radfahren = sportRepository.save(radfahren);
        log.info("Created sport: {}", radfahren);

        Sport rudern = new Sport();
        rudern.setName("Rudern");
        rudern.setUnit("m");
        rudern = sportRepository.save(rudern);
        log.info("Created sport: {}", rudern);

        // 2. Athletes erstellen
        Athlete max = new Athlete();
        max.setFirstname("Max");
        max.setName("Mustermann");
        max = athleteRepository.save(max);
        log.info("Created athlete: {}", max);

        Athlete anna = new Athlete();
        anna.setFirstname("Anna");
        anna.setName("Schmidt");
        anna = athleteRepository.save(anna);
        log.info("Created athlete: {}", anna);

        Athlete tom = new Athlete();
        tom.setFirstname("Tom");
        tom.setName("Meyer");
        tom = athleteRepository.save(tom);
        log.info("Created athlete: {}", tom);

        Athlete lisa = new Athlete();
        lisa.setFirstname("Lisa");
        lisa.setName("Weber");
        lisa = athleteRepository.save(lisa);
        log.info("Created athlete: {}", lisa);

        // 3. Trainings erstellen
        Training training1 = new Training();
        training1.setAthleteId(max.getAthleteId());
        training1.setSportId(laufen.getSportId());
        training1.setDate(OffsetDateTime.now().minusDays(7));
        training1.setMetric(5.5f);
        trainingRepository.save(training1);

        Training training2 = new Training();
        training2.setAthleteId(max.getAthleteId());
        training2.setSportId(laufen.getSportId());
        training2.setDate(OffsetDateTime.now().minusDays(5));
        training2.setMetric(7.2f);
        trainingRepository.save(training2);

        Training training3 = new Training();
        training3.setAthleteId(anna.getAthleteId());
        training3.setSportId(schwimmen.getSportId());
        training3.setDate(OffsetDateTime.now().minusDays(6));
        training3.setMetric(1200.0f);
        trainingRepository.save(training3);

        Training training4 = new Training();
        training4.setAthleteId(anna.getAthleteId());
        training4.setSportId(radfahren.getSportId());
        training4.setDate(OffsetDateTime.now().minusDays(4));
        training4.setMetric(25.0f);
        trainingRepository.save(training4);

        Training training5 = new Training();
        training5.setAthleteId(tom.getAthleteId());
        training5.setSportId(laufen.getSportId());
        training5.setDate(OffsetDateTime.now().minusDays(3));
        training5.setMetric(10.0f);
        trainingRepository.save(training5);

        Training training6 = new Training();
        training6.setAthleteId(lisa.getAthleteId());
        training6.setSportId(rudern.getSportId());
        training6.setDate(OffsetDateTime.now().minusDays(2));
        training6.setMetric(1500.0f);
        trainingRepository.save(training6);

        log.info("Test data loaded successfully! Created {} sports, {} athletes, {} trainings",
                sportRepository.count(), athleteRepository.count(), trainingRepository.count());
    }

    @Transactional
    public void clearAllData() {
        log.info("Starting to clear all data...");
        
        trainingRepository.deleteAll();
        athleteRepository.deleteAll();
        sportRepository.deleteAll();
        
        log.info("All data cleared successfully!");
    }
}