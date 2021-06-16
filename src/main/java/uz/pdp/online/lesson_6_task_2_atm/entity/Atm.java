package uz.pdp.online.lesson_6_task_2_atm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.CardTypeEnum;
import uz.pdp.online.lesson_6_task_2_atm.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Atm extends AbsEntity {

    @Enumerated(EnumType.STRING)
    private CardTypeEnum cardTypeEnum;

    @Column(nullable = false)
    private double maxWithdrawAmount; // card uchun bankomatdan max pul yechish miqdori

    @Column(nullable = false)
    private double minWithdrawAmount; // card uchun bankomatdan min pul yechish miqdori

    @ManyToOne
    private Bank bank;

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    private AtmMoneyCase atmMoneyCase;

    private boolean status;


}
