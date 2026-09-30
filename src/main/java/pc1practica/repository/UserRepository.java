package pc1practica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pc1practica.entity.User;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
