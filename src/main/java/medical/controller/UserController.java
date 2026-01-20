package medical.controller;

import medical.domain.AppUser;
import medical.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public AppUser createUser(@RequestBody AppUser user) {
        return userService.create(user);
    }

    @GetMapping
    public List<AppUser> getAllUsers() {
        return userService.getAll();
    }
}
