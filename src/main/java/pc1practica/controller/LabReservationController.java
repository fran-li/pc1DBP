package pc1practica.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pc1practica.dto.LabReservationRequestDto;
import pc1practica.dto.LabReservationResponseDto;
import pc1practica.service.LabReservationService;

import java.util.List;

@RestController
public class LabReservationController {

    private final LabReservationService labReservationService;

    public LabReservationController(LabReservationService labReservationService) {
        this.labReservationService = labReservationService;
    }

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public LabReservationResponseDto create(
            @Valid @RequestBody LabReservationRequestDto request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return labReservationService.create(request, currentUserId(jwt));
    }

    @GetMapping("/reservations/myreservations")
    public List<LabReservationResponseDto> findMine(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return labReservationService.findMine(currentUserId(jwt));
    }

    @GetMapping("/reservations/{id}")
    public LabReservationResponseDto findMineById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return labReservationService.findMineById(id, currentUserId(jwt));
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        labReservationService.cancel(id, currentUserId(jwt));
    }

    @GetMapping("/slots/{slotId}/reservations")
    public List<LabReservationResponseDto> findBySlotForManager(
            @PathVariable Long slotId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return labReservationService.findBySlotForManager(
                slotId,
                currentUserId(jwt)
        );
    }

    private Long currentUserId(Jwt jwt) {
        Number userId = jwt.getClaim("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Token sin userid"
            );
        }

        return userId.longValue();
    }
}

