package uz.pdp.cashmachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cashmachine.entity.Transfer;
import uz.pdp.cashmachine.enums.RoleName;
import uz.pdp.cashmachine.enums.TransferType;

import java.text.SimpleDateFormat;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Integer> {
    List<Transfer> findAllByAtmSerialNumberAndTransferTypeAndDate(String serilNumber, TransferType income,
                                                                  SimpleDateFormat simpleDateFormat);

    List<Transfer> findAllByAtmSerialNumberAndRoleName(String serilNumber, RoleName worker);
}
