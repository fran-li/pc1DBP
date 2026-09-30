package pc1practica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LaboratoryRequestDto(
        @NotBlank
        @Size(max = 6)
        String name,

        @NotBlank
        String location,

        @NotBlank
        String status
) {
}