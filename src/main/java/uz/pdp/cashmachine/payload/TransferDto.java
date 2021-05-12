package uz.pdp.cashmachine.payload;

import lombok.Data;

@Data
public class TransferDto {
    private String atmSerialNumber;
    private Integer amount;

    private String code;
    private String pinCode;
}
