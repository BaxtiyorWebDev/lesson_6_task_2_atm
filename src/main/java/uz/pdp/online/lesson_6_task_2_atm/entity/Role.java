package uz.pdp.online.lesson_6_task_2_atm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.RoleEnum;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;

    @Override
    public String getAuthority() {
        return roleEnum.name();
    }
}
