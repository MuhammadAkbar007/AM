package uz.pdp.cashmachine.payload;

import lombok.Data;

@Data
public class AtmDto {
    private String cardType;
    private Integer maxAmount;
    private Integer minAmount;
    private String serialNumber;
    private String address;
    private String bankName;
}
