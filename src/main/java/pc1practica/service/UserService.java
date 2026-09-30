package pc1practica.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pc1practica.dto.LoginRequestDto;
import pc1practica.dto.TokenResponseDto;
import pc1practica.dto.UserIdResponseDto;
import pc1practica.dto.UserRegisterRequestDto;

import pc1practica.entity.User;
import pc1practica.repository.UserRepository;

import java.time.Instant;

@Service
public class UserService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public UserService(
            UserRepository users,
            PasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder
    ) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public UserIdResponseDto register(
            UserRegisterRequestDto request
    ) {

        String email = request.email()
                .trim()
                .toLowerCase();

        if (users.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ya tiene registrado correo"
            );
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(request.password())
        );
        user.setRole("STUDENT");

        user = users.save(user);

        return new UserIdResponseDto(user.getId());
    }

    public TokenResponseDto login(
            LoginRequestDto request
    ) {

        String email = request.email()
                .trim()
                .toLowerCase();

        User user = users.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "email desco"
                        )
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "mala contra"
            );
        }

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .build();

        String token = jwtEncoder
                .encode(
                        JwtEncoderParameters.from(claims)
                )
                .getTokenValue();

        return new TokenResponseDto(token);
    }
}



