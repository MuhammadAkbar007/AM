package uz.pdp.cashmachine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cashmachine.component.Calculation;
import uz.pdp.cashmachine.entity.Atm;
import uz.pdp.cashmachine.entity.Card;
import uz.pdp.cashmachine.enums.CardType;
import uz.pdp.cashmachine.payload.ApiResponse;
import uz.pdp.cashmachine.payload.AtmDto;
import uz.pdp.cashmachine.payload.CardDto;
import uz.pdp.cashmachine.repository.AtmRepository;
import uz.pdp.cashmachine.repository.CardRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AtmService {
    @Autowired
    AtmRepository atmRepository;
    @Autowired
    Calculation calculation;
    @Autowired
    CardRepository cardRepository;

    Map<String, Integer> hm = new HashMap<>();

    public ApiResponse addATM(AtmDto dto) {
        if (atmRepository.existsBySerialNumber(dto.getSerialNumber()))
            return new ApiResponse("ATM by this serial number already exists !", false);

        CardType contains = calculation.contains(dto.getCardType());
        if (contains == null) return new ApiResponse("Wrong card type !", false);

        Atm atm = new Atm();
        atm.setCardType(contains);
        atm.setSerialNumber(dto.getSerialNumber());
        atm.setBankName(dto.getBankName());
        atm.setMaxAmount(dto.getMaxAmount());
        atm.setMinAmount(dto.getMinAmount());
        atm.setAddress(dto.getAddress());
        atmRepository.save(atm);
        return new ApiResponse("bankomat ro`yxatdan o`tkazildi. Endi bankomat hisobini to`ldiring", true);
    }

    public ApiResponse verifyCard(CardDto cardDto) {
        Optional<Card> optionalCard = cardRepository.findByNumber(cardDto.getNumber());
        if (!optionalCard.isPresent() || !optionalCard.get().isStatus())
            return new ApiResponse("Card is unavailable or blocked !", false);
        Card card = optionalCard.get();
        boolean bool = false;
        if (!card.getPassword().equals(cardDto.getPassword())) {
            for (Map.Entry<String, Integer> entry : hm.entrySet()) {
                if (entry.getKey().equals(cardDto.getNumber())) {
                    bool = true;
                    if (entry.getValue() == 3) {
                        card.setStatus(false);
                        cardRepository.save(card);
                        return new ApiResponse("Your card has been blocked !", false);
                    } else entry.setValue(entry.getValue() + 1);
                }
            }
            if (!bool) {
                hm.put(cardDto.getNumber(), 1);
                return new ApiResponse("Wrong pin code !", false);
            } else return new ApiResponse("Wrong pin code !", false);
        }
        return new ApiResponse("Card verified !", true);
    }

    public ApiResponse balance(UUID id) {
        Optional<Atm> optionalATM = atmRepository.findById(id);
        if (!optionalATM.isPresent()) return new ApiResponse("Atm not found !", false);
        Atm atm = optionalATM.get();
        Integer balance = calculation.balance(atm.getAtmBox());
        return new ApiResponse("Atm balance: " + balance, true, balance);
    }
}
