package uz.pdp.online.lesson_6_task_2_atm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_6_task_2_atm.component.Calculation;
import uz.pdp.online.lesson_6_task_2_atm.component.DetectAuthenticationService;
import uz.pdp.online.lesson_6_task_2_atm.entity.Address;
import uz.pdp.online.lesson_6_task_2_atm.entity.Atm;
import uz.pdp.online.lesson_6_task_2_atm.entity.Card;
import uz.pdp.online.lesson_6_task_2_atm.entity.CardType;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.CardTypeEnum;
import uz.pdp.online.lesson_6_task_2_atm.payload.AddressDto;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;
import uz.pdp.online.lesson_6_task_2_atm.payload.AtmDto;
import uz.pdp.online.lesson_6_task_2_atm.payload.CardDto;
import uz.pdp.online.lesson_6_task_2_atm.repository.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AtmService {

    @Autowired
    AtmRepos atmRepos;
    @Autowired
    CardTypeRepos cardTypeRepos;
    @Autowired
    BankRepos bankRepos;
    @Autowired
    CardRepos cardRepos;
    @Autowired
    Calculation calculation;

    public ApiResponse addAtm(AtmDto atmDto) {
        boolean detectAuthentication = DetectAuthenticationService.detectAuthForDirectorOrEmployee();
        if (detectAuthentication) {
            Atm atm = new Atm();

            CardTypeEnum contains = calculation.contains(atmDto.getCardType());
            if (contains == null)
                return new ApiResponse("Karta toifasi mos kelmaydi", false);
            atm.setCardTypeEnum(contains);
            atm.setMaxWithdrawAmount(atmDto.getMaxWithDrawAmount());
            atm.setMinWithdrawAmount(atmDto.getMinWithDrawAmount());
            atm.setBank(bankRepos.getById(atmDto.getBankId()));
            atm.setStatus(true);
            AddressDto addressDto = atmDto.getAddressDto();
            Address address = new Address();
            address.setCountry(addressDto.getCountry());
            address.setRegion(addressDto.getRegion());
            address.setDistrict(addressDto.getDistrict());
            address.setStreet(addressDto.getStreet());

            atm.setAddress(address);

            atmRepos.save(atm);
            return new ApiResponse("Bankomat qo'shildi", true);
        }
        return new ApiResponse("Siz ushbu amalni bajara olmaysiz", false);
    }

    public ApiResponse editAtm(UUID uuid, AtmDto atmDto) {
        boolean detectAuthentication = DetectAuthenticationService.detectAuthForDirectorOrEmployee();
        if (detectAuthentication) {
            Optional<Atm> optionalAtm = atmRepos.findById(uuid);
            if (!optionalAtm.isPresent())
                return new ApiResponse("Bunday bankomat mavjud emas", false);
            Atm editingAtm = optionalAtm.get();
            editingAtm.setMaxWithdrawAmount(atmDto.getMaxWithDrawAmount());
            atmRepos.save(editingAtm);
            return new ApiResponse("Ma'lumotlar tahrirlandi", true);
        }
        return new ApiResponse("Siz ushbu amalni bajara olmaysiz", false);
    }

    public Page<Atm> getPagesAtm(int page) {
        boolean detectAuthentication = DetectAuthenticationService.detectAuthForDirectorOrEmployee();
        if (detectAuthentication) {
            Pageable pageable = PageRequest.of(page, 20);
            Page<Atm> atmPage = atmRepos.findAll(pageable);
            return atmPage;
        }
        return null;
    }

    public Atm getById(UUID uuid) {
        boolean detectAuthentication = DetectAuthenticationService.detectAuthForDirectorOrEmployee();
        if (detectAuthentication) {
            Optional<Atm> optionalAtm = atmRepos.findById(uuid);
            return optionalAtm.orElse(null);
        }
        return null;
    }

    public ApiResponse deleteAtmById(UUID uuid) {
        try {
            boolean detectAuthentication = DetectAuthenticationService.detectAuthForDirector();
            if (detectAuthentication) {
                atmRepos.deleteById(uuid);
                return new ApiResponse("Ma'lumot o'chirildi", true);
            } else {
                return new ApiResponse("Siz ushbu so'rovni amalga oshira olmaysiz", false);
            }
        } catch (Exception e) {
            return new ApiResponse("Ma'lumot topilmadi", false);
        }
    }

    Map<String, Integer> hm = new HashMap<>();

    public ApiResponse verifyCard(CardDto cardDto) {
        Optional<Card> optionalCard = cardRepos.findByNumber(cardDto.getNumber());
        if (!optionalCard.isPresent() || !optionalCard.get().isActive())
            return new ApiResponse("Karta mavjud emas yoki nofaol holatda", false);
        Card card = optionalCard.get();
        boolean bool = false;
        if (card.getPinCode() != cardDto.getPinCode()) {
            for (Map.Entry<String, Integer> entry : hm.entrySet()) {
                if (entry.getKey().equals(cardDto.getNumber())) {
                    bool = true;
                    if (entry.getValue() == 3) {
                        card.setActive(false);
                        cardRepos.save(card);
                        return new ApiResponse("Kartangiz bloklandi", false);
                    } else entry.setValue(entry.getValue() + 1);
                }
            }
            if (!bool) {
                hm.put(String.valueOf(cardDto.getNumber()), 1);
                return new ApiResponse("Notog'ri pin-code", false);
            }
        }
        return new ApiResponse("Karta verifikatsiyadan o'tkazildi", true);
    }

    public ApiResponse balance(UUID uuid) {
        boolean detectAuthForDirector = DetectAuthenticationService.detectAuthForDirector();
        if (detectAuthForDirector) {
            Optional<Atm> optionalAtm = atmRepos.findById(uuid);
            if (!optionalAtm.isPresent())
                return new ApiResponse("Bankomat topilmadi", false);
            Atm atm = optionalAtm.get();
            Integer balance = calculation.balance(atm.getAtmMoneyCase());
            return new ApiResponse("Bankomat hisobi: " + balance, true);
        } else {
            return new ApiResponse("Sizga ushbu amalni bajarishga ruxsat yo'q",false);
        }
    }
}
