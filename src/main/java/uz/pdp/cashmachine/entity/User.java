package uz.pdp.cashmachine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cashmachine.entity.template.AbsEntity;
import uz.pdp.cashmachine.enums.RoleName;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@EqualsAndHashCode(callSuper = true)
public class User extends AbsEntity {
    private String fullName;

    @Column(unique = true, nullable = false)
    @Email
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
