package uz.pdp.online.lesson_6_task_2_atm.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.pdp.online.lesson_6_task_2_atm.entity.Card;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;
import uz.pdp.online.lesson_6_task_2_atm.payload.TransferDto;
import uz.pdp.online.lesson_6_task_2_atm.repository.CardRepos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class VerifyCard {

    @Autowired
    CardRepos cardRepos;

    public ApiResponse verifyCard(TransferDto transferDto) {
        Map<String, Integer> hm = new HashMap<>();
        Optional<Card> optionalCard = cardRepos.findByNumber(transferDto.getNumber());
        if (!optionalCard.isPresent() || !optionalCard.get().isActive())
            return new ApiResponse("Karta mavjud emas yoki nofaol holatda", false);
        Card card = optionalCard.get();
        boolean bool = false;
        if (card.getPinCode() != transferDto.getPinCode()) {
            for (Map.Entry<String, Integer> entry : hm.entrySet()) {
                if (entry.getKey().equals(transferDto.getNumber().toString())) {
                    bool = true;
                    if (entry.getValue() == 3) {
                        card.setActive(false);
                        cardRepos.save(card);
                        return new ApiResponse("Kartangiz bloklandi", false);
                    } else entry.setValue(entry.getValue() + 1);
                }
            }
            if (!bool) {
                hm.put(String.valueOf(transferDto.getNumber()), 1);
                return new ApiResponse("Notog'ri pin-code", false);
            }
        }
        return new ApiResponse("Tizimga xush kelibsiz",true);
    }

}
