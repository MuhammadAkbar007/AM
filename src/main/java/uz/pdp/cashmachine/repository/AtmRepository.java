package uz.pdp.cashmachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cashmachine.entity.Atm;

import java.util.Optional;
import java.util.UUID;

public interface AtmRepository extends JpaRepository<Atm, UUID> {
    boolean existsBySerialNumber(String serialNumber);
    Optional<Atm> findBySerialNumber(String serialNumber);
}
