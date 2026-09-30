package pc1practica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterRequestDto(
        @NotBlank
        @Pattern(regexp = ".*[A-Z].*")
        String username,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$")
        String password
) {}