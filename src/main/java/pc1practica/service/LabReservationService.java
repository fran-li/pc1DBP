package pc1practica.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pc1practica.dto.LabReservationRequestDto;
import pc1practica.dto.LabReservationResponseDto;
import pc1practica.entity.EquipmentSlot;
import pc1practica.entity.LabReservation;
import pc1practica.entity.Laboratory;
import pc1practica.repository.EquipmentSlotRepository;
import pc1practica.repository.LabReservationRepository;
import pc1practica.repository.LaboratoryRepository;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class LabReservationService {

    private static final String ACTIVE = "ACTIVE";
    private static final String CANCELLED = "CANCELLED";
    private static final String AVAILABLE = "AVAILABLE";

    private final LabReservationRepository reservations;
    private final EquipmentSlotRepository slots;
    private final LaboratoryRepository laboratories;

    public LabReservationService(
            LabReservationRepository reservations,
            EquipmentSlotRepository slots,
            LaboratoryRepository laboratories
    ) {
        this.reservations = reservations;
        this.slots = slots;
        this.laboratories = laboratories;
    }

    public LabReservationResponseDto create(
            LabReservationRequestDto request,
            Long studentId
    ) {
        EquipmentSlot slot = findSlot(request.slotId());

        if (!AVAILABLE.equalsIgnoreCase(slot.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "slots no disponibles"
            );
        }

        if (reservations.existsBySlotIdAndStudentIdAndStatus(
                slot.getId(),
                studentId,
                ACTIVE
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ya tienes reserva "
            );
        }

        long activeReservations = reservations.countBySlotIdAndStatus(
                slot.getId(),
                ACTIVE
        );

        if (activeReservations >= slot.getCapacity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "slots maximos"
            );
        }

        LabReservation reservation = new LabReservation();
        reservation.setSlotId(slot.getId());
        reservation.setStudentId(studentId);
        reservation.setPurpose(request.purpose().trim());
        reservation.setReservedAt(ZonedDateTime.now());
        reservation.setStatus(ACTIVE);

        return toResponse(reservations.save(reservation));
    }

    public List<LabReservationResponseDto> findMine(Long studentId) {
        return reservations.findByStudentId(studentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LabReservationResponseDto findMineById(
            Long id,
            Long studentId
    ) {
        return toResponse(findOwnedReservation(id, studentId));
    }

    public void cancel(Long id, Long studentId) {
        LabReservation reservation = findOwnedReservation(id, studentId);

        if (CANCELLED.equalsIgnoreCase(reservation.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "reserva cancelada"
            );
        }

        reservation.setStatus(CANCELLED);
        reservations.save(reservation);
    }

    public List<LabReservationResponseDto> findBySlotForManager(
            Long slotId,
            Long managerId
    ) {
        EquipmentSlot slot = findSlot(slotId);
        Laboratory laboratory = laboratories.findById(slot.getLaboratoryId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "no lab"
                        )
                );

        if (!laboratory.getManagerId().equals(managerId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "no eres admin de este lab"
            );
        }

        return reservations.findBySlotId(slotId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private LabReservation findOwnedReservation(Long id, Long studentId) {
        LabReservation reservation = reservations.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "sin reserva"
                        )
                );

        if (!reservation.getStudentId().equals(studentId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "no dueño de esta resserv"
            );
        }

        return reservation;
    }

    private EquipmentSlot findSlot(Long slotId) {
        return slots.findById(slotId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "no encuentra slots"
                        )
                );
    }

    private LabReservationResponseDto toResponse(LabReservation reservation) {
        return new LabReservationResponseDto(
                reservation.getId(),
                reservation.getSlotId(),
                reservation.getStudentId(),
                reservation.getPurpose(),
                reservation.getReservedAt(),
                reservation.getStatus()
        );
    }
}

