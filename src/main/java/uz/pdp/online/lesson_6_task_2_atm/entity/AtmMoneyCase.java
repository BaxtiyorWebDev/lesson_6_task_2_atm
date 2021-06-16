package uz.pdp.online.lesson_6_task_2_atm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.online.lesson_6_task_2_atm.entity.template.AbsEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AtmMoneyCase extends AbsEntity {
    private Integer uzs1000;
    private Integer uzs5000;
    private Integer uzs10000;
    private Integer uzs50000;
    private Integer uzs100000;

    private Integer usd1;
    private Integer usd5;
    private Integer usd10;
    private Integer usd50;
    private Integer usd100;
}
