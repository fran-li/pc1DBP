package pc1practica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc1practica.entity.EquipmentSlot;

import java.util.List;

public interface EquipmentSlotRepository extends JpaRepository<EquipmentSlot, Long> {
    List<EquipmentSlot> findByLaboratoryId(Long laboratoryId);
}
