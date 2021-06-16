package uz.pdp.online.lesson_6_task_2_atm.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDto {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
