package uz.pdp.online.lesson_6_task_2_atm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.lesson_6_task_2_atm.entity.Card;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;
import uz.pdp.online.lesson_6_task_2_atm.payload.CardDto;
import uz.pdp.online.lesson_6_task_2_atm.service.CardService;

import java.util.UUID;

@RestController
@RequestMapping("/api/card")
public class CardController {

    @Autowired
    CardService cardService;


    @PostMapping
    public HttpEntity<?> addCard(@RequestBody CardDto cardDto) {
        ApiResponse apiResponse = cardService.addCard(cardDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @GetMapping("/{userId}")
    public Card getCardById(@PathVariable UUID userId) {
        Card cardById = cardService.getCardByUserId(userId);
        return cardById;
    }


    @GetMapping
    public Page<Card> getCardPage(@RequestParam int page) {
        Page<Card> cardPage = cardService.getCardPage(page);
        return cardPage;
    }

    @PutMapping
    public HttpEntity<?> editCard(@RequestBody CardDto cardDto) {
        ApiResponse apiResponse = cardService.editCard(cardDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @DeleteMapping("/{cardNumber}")
    public HttpEntity<?> deleteCardByCardNumber(@PathVariable Long cardNumber) {
        ApiResponse apiResponse = cardService.deleteCardByCardNumber(cardNumber);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


}
