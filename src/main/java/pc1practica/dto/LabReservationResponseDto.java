package pc1practica.dto;

import java.time.ZonedDateTime;

public record LabReservationResponseDto(
        Long id,
        Long slotId,
        Long studentId,
        String purpose,
        ZonedDateTime reservedAt,
        String status
) {
}