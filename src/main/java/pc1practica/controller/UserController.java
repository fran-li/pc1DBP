package pc1practica.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pc1practica.dto.UserIdResponseDto;
import pc1practica.dto.UserRegisterRequestDto;
import pc1practica.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserIdResponseDto> register(
            @Valid @RequestBody UserRegisterRequestDto request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.register(request));
    }
}


