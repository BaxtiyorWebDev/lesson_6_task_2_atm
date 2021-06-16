package uz.pdp.online.lesson_6_task_2_atm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.online.lesson_6_task_2_atm.entity.AtmMoneyCase;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;
import uz.pdp.online.lesson_6_task_2_atm.payload.TransferDto;
import uz.pdp.online.lesson_6_task_2_atm.service.TransferService;

import java.util.UUID;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    @Autowired
    TransferService transferService;

    // bankomatdan pul yechish
    @PutMapping("/outcome")
    public HttpEntity<?> outcome(@RequestBody TransferDto transferDto) {
        ApiResponse apiResponse = transferService.getMoney(transferDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

}
