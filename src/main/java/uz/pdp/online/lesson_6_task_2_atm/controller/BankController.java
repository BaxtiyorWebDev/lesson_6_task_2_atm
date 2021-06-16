package uz.pdp.online.lesson_6_task_2_atm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.lesson_6_task_2_atm.entity.Bank;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;
import uz.pdp.online.lesson_6_task_2_atm.payload.BankDto;
import uz.pdp.online.lesson_6_task_2_atm.repository.BankRepos;
import uz.pdp.online.lesson_6_task_2_atm.service.BankService;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Autowired
    BankService bankService;


    @PostMapping
    public HttpEntity<?> addBank(@RequestBody BankDto bankDto) {
        ApiResponse apiResponse = bankService.addBank(bankDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getBankById(@PathVariable Integer id) {
        Bank bankById = bankService.getBankById(id);
        return ResponseEntity.status(bankById!=null?200:409).body(bankById);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editBank(@PathVariable Integer id, @RequestBody BankDto bankDto) {
        ApiResponse apiResponse = bankService.editBank(id, bankDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteBankById(@PathVariable Integer id) {
        ApiResponse apiResponse = bankService.deleteBankById(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


}
