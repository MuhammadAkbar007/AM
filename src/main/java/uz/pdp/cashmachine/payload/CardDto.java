package uz.pdp.cashmachine.payload;

import lombok.Data;

@Data
public class CardDto {
    public String number;//kartaning 16 tali nomeri
    public String password;
}
