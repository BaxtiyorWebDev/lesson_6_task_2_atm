package uz.pdp.online.lesson_6_task_2_atm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_6_task_2_atm.component.DetectAuthenticationService;
import uz.pdp.online.lesson_6_task_2_atm.entity.Card;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;
import uz.pdp.online.lesson_6_task_2_atm.payload.CardDto;
import uz.pdp.online.lesson_6_task_2_atm.repository.BankRepos;
import uz.pdp.online.lesson_6_task_2_atm.repository.CardRepos;
import uz.pdp.online.lesson_6_task_2_atm.repository.CardTypeRepos;
import uz.pdp.online.lesson_6_task_2_atm.repository.UserRepos;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardService {

    @Autowired
    private CardRepos cardRepos;
    @Autowired
    private CardTypeRepos cardTypeRepos;
    @Autowired
    private BankRepos bankRepos;
    @Autowired
    private UserRepos userRepos;


    public ApiResponse addCard(CardDto cardDto) {
        boolean detectAuthentication = DetectAuthenticationService.detectAuthForDirectorOrEmployee();
        if (detectAuthentication) {
            Optional<Card> byNumberAndUserId = cardRepos.findByNumberAndUserId(cardDto.getNumber(), cardDto.getUserId());
            if (byNumberAndUserId.isPresent())
                return new ApiResponse("Bunday karta MO da mavjud", false);
            Card card = new Card();
            card.setBank(bankRepos.getById(cardDto.getBankId()));
            card.setHolderName(cardDto.getHolderName());
            card.setPinCode(cardDto.getPinCode());
            card.setCardType(cardTypeRepos.getById(cardDto.getCardTypeId()));
            card.setUser(userRepos.getById(cardDto.getUserId()));
            card.setExpireDate(new Date(new Date().getYear() + 5, new Date().getMonth(), new Date().getDate()));
            while (true) {
                card.setNumber((long) (Math.random() * 10000000000000000L));
                card.setCvv((int) (Math.random() * 1000));
                boolean existsByNumberAndCvv = cardRepos.existsByNumberAndCvv(card.getNumber(), card.getCvv());
                if (!existsByNumberAndCvv && card.getNumber() != 16 || card.getCvv() != 3)
                    break;
            }


            cardRepos.save(card);
            return new ApiResponse("Karta saqlandi. Login: " + card.getNumber() + ". Pin code: " + card.getPinCode(), true);
        }
        return new ApiResponse("Sizda kartani qo'shishga ruxsat yo'q", false);
    }


    public Card getCardByUserId(UUID userId) {
        boolean detectAuthentication = DetectAuthenticationService.detectAuthForDirectorOrEmployee();
        if (detectAuthentication) {
            Optional<Card> optionalCard = cardRepos.findAllByUserId(userId);
            return optionalCard.orElse(null);
        }
        return null;
    }


    public Page<Card> getCardPage(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<Card> cardPage = cardRepos.findAll(pageable);
        return cardPage;
    }

    public ApiResponse deleteCardByCardNumber(Long cardNumber) {
        boolean detectAuthentication = DetectAuthenticationService.detectAuthForDirectorOrEmployee();
        if (detectAuthentication) {
            try {
                Optional<Card> optionalCard = cardRepos.findByNumber(cardNumber);
                cardRepos.delete(optionalCard.get());
                return new ApiResponse("Karta o'chirildi", true);
            } catch (Exception e) {
                return new ApiResponse("Karta topilmadi", false);
            }
        } else {
            return new ApiResponse("Sizda kartalarni boshqarishga ruxsat yo'q", false);
        }
    }

}
