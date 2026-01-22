package sportverein;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "sportverein.controller",
    "sportverein.service",
    "sportverein.repository",
    "sportverein.model",
    "sportverein.dto",
    "sportverein.config"
})
@EnableJpaRepositories(basePackages = "sportverein.repository")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}