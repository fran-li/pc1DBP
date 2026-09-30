package pc1practica.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pc1practica.dto.LaboratoryRequestDto;
import pc1practica.dto.LaboratoryResponseDto;
import pc1practica.service.LaboratoryService;

import java.util.List;

@RestController
@RequestMapping("/laboratories")
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    public LaboratoryController(LaboratoryService laboratoryService) {
        this.laboratoryService = laboratoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LaboratoryResponseDto create(
            @Valid @RequestBody LaboratoryRequestDto request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return laboratoryService.create(request, currentUserId(jwt));
    }

    @GetMapping
    public List<LaboratoryResponseDto> findAll() {
        return laboratoryService.findAll();
    }

    @GetMapping("/{id}")
    public LaboratoryResponseDto findById(@PathVariable Long id) {
        return laboratoryService.findById(id);
    }

    @GetMapping("/mine")
    public List<LaboratoryResponseDto> findMine(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return laboratoryService.findMine(currentUserId(jwt));
    }

    @PutMapping("/{id}")
    public LaboratoryResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody LaboratoryRequestDto request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return laboratoryService.update(id, request, currentUserId(jwt));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        laboratoryService.delete(id, currentUserId(jwt));
    }

    private Long currentUserId(Jwt jwt) {
        Number userId = jwt.getClaim("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "token sin userId"
            );
        }

        return userId.longValue();
    }
}



