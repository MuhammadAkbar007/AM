package uz.pdp.cashmachine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cashmachine.entity.template.AbsEntity;
import uz.pdp.cashmachine.enums.CardType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Card extends AbsEntity {
    @Column(nullable = false, unique = true)
    @Size(min = 16, max = 16)
    private String number;

    @Column(nullable = false)
    @Size(min = 4, max = 4)
    private String password;

    private String bankName;

    private String CVVcode;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)//amal qilish muddati
    private Timestamp validityPeriod;

    private double balance = 0;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private boolean status = true;
}
