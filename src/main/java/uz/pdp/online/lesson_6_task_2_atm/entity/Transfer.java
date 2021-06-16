package uz.pdp.online.lesson_6_task_2_atm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.TransferType;
import uz.pdp.online.lesson_6_task_2_atm.entity.template.AbsEntity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Transfer extends AbsEntity {

    private Long number;/// card number

    @ManyToOne(cascade = CascadeType.ALL)
    private AtmMoneyCase atmMoneyCase;

    private UUID atmId;

    private double commissionAmount;

    private SimpleDateFormat date;

    @Enumerated(EnumType.STRING)
    private TransferType transferType;
}
