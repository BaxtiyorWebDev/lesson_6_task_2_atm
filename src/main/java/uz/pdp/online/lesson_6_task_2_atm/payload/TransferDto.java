package uz.pdp.online.lesson_6_task_2_atm.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class TransferDto {

    @NotNull
    private UUID atmId;

    @NotNull
    private Integer amount;

    @NotNull
    private Long number;

    @NotNull
    private short pinCode;


}
