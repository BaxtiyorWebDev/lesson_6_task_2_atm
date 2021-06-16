package uz.pdp.online.lesson_6_task_2_atm.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CardDto {

    @NotNull
    private Integer bankId;

    @NotNull
    private String holderName;

    @NotNull
    private short pinCode;

    @NotNull
    private Integer cardTypeId;

    /**
     * for put method
     */
    private boolean active;

    /**
     * for put method
     */
    private Long number;


    /* FOR USER */

    private UUID userId;

    private double balance;




}
