package uz.pdp.cashmachine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.cashmachine.entity.AtmBox;
import uz.pdp.cashmachine.enums.TransferType;

import javax.persistence.*;
import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cardNumber;

    private String atmSerialNumber;

    @ManyToOne
    private AtmBox atmBox;

    private double commissionAmount;

    private SimpleDateFormat date;

    //ushbu field bankomatga nisbatan olinadi yani karta orqali bankomatga pul qo`shildi yoki yechiladi
    private TransferType transferType;
}
