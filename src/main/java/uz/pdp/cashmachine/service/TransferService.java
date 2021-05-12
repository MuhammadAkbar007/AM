package uz.pdp.cashmachine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cashmachine.component.Calculation;
import uz.pdp.cashmachine.component.EmailSender;
import uz.pdp.cashmachine.entity.*;
import uz.pdp.cashmachine.enums.CardType;
import uz.pdp.cashmachine.enums.RoleName;
import uz.pdp.cashmachine.enums.TransferType;
import uz.pdp.cashmachine.payload.ApiResponse;
import uz.pdp.cashmachine.payload.TransferDto;
import uz.pdp.cashmachine.repository.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {
    @Autowired
    AtmRepository atmRepository;
    @Autowired
    AtmBoxRepository boxRepository;
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    Calculation calculation;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailSender emailSender;

    public ApiResponse update(String atmSerialNumber, AtmBox dto) {
        Optional<Atm> optionalAtm = atmRepository.findBySerialNumber(atmSerialNumber);
        if (!optionalAtm.isPresent()) return new ApiResponse("ATM not found !", false);
        Atm atm = optionalAtm.get();
        AtmBox atmBox = new AtmBox();
        if (atm.getCardType().name().equalsIgnoreCase("visa")) {

            atmBox.setUsd_1(dto.getUsd_1());
            atmBox.setUsd_5(dto.getUsd_5());
            atmBox.setUsd_10(dto.getUsd_10());
            atmBox.setUsd_50(dto.getUsd_50());
            atmBox.setUsd_100(dto.getUsd_100());
            boxRepository.save(atmBox);
            atm.setAtmBox(atmBox);
            atm.setStatus(true);
            atmRepository.save(atm);
            Transfer transfer = new Transfer();
            transfer.setAtmSerialNumber(atmSerialNumber);
            transfer.setAtmBox(dto);
            transfer.setDate(new SimpleDateFormat());
            transfer.setTransferType(TransferType.INCOME);
            transferRepository.save(transfer);
            return new ApiResponse("bankomat hisobi to`ldirildi", true);

        } else {
            atmBox.setUzs_1_000(dto.getUzs_1_000());
            atmBox.setUzs_5_000(dto.getUzs_5_000());
            atmBox.setUzs_10_000(dto.getUzs_10_000());
            atmBox.setUzs_50_000(dto.getUzs_50_000());
            atmBox.setUzs_100_000(dto.getUzs_100_000());
            boxRepository.save(atmBox);
            atm.setAtmBox(atmBox);
            atm.setStatus(true);
            atmRepository.save(atm);
            Transfer transfer = new Transfer();
            transfer.setAtmSerialNumber(atmSerialNumber);
            transfer.setAtmBox(dto);
            transfer.setDate(new SimpleDateFormat());
            transfer.setTransferType(TransferType.INCOME);
            transferRepository.save(transfer);
            return new ApiResponse("bankomat hisobi to`ldirildi", true);
        }
    }

    public ApiResponse getAll() {
        return new ApiResponse("List:", true, transferRepository.findAll());
    }

    public ApiResponse getIncome(String serilNumber) {
        Optional<Atm> optionalATM = atmRepository.findBySerialNumber(serilNumber);
        if (!optionalATM.isPresent()) return new ApiResponse("bankomat topilmadi", false);

        List<Transfer> all = transferRepository.findAllByAtmSerialNumberAndTransferTypeAndDate(serilNumber,
                TransferType.INCOME, new SimpleDateFormat());

        return new ApiResponse("List: ", true, all);
    }

    public ApiResponse getOutcome(String serilNumber) {
        Optional<Atm> optionalATM = atmRepository.findBySerialNumber(serilNumber);
        if (!optionalATM.isPresent()) return new ApiResponse("bankomat topilmadi", false);

        List<Transfer> all = transferRepository.findAllByAtmSerialNumberAndTransferTypeAndDate(serilNumber,
                TransferType.OUTCOME, new SimpleDateFormat());

        return new ApiResponse("List: ", true, all);
    }

    public ApiResponse getByWorker(String serilNumber) {
        Optional<Atm> optionalATM = atmRepository.findBySerialNumber(serilNumber);
        if (!optionalATM.isPresent()) return new ApiResponse("bankomat topilmadi", false);

        List<Transfer> all = transferRepository.findAllByAtmSerialNumberAndRoleName(serilNumber,
                RoleName.WORKER);

        return new ApiResponse("List: ", true, all);
    }

    public ApiResponse getMoney(TransferDto dto) {
        Double commission = 0.01; //1% komissiya

        Optional<Atm> optionalATM = atmRepository.findBySerialNumber(dto.getAtmSerialNumber());
        if (!optionalATM.isPresent() || !optionalATM.get().isStatus()) return new ApiResponse("bankomat ishlamaydi",
                false);

        Atm atm = optionalATM.get();

        Optional<Card> optionalCard = cardRepository.findByNumber(dto.getCode());
        if (!optionalCard.isPresent()) return new ApiResponse("Card not found !", false);
        Card card = optionalCard.get();
        if (!card.getCardType().equals(atm.getCardType()))
            return new ApiResponse("Your card type is not suitable for this ATM !", false);

        AtmBox atmBox = atm.getAtmBox();
        Integer balance = calculation.balance(atmBox);
        if (balance < (dto.getAmount() * 0.01) + dto.getAmount() ||
                dto.getAmount() > atm.getMaxAmount() ||
                dto.getAmount() < atm.getMinAmount())
            return new ApiResponse("Unavailable amount of money !", false);

        try {


            if (card.getCardType().equals(CardType.VISA)) {
                AtmBox atmBox1 = calculation.balanceToAtm(dto.getAmount(), "usd");
                if (atmBox1 == null) return new ApiResponse("Wrong balance !", false);
                if (atmBox.getUsd_100() >= atmBox1.getUsd_100() &&
                        atmBox.getUsd_50() >= atmBox1.getUsd_50() &&
                        atmBox.getUsd_10() >= atmBox1.getUsd_10() &&
                        atmBox.getUsd_5() >= atmBox1.getUsd_5() &&
                        atmBox.getUsd_1() >= atmBox1.getUsd_1()) {
                    atmBox.setUsd_100(atmBox.getUsd_100() - atmBox1.getUsd_100());
                    atmBox.setUsd_50(atmBox.getUsd_50() - atmBox1.getUsd_50());
                    atmBox.setUsd_10(atmBox.getUsd_10() - atmBox1.getUsd_10());
                    atmBox.setUsd_5(atmBox.getUsd_5() - atmBox1.getUsd_5());
                    atmBox.setUsd_1(atmBox.getUsd_1() - atmBox1.getUsd_1());
                    boxRepository.save(atmBox);
                    atm.setAtmBox(atmBox);
                    atmRepository.save(atm);
                    card.setBalance(card.getBalance() - (balance + balance * commission));
                    cardRepository.save(card);
                    Transfer transfer = new Transfer();
                    transfer.setAtmSerialNumber(atm.getSerialNumber());
                    transfer.setAtmBox(atmBox1);
                    transfer.setDate(new SimpleDateFormat());
                    transfer.setTransferType(TransferType.OUTCOME);
                    transferRepository.save(transfer);
                    return new ApiResponse("Successful !", true);
                } else return new ApiResponse("Suitable bill not found !", false);
            } else {
                AtmBox atmBox1 = calculation.balanceToAtm(dto.getAmount(), "uzs");
                if (atmBox1 == null) return new ApiResponse("Wrong balance !", false);
                if (atmBox.getUzs_100_000() >= atmBox1.getUzs_100_000() &&
                        atmBox.getUzs_50_000() >= atmBox1.getUzs_50_000() &&
                        atmBox.getUzs_10_000() >= atmBox1.getUzs_10_000() &&
                        atmBox.getUzs_5_000() >= atmBox1.getUzs_5_000() &&
                        atmBox.getUzs_1_000() >= atmBox1.getUzs_1_000()) {
                    atmBox.setUzs_100_000(atmBox.getUzs_100_000() - atmBox1.getUzs_100_000());
                    atmBox.setUzs_50_000(atmBox.getUzs_50_000() - atmBox1.getUzs_50_000());
                    atmBox.setUzs_10_000(atmBox.getUzs_10_000() - atmBox1.getUzs_10_000());
                    atmBox.setUzs_5_000(atmBox.getUzs_5_000() - atmBox1.getUzs_5_000());
                    atmBox.setUzs_1_000(atmBox.getUzs_1_000() - atmBox1.getUzs_1_000());
                    boxRepository.save(atmBox);
                    atm.setAtmBox(atmBox);
                    atmRepository.save(atm);
                    card.setBalance(card.getBalance() - (balance + balance * commission));
                    cardRepository.save(card);
                    Transfer transfer = new Transfer();
                    transfer.setAtmSerialNumber(atm.getSerialNumber());
                    transfer.setAtmBox(atmBox1);
                    transfer.setDate(new SimpleDateFormat());
                    transfer.setTransferType(TransferType.OUTCOME);
                    transferRepository.save(transfer);

                    Integer newBalance = calculation.balance(atmBox);
                    if (newBalance <= 10_000_000) {
                        Optional<User> byRoleName = userRepository.findByRoleName(RoleName.WORKER);
                        User xodim = byRoleName.get();
                        boolean b1 = emailSender.sendEmail(xodim.getEmail(), "Less than 10_000_000 in Atm !");
                        if (b1) return new ApiResponse("Email has sent !", true);

                        return new ApiResponse("Error !", false);
                    }

                    return new ApiResponse("Successful !", true);
                } else return new ApiResponse("Suitable bill not found !", false);
            }
        } catch (Exception e) {
            return new ApiResponse("Error !", false);
        }
    }
}
