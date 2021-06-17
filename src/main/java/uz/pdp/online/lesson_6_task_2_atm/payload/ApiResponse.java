package uz.pdp.online.lesson_6_task_2_atm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private String message;
    private boolean success;

    private Integer uzs;
    private Integer usd;

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ApiResponse(boolean success, Integer uzs, Integer usd) {
        this.success = success;
        this.uzs = uzs;
        this.usd = usd;
    }
}

