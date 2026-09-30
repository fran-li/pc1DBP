package pc1practica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.jspecify.annotations.Nullable;

public record LoginRequestDto(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {}

