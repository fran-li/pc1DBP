package pc1practica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LabReservationRequestDto(
        @NotNull
        Long slotId,

        @NotBlank
        String purpose
) {
}

