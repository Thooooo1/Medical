package medical.bootstrap;

import medical.domain.AppUser;
import medical.domain.Doctor;
import medical.repo.DoctorRepository;
import medical.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner seedData(UserRepository userRepo, DoctorRepository doctorRepo) {
        return args -> {
            if (userRepo.count() == 0) {
                // Demo user used by the frontend (USER_ID = 1)
                AppUser u = userRepo.save(new AppUser("Demo User", "demo@example.com"));
                log.info("Seeded demo user id={}", u.getId());
            }

            if (doctorRepo.count() == 0) {
                doctorRepo.save(new Doctor("BS. Nguyễn Văn A", "Nội tổng quát"));
                doctorRepo.save(new Doctor("BS. Trần Thị B", "Nhi khoa"));
                doctorRepo.save(new Doctor("BS. Lê Văn C", "Da liễu"));
                log.info("Seeded demo doctors");
            }
        };
    }
}
