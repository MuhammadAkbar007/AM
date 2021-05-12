package uz.pdp.cashmachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cashmachine.entity.AtmBox;

import java.util.UUID;

public interface AtmBoxRepository extends JpaRepository<AtmBox, UUID> {
}
