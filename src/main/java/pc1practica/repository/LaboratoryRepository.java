package pc1practica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc1practica.entity.Laboratory;

import java.util.List;

public interface LaboratoryRepository extends JpaRepository<Laboratory, Long> {

    List<Laboratory> findByManagerId(Long managerId);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
