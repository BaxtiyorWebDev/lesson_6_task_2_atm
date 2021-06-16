package uz.pdp.online.lesson_6_task_2_atm.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AtmDto {

    @NotNull
    String cardType;

    @NotNull
    private double maxWithDrawAmount; // card uchun bankomatdan max pul yechish miqdori

    @NotNull
    private double minWithDrawAmount; // card uchun bankomatdan min pul yechish miqdori


    @NotNull
    private Integer bankId;

    @NotNull
    private AddressDto addressDto;



}
