package medical.service;

import medical.domain.AppUser;
import medical.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser create(AppUser user) {
        return userRepository.save(user);
    }

    public List<AppUser> getAll() {
        return userRepository.findAll();
    }
}
