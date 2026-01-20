package medical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * We intentionally scan only one set of JPA entities/repositories.
 * The project currently contains legacy classes in both `medical.domain` and `medical.entity`.
 * Scanning both will cause duplicate @Entity mappings (same table name) and break at runtime.
 */
@SpringBootApplication(scanBasePackages = "medical")
@EntityScan(basePackages = "medical.entity")
@EnableJpaRepositories(basePackages = "medical.repository")
public class MedicalServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalServicesApplication.class, args);
    }
}
