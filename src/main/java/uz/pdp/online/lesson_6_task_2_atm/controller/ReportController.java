package uz.pdp.online.lesson_6_task_2_atm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.lesson_6_task_2_atm.entity.Transfer;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;
import uz.pdp.online.lesson_6_task_2_atm.service.AtmService;
import uz.pdp.online.lesson_6_task_2_atm.service.TransferService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private AtmService atmService;
    @Autowired
    private TransferService transferService;

    //bankomat hisobini ko'rish
    @GetMapping("/{id}")
    public HttpEntity<?> getBalance(@PathVariable UUID id) { // atmId orqali
        ApiResponse apiResponse = atmService.balance(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<List<Transfer>> getAll() {
        List<Transfer> allTransferList = transferService.getAll();
        return ResponseEntity.status(allTransferList!=null?200:409).body(allTransferList);
    }

    @GetMapping("/income")
    public HttpEntity<List<Transfer>> getIncome(@RequestBody UUID atmId) {
        List<Transfer> income = transferService.getIncome(atmId);
        return ResponseEntity.status(income!=null?200:409).body(income);
    }

    @GetMapping("/outcome")
    public HttpEntity<List<Transfer>> getOutcome(@RequestBody UUID atmId) {
        List<Transfer> outcome = transferService.getOutcome(atmId);
        return ResponseEntity.status(outcome!=null?200:409).body(outcome);
    }

    @GetMapping("/byEmployee")
    public HttpEntity<List<Transfer>> getTransferByWorker(@RequestBody UUID atmId, @RequestBody UUID employeeId) {
        List<Transfer> byEmployee = transferService.getByEmployee(atmId, employeeId);
        return ResponseEntity.status(byEmployee!=null?200:409).body(byEmployee);
    }

}
