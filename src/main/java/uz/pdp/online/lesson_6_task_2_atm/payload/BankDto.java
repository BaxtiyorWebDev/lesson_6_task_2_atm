package uz.pdp.online.lesson_6_task_2_atm.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BankDto {

    @NotNull
    private String name;

    private AddressDto addressDto;
}
