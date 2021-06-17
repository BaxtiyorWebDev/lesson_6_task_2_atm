package uz.pdp.online.lesson_6_task_2_atm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.lesson_6_task_2_atm.entity.Atm;
import uz.pdp.online.lesson_6_task_2_atm.entity.AtmMoneyCase;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;
import uz.pdp.online.lesson_6_task_2_atm.payload.AtmDto;
import uz.pdp.online.lesson_6_task_2_atm.payload.CardDto;
import uz.pdp.online.lesson_6_task_2_atm.service.AtmService;
import uz.pdp.online.lesson_6_task_2_atm.service.TransferService;

import java.util.UUID;

@RestController
@RequestMapping("/api/atm")
public class AtmController {

    @Autowired
    private AtmService atmService;
    @Autowired
    private TransferService transferService;

    @PostMapping
    public HttpEntity<?> addAtm(@RequestBody AtmDto atmDto) {
        ApiResponse apiResponse = atmService.addAtm(atmDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @GetMapping
    public Page<Atm> getPagesAtm(@RequestParam int page) {
        Page<Atm> pagesAtm = atmService.getPagesAtm(page);
        return pagesAtm;
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getAtmById(@PathVariable UUID id) {
        Atm atm = atmService.getById(id);
        return ResponseEntity.status(atm!=null?200:409).body(atm);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editAtm(@PathVariable UUID id, @RequestBody AtmDto atmDto) {
        ApiResponse apiResponse = atmService.editAtm(id, atmDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteAtm(@PathVariable UUID id) {
        ApiResponse apiResponse = atmService.deleteAtmById(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


    // xodim bankomatga pul qo'yishi
    @PutMapping("/inputMoneyForEmployee/atmId/{id}")
    public HttpEntity<?> inputMoneyForEmployee(@PathVariable UUID id, @RequestBody AtmMoneyCase atmMoneyCase) {
        ApiResponse apiResponse = transferService.update(id, atmMoneyCase);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }
}
