package pc1practica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.ZonedDateTime;

public record EquipmentSlotRequestDto(
        @NotBlank
        String equipmentCode,

        @NotNull
        @Positive
        Integer capacity,

        @NotNull
        ZonedDateTime startTime,

        @NotNull
        ZonedDateTime endTime
) {
}
