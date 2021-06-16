package uz.pdp.online.lesson_6_task_2_atm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_6_task_2_atm.component.Calculation;
import uz.pdp.online.lesson_6_task_2_atm.component.DetectAuthenticationService;
import uz.pdp.online.lesson_6_task_2_atm.component.EmailSender;
import uz.pdp.online.lesson_6_task_2_atm.entity.*;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.CardTypeEnum;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.TransferType;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;
import uz.pdp.online.lesson_6_task_2_atm.payload.TransferDto;
import uz.pdp.online.lesson_6_task_2_atm.repository.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferService {
    @Autowired
    AtmRepos atmRepos;
    @Autowired
    AtmMoneyCaseRepos atmMoneyCaseRepos;
    @Autowired
    TransferRepos transferRepos;
    @Autowired
    CardRepos cardRepos;
    @Autowired
    Calculation calculation;
    @Autowired
    UserRepos userRepos;
    @Autowired
    EmailSender emailSender;

    public ApiResponse update(UUID atmId, AtmMoneyCase atmMoneyCaseDto) {
        boolean detectAuthentication = DetectAuthenticationService.detectAuthForEmployee();
        if (detectAuthentication) {
            Optional<Atm> optionalAtm = atmRepos.findById(atmId);
            if (!optionalAtm.isPresent())
                return new ApiResponse("Bankomat topilmadi", false);
            Atm atm = optionalAtm.get();
            AtmMoneyCase atmMoneyCase = new AtmMoneyCase();
            if (atm.getCardTypeEnum().name().equalsIgnoreCase("visa")) {
                atmMoneyCase.setUsd1(atmMoneyCaseDto.getUsd1());
                atmMoneyCase.setUsd5(atmMoneyCaseDto.getUsd5());
                atmMoneyCase.setUsd10(atmMoneyCaseDto.getUsd10());
                atmMoneyCase.setUsd50(atmMoneyCaseDto.getUsd50());
                atmMoneyCase.setUsd100(atmMoneyCaseDto.getUsd100());
                atmMoneyCaseRepos.save(atmMoneyCase);
                atm.setStatus(true);
                atmRepos.save(atm);
                Transfer transfer = new Transfer();
                transfer.setAtmId(atmId);
                transfer.setAtmMoneyCase(atmMoneyCaseDto);
                transfer.setDate(new SimpleDateFormat());
                transfer.setTransferType(TransferType.INCOME);
                transferRepos.save(transfer);
                return new ApiResponse("Bankomatning kupyuralar qutisiga xodim tomonidan pul qo'yildi", true);
            } else {
                atmMoneyCase.setUzs1000(atmMoneyCaseDto.getUzs1000());
                atmMoneyCase.setUzs5000(atmMoneyCaseDto.getUzs5000());
                atmMoneyCase.setUzs5000(atmMoneyCaseDto.getUzs5000());
                atmMoneyCase.setUzs10000(atmMoneyCaseDto.getUzs10000());
                atmMoneyCase.setUzs100000(atmMoneyCaseDto.getUzs100000());
                atmMoneyCaseRepos.save(atmMoneyCase);
                atm.setAtmMoneyCase(atmMoneyCase);
                atm.setStatus(true);
                atmRepos.save(atm);
                Transfer transfer = new Transfer();
                transfer.setAtmId(atmId);
                transfer.setAtmMoneyCase(atmMoneyCaseDto);
                transfer.setDate(new SimpleDateFormat());
                transfer.setTransferType(TransferType.INCOME);
                transferRepos.save(transfer);
                return new ApiResponse("Bankomatning kupyuralar qutisiga xodim tomonidan pul qo'yildi", true);
            }
        } else {
            return new ApiResponse("Sizda ushbu amalni bajarish uchun ruxsat yo'q", false);
        }
    }

    public List<Transfer> getAll() {
        return transferRepos.findAll();
    }

    public List<Transfer> getIncome(UUID atmId) {
        boolean detectAuthForDirector = DetectAuthenticationService.detectAuthForDirector();
        if (detectAuthForDirector) {
            Optional<Atm> optionalAtm = atmRepos.findById(atmId);
            if (!optionalAtm.isPresent())
                return null;
            List<Transfer> allByAtmIdAndTransferTypeAndDate = transferRepos.findAllByAtmIdAndTransferTypeAndDate(atmId, TransferType.INCOME, new SimpleDateFormat());
            return allByAtmIdAndTransferTypeAndDate;
        } else {
            return null;
        }
    }

    public List<Transfer> getOutcome(UUID atmId) {
        boolean detectAuthForDirector = DetectAuthenticationService.detectAuthForDirector();
        if (detectAuthForDirector) {
            Optional<Atm> optionalAtm = atmRepos.findById(atmId);
            if (!optionalAtm.isPresent())
                return null;
            List<Transfer> allByAtmIdAndTransferTypeAndDate = transferRepos.findAllByAtmIdAndTransferTypeAndDate(atmId, TransferType.OUTCOME, new SimpleDateFormat());
            return allByAtmIdAndTransferTypeAndDate;
        } else {
            return null;
        }
    }

    public List<Transfer> getByEmployee(UUID atmId, UUID employeeId) {
        boolean detectAuthForDirector = DetectAuthenticationService.detectAuthForDirector();
        if (detectAuthForDirector) {
            Optional<Atm> optionalAtm = atmRepos.findById(atmId);
            if (!optionalAtm.isPresent())
                return null;
            List<Transfer> allByAtmIdAndCreatedBy = transferRepos.findAllByAtmIdAndCreatedBy(atmId, employeeId);
            return allByAtmIdAndCreatedBy;
        } else {
            return null;
        }
    }

    public ApiResponse getMoney(TransferDto transferDto) {
        Double commission = 0.01;
        Optional<Atm> optionalAtm = atmRepos.findById(transferDto.getAtmId());
        if (!optionalAtm.isPresent() || !optionalAtm.get().isStatus())
            return new ApiResponse("Bankomat nosoz", false);
        Atm atm = optionalAtm.get();

        Optional<Card> optionalCard = cardRepos.findByNumber(transferDto.getNumber());
        if (!optionalCard.isPresent())
            return new ApiResponse("Karta topilmadi", false);
        Card card = optionalCard.get();
        if (!card.getCardType().getCardTypeEnum().equals(atm.getCardTypeEnum()))
            return new ApiResponse("Bankomat ushbu turdagi kartaga xizmat ko'rsatmaydi", false);

        AtmMoneyCase atmMoneyCase = atm.getAtmMoneyCase();
        Integer balance = calculation.balance(atmMoneyCase);

        if (balance < (transferDto.getAmount() * 0.01) + transferDto.getAmount() ||
                transferDto.getAmount() > atm.getMaxWithdrawAmount() ||
                transferDto.getAmount() < atm.getMinWithdrawAmount())
            return new ApiResponse("Xatolik", false);
        try {
            if (card.getCardType().equals(CardTypeEnum.VIZA)) {
                AtmMoneyCase atmMoneyCase1 = calculation.balanceToAtm(transferDto.getAmount(), "usd");
                if (atmMoneyCase1 == null)
                    return new ApiResponse("Xatolik", false);
                if (atmMoneyCase.getUsd100() >= atmMoneyCase1.getUsd100() &&
                        atmMoneyCase.getUsd50() >= atmMoneyCase1.getUsd50() &&
                        atmMoneyCase.getUsd10() >= atmMoneyCase1.getUsd10() &&
                        atmMoneyCase.getUsd5() >= atmMoneyCase1.getUsd5() &&
                        atmMoneyCase.getUsd1() >= atmMoneyCase1.getUsd1()) {
                    atmMoneyCase.setUsd100(atmMoneyCase.getUsd100() - atmMoneyCase1.getUsd100());
                    atmMoneyCase.setUsd50(atmMoneyCase.getUsd50() - atmMoneyCase1.getUsd50());
                    atmMoneyCase.setUsd10(atmMoneyCase.getUsd10() - atmMoneyCase1.getUsd10());
                    atmMoneyCase.setUsd5(atmMoneyCase.getUsd5() - atmMoneyCase1.getUsd5());
                    atmMoneyCase.setUsd1(atmMoneyCase.getUsd1() - atmMoneyCase1.getUsd1());
                    atmMoneyCaseRepos.save(atmMoneyCase);
                    atm.setAtmMoneyCase(atmMoneyCase);
                    atmRepos.save(atm);
                    card.setBalance(card.getBalance() - (balance + balance * commission));
                    cardRepos.save(card);
                    Transfer transfer = new Transfer();
                    transfer.setAtmId(atm.getId());
                    transfer.setAtmMoneyCase(atmMoneyCase1);
                    transfer.setDate(new SimpleDateFormat());
                    transfer.setTransferType(TransferType.OUTCOME);
                    transferRepos.save(transfer);
                    return new ApiResponse("Muvaffaqqiyatli", true);
                } else return new ApiResponse("Xatolik", false);
            } else {
                AtmMoneyCase atmMoneyCase1 = calculation.balanceToAtm(transferDto.getAmount(), "uzs");
                if (atmMoneyCase1 == null)
                    return new ApiResponse("Xatolik", false);
                if (atmMoneyCase.getUzs100000() >= atmMoneyCase1.getUzs100000() &&
                        atmMoneyCase.getUzs50000() >= atmMoneyCase1.getUzs50000() &&
                        atmMoneyCase.getUzs10000() >= atmMoneyCase1.getUzs10000() &&
                        atmMoneyCase.getUzs5000() >= atmMoneyCase1.getUzs5000() &&
                        atmMoneyCase.getUzs1000() >= atmMoneyCase1.getUzs1000()) {
                    atmMoneyCase.setUzs100000(atmMoneyCase.getUzs100000() - atmMoneyCase1.getUzs100000());
                    atmMoneyCase.setUzs50000(atmMoneyCase.getUzs50000() - atmMoneyCase1.getUzs50000());
                    atmMoneyCase.setUzs10000(atmMoneyCase.getUzs10000() - atmMoneyCase1.getUzs10000());
                    atmMoneyCase.setUzs5000(atmMoneyCase.getUzs5000() - atmMoneyCase1.getUzs5000());
                    atmMoneyCase.setUzs1000(atmMoneyCase.getUzs1000() - atmMoneyCase1.getUzs1000());
                    atmMoneyCaseRepos.save(atmMoneyCase);
                    if(card.getBank().getName().equalsIgnoreCase(atm.getBank().getName())) {
                        card.setBalance(card.getBalance() - (balance + balance * 0.005));
                    } else {
                        card.setBalance(card.getBalance() - (balance + balance * commission));
                    }
                    cardRepos.save(card);
                    Transfer transfer = new Transfer();
                    transfer.setAtmId(atm.getId());
                    transfer.setAtmMoneyCase(atmMoneyCase1);
                    transfer.setDate(new SimpleDateFormat());
                    transfer.setTransferType(TransferType.OUTCOME);
                    transferRepos.save(transfer);

                    Integer newBalance = calculation.balance(atmMoneyCase);
                    if (newBalance <= 10000000) {
                        User user = userRepos.findByRolesId(2);
                        ApiResponse apiResponse = emailSender.sendEmailToEmployee(user.getEmail(), "Bankomat hisobi", "Bankomatda 10 000 000 dan kam pul qoldi");
                        return apiResponse;
                    }
                    return new ApiResponse("Muvaffaqqiyatli", true);
                } else return new ApiResponse("Xatolik", false);
            }
        } catch (Exception e) {
            return new ApiResponse("Xatolik", false);
        }
    }
}
