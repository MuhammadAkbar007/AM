package uz.pdp.cashmachine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cashmachine.entity.template.AbsEntity;

import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class AtmBox extends AbsEntity {
    private Integer uzs_1_000 = 0;
    private Integer uzs_5_000 = 0;
    private Integer uzs_10_000 = 0;
    private Integer uzs_50_000 = 0;
    private Integer uzs_100_000 = 0;

    private Integer usd_1 = 0;
    private Integer usd_5 = 0;
    private Integer usd_10 = 0;
    private Integer usd_50 = 0;
    private Integer usd_100 = 0;
}
