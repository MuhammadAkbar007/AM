package uz.pdp.cashmachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cashmachine.entity.Card;

import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    Optional<Card> findByNumber(@Size(min = 16, max = 16) String number);
}
