package pc1practica.dto;

import java.time.ZonedDateTime;

public record EquipmentSlotResponseDto(
        Long id,
        Long laboratoryId,
        String equipmentCode,
        Integer capacity,
        ZonedDateTime startTime,
        ZonedDateTime endTime,
        String status
) {
}
