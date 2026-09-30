package pc1practica.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pc1practica.dto.EquipmentSlotRequestDto;
import pc1practica.dto.EquipmentSlotResponseDto;
import pc1practica.entity.EquipmentSlot;
import pc1practica.entity.Laboratory;
import pc1practica.repository.EquipmentSlotRepository;
import pc1practica.repository.LaboratoryRepository;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class EquipmentSlotService {

    private final EquipmentSlotRepository slots;
    private final LaboratoryRepository laboratories;

    public EquipmentSlotService(
            EquipmentSlotRepository slots,
            LaboratoryRepository laboratories
    ) {
        this.slots = slots;
        this.laboratories = laboratories;
    }

    public EquipmentSlotResponseDto create(
            Long laboratoryId,
            EquipmentSlotRequestDto request,
            Long managerId
    ) {
        findOwnedLaboratory(laboratoryId, managerId);
        validateTimes(request.startTime(), request.endTime());

        EquipmentSlot slot = new EquipmentSlot();
        slot.setLaboratoryId(laboratoryId);
        slot.setEquipmentCode(request.equipmentCode().trim());
        slot.setCapacity(request.capacity());
        slot.setStartTime(request.startTime());
        slot.setEndTime(request.endTime());
        slot.setStatus("AVAILABLE");

        return toResponse(slots.save(slot));
    }

    public List<EquipmentSlotResponseDto> findByLaboratory(Long laboratoryId) {
        findLaboratory(laboratoryId);

        return slots.findByLaboratoryId(laboratoryId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public EquipmentSlotResponseDto findById(Long id) {
        return toResponse(findEntity(id));
    }

    public EquipmentSlotResponseDto update(
            Long id,
            EquipmentSlotRequestDto request,
            Long managerId
    ) {
        EquipmentSlot slot = findEntity(id);
        findOwnedLaboratory(slot.getLaboratoryId(), managerId);
        validateTimes(request.startTime(), request.endTime());

        slot.setEquipmentCode(request.equipmentCode().trim());
        slot.setCapacity(request.capacity());
        slot.setStartTime(request.startTime());
        slot.setEndTime(request.endTime());

        return toResponse(slots.save(slot));
    }

    public void delete(Long id, Long managerId) {
        EquipmentSlot slot = findEntity(id);
        findOwnedLaboratory(slot.getLaboratoryId(), managerId);
        slots.delete(slot);
    }

    private EquipmentSlot findEntity(Long id) {
        return slots.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "no equipment"
                        )
                );
    }

    private Laboratory findLaboratory(Long laboratoryId) {
        return laboratories.findById(laboratoryId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "no lab"
                        )
                );
    }

    private Laboratory findOwnedLaboratory(Long laboratoryId, Long managerId) {
        Laboratory laboratory = findLaboratory(laboratoryId);

        if (!laboratory.getManagerId().equals(managerId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "no eres admin de este alb"
            );
        }

        return laboratory;
    }

    private void validateTimes(ZonedDateTime startTime, ZonedDateTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "startTime debe estar antes de endtie"
            );
        }
    }

    private EquipmentSlotResponseDto toResponse(EquipmentSlot slot) {
        return new EquipmentSlotResponseDto(
                slot.getId(),
                slot.getLaboratoryId(),
                slot.getEquipmentCode(),
                slot.getCapacity(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getStatus()
        );
    }
}

