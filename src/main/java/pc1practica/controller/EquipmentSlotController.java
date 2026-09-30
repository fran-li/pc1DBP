package pc1practica.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pc1practica.dto.EquipmentSlotRequestDto;
import pc1practica.dto.EquipmentSlotResponseDto;
import pc1practica.service.EquipmentSlotService;

import java.util.List;

@RestController
public class EquipmentSlotController {

    private final EquipmentSlotService equipmentSlotService;

    public EquipmentSlotController(EquipmentSlotService equipmentSlotService) {
        this.equipmentSlotService = equipmentSlotService;
    }

    @PostMapping("/laboratories/{laboratoryId}/slots")
    @ResponseStatus(HttpStatus.CREATED)
    public EquipmentSlotResponseDto create(
            @PathVariable Long laboratoryId,
            @Valid @RequestBody EquipmentSlotRequestDto request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return equipmentSlotService.create(
                laboratoryId,
                request,
                currentUserId(jwt)
        );
    }

    @GetMapping("/laboratories/{laboratoryId}/slots")
    public List<EquipmentSlotResponseDto> findByLaboratory(
            @PathVariable Long laboratoryId
    ) {
        return equipmentSlotService.findByLaboratory(laboratoryId);
    }

    @GetMapping("/slots/{id}")
    public EquipmentSlotResponseDto findById(@PathVariable Long id) {
        return equipmentSlotService.findById(id);
    }

    @PutMapping("/slots/{id}")
    public EquipmentSlotResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentSlotRequestDto request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return equipmentSlotService.update(id, request, currentUserId(jwt));
    }

    @DeleteMapping("/slots/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        equipmentSlotService.delete(id, currentUserId(jwt));
    }

    private Long currentUserId(Jwt jwt) {
        Number userId = jwt.getClaim("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Token sin userId"
            );
        }

        return userId.longValue();
    }
}


