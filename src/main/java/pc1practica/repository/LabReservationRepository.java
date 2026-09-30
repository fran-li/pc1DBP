package pc1practica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc1practica.entity.LabReservation;

import java.util.List;

public interface LabReservationRepository extends JpaRepository<LabReservation, Long> {

    List<LabReservation> findByStudentId(Long studentId);

    List<LabReservation> findBySlotId(Long slotId);

    boolean existsBySlotIdAndStudentIdAndStatus(
            Long slotId,
            Long studentId,
            String status
    );

    long countBySlotIdAndStatus(Long slotId, String status);
}