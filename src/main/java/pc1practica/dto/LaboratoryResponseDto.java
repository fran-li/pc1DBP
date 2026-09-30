package pc1practica.dto;

public record LaboratoryResponseDto(
        Long id,
        String name,
        String location,
        Long managerId,
        String status
) {
}
