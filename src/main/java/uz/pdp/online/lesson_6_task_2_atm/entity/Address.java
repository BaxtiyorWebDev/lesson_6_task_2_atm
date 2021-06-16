package uz.pdp.online.lesson_6_task_2_atm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String street;

}
