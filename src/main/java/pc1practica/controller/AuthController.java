package pc1practica.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pc1practica.dto.LoginRequestDto;
import pc1practica.dto.TokenResponseDto;
import pc1practica.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public TokenResponseDto login(
            @Valid @RequestBody LoginRequestDto request) {

        return userService.login(request);
    }
}