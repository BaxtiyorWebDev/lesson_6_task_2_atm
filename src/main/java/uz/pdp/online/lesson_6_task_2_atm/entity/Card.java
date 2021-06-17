package uz.pdp.online.lesson_6_task_2_atm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.online.lesson_6_task_2_atm.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Card extends AbsEntity {


    @Column(nullable = false, unique = true)
    private Long number; // 16 digit

    @Column(nullable = false)
    private short pinCode; // 4 digit

    @Column(nullable = false)
    private Integer cvv; // 3 digit

    @Column(nullable = false)
    private String holderName;

    @Column(nullable = false)
    private Date expireDate;


    @ManyToOne
    private Bank bank;

    @ManyToOne
    private CardType cardType;

    private boolean active = true;

    @ManyToOne
    private User user;

    private Double balance;


}

