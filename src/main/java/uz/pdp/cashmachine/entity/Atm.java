package uz.pdp.cashmachine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cashmachine.entity.template.AbsEntity;
import uz.pdp.cashmachine.enums.CardType;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Atm extends AbsEntity {
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    //har bir bankomatning seria raqami bo`ladi
    @Column(nullable = false, unique = true)
    private String serialNumber;

    private String bankName;

    private Integer maxAmount;

    private Integer minAmount;

    private String address;

    @OneToOne
    private AtmBox atmBox;//balance

    private boolean status;
}
