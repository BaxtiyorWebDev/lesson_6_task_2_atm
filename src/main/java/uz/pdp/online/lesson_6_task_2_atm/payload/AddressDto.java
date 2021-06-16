package uz.pdp.online.lesson_6_task_2_atm.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AddressDto {

    @NotNull
    @Size(min = 3,max = 20)
    private String country;

    @NotNull
    @Size(min = 3,max = 20)
    private String region;

    @NotNull
    @Size(min = 3,max = 20)
    private String district;

    @NotNull
    @Size(min = 3,max = 20)
    private String street;


}
