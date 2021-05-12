package uz.pdp.cashmachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cashmachine.entity.User;
import uz.pdp.cashmachine.enums.RoleName;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByRoleName(RoleName worker);
}
