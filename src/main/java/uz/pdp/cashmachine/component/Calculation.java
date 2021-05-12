package uz.pdp.cashmachine.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.pdp.cashmachine.entity.AtmBox;
import uz.pdp.cashmachine.enums.CardType;
import uz.pdp.cashmachine.repository.AtmRepository;

@Component
public class Calculation {
    @Autowired
    AtmRepository atmRepository;

    public CardType contains(String test) {
        for (CardType c : CardType.values()) {
            if (c.name().equalsIgnoreCase(test)) {
                return c;
            }
        }
        return null;
    }

    public Integer balance(AtmBox box) {
        return 1_000 * box.getUzs_1_000() +
                5_000 * box.getUzs_5_000() +
                10_000 * box.getUzs_10_000() +
                50_000 * box.getUzs_50_000() +
                100_000 * box.getUzs_100_000() +

                box.getUsd_1() +
                5 * box.getUsd_5() +
                10 * box.getUsd_10() +
                50 * box.getUsd_50() +
                100 * box.getUsd_100();

    }

    public AtmBox balanceToAtm(Integer balance, String type) {
        AtmBox atmBox = new AtmBox();
        if (type.equals("uzs")) {
            atmBox.setUzs_100_000(balance / 100_000);
            atmBox.setUzs_50_000((balance % 100_000) / 50_000);
            atmBox.setUzs_10_000(balance % 100_000 % 50_000 / 10_000);
            atmBox.setUzs_5_000(balance % 100_000 % 50_000 % 10_000 / 5_000);
            atmBox.setUzs_1_000(balance % 100_000 % 50_000 % 10_000 % 5_000 / 1_000);
            if (balance % 100_000 % 50_000 % 10_000 % 5_000 % 1_000 == 0)
                return atmBox;
            return null;
        } else
            atmBox.setUsd_100(balance / 100);
        atmBox.setUsd_50((balance % 100) / 50);
        atmBox.setUsd_10(balance % 100 % 50 / 10);
        atmBox.setUsd_5(balance % 100 % 50 % 10 / 5);
        atmBox.setUsd_1(balance % 100 % 50 % 10 % 5);
        if (balance % 100 % 50 % 10 % 5 == 0)
            return atmBox;
        return null;
    }
}
