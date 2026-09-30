package pc1practica.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pc1practica.dto.LaboratoryRequestDto;
import pc1practica.dto.LaboratoryResponseDto;
import pc1practica.entity.Laboratory;
import pc1practica.repository.LaboratoryRepository;

import java.util.List;

@Service
public class LaboratoryService {

    private final LaboratoryRepository laboratories;

    public LaboratoryService(LaboratoryRepository laboratories) {
        this.laboratories = laboratories;
    }

    public LaboratoryResponseDto create(
            LaboratoryRequestDto request,
            Long managerId
    ) {
        String name = request.name().trim();

        if (laboratories.existsByName(name)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "nombre de lab ya existe"
            );
        }

        Laboratory laboratory = new Laboratory();
        laboratory.setName(name);
        laboratory.setLocation(request.location().trim());
        laboratory.setManagerId(managerId);
        laboratory.setStatus(request.status().trim());

        return toResponse(laboratories.save(laboratory));
    }

    public List<LaboratoryResponseDto> findAll() {
        return laboratories.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LaboratoryResponseDto findById(Long id) {
        return toResponse(findEntity(id));
    }

    public List<LaboratoryResponseDto> findMine(Long managerId) {
        return laboratories.findByManagerId(managerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LaboratoryResponseDto update(
            Long id,
            LaboratoryRequestDto request,
            Long managerId
    ) {
        Laboratory laboratory = findOwnedLaboratory(id, managerId);
        String name = request.name().trim();

        if (laboratories.existsByNameAndIdNot(name, id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "nombre de lab ya existe"
            );
        }

        laboratory.setName(name);
        laboratory.setLocation(request.location().trim());
        laboratory.setStatus(request.status().trim());

        return toResponse(laboratories.save(laboratory));
    }

    public void delete(Long id, Long managerId) {
        Laboratory laboratory = findOwnedLaboratory(id, managerId);
        laboratories.delete(laboratory);
    }

    private Laboratory findEntity(Long id) {
        return laboratories.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Lab no se encontro"
                        )
                );
    }

    private Laboratory findOwnedLaboratory(Long id, Long managerId) {
        Laboratory laboratory = findEntity(id);

        if (!laboratory.getManagerId().equals(managerId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "no administras este lab"
            );
        }

        return laboratory;
    }

    private LaboratoryResponseDto toResponse(Laboratory laboratory) {
        return new LaboratoryResponseDto(
                laboratory.getId(),
                laboratory.getName(),
                laboratory.getLocation(),
                laboratory.getManagerId(),
                laboratory.getStatus()
        );
    }
}
