package uz.pdp.online.lesson_6_task_2_atm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_6_task_2_atm.component.Calculation;
import uz.pdp.online.lesson_6_task_2_atm.component.DetectAuthenticationService;
import uz.pdp.online.lesson_6_task_2_atm.component.EmailSender;
import uz.pdp.online.lesson_6_task_2_atm.component.VerifyCard;
import uz.pdp.online.lesson_6_task_2_atm.entity.*;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.CardTypeEnum;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.TransferType;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;
import uz.pdp.online.lesson_6_task_2_atm.payload.TransferDto;
import uz.pdp.online.lesson_6_task_2_atm.repository.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TransferService {
    @Autowired
    private AtmRepos atmRepos;
    @Autowired
    private AtmMoneyCaseRepos atmMoneyCaseRepos;
    @Autowired
    private TransferRepos transferRepos;
    @Autowired
    private CardRepos cardRepos;
    @Autowired
    private Calculation calculation;
    @Autowired
    private UserRepos userRepos;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private VerifyCard verifyCard;

    // xodim bankomatga pul qo'yishi
    public ApiResponse update(UUID atmId, AtmMoneyCase atmMoneyCaseDto) {
        boolean detectAuthentication = DetectAuthenticationService.detectAuthForEmployee();
        if (detectAuthentication) {
            Optional<Atm> optionalAtm = atmRepos.findById(atmId);
            if (!optionalAtm.isPresent())
                return new ApiResponse("Bankomat topilmadi", false);
            Atm atm = optionalAtm.get();
            AtmMoneyCase atmMoneyCase = atm.getAtmMoneyCase();
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
        boolean detectAuthForDirectorOrEmployee = DetectAuthenticationService.detectAuthForDirectorOrEmployee();
        if (detectAuthForDirectorOrEmployee) {
            return transferRepos.findAll();
        }
        return null;
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

    // bankomatdan pull yechish
    public ApiResponse getMoney(TransferDto transferDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            ApiResponse verifyCard = this.verifyCard.verifyCard(transferDto);
            if (verifyCard.isSuccess()) {
                Double commission = 0.01;
                Optional<Atm> optionalAtm = atmRepos.findById(transferDto.getAtmId());
                if (!optionalAtm.isPresent() || !optionalAtm.get().isStatus())
                    return new ApiResponse("Bankomat nosoz", false);
                Atm atm = optionalAtm.get();

                Optional<Card> optionalCard = cardRepos.findByNumber(transferDto.getNumber());
                Card card = optionalCard.get();
                if (!card.getCardType().getCardTypeEnum().equals(atm.getCardTypeEnum()))
                    return new ApiResponse("Bankomat ushbu turdagi kartaga xizmat ko'rsatmaydi", false);

                AtmMoneyCase atmMoneyCase = atm.getAtmMoneyCase();
                ApiResponse balance = calculation.balance(atmMoneyCase);

                try {
                    if (card.getCardType().getCardTypeEnum().equals(CardTypeEnum.VIZA)) {

                        if (balance.getUsd() < (transferDto.getAmount() * 0.01) + transferDto.getAmount() ||
                                transferDto.getAmount() > atm.getMaxWithdrawAmount() ||
                                transferDto.getAmount() < atm.getMinWithdrawAmount())
                            return new ApiResponse("Summa miqdori notog'ri kiritildi", false);

                        AtmMoneyCase atmMoneyCase1 = calculation.balanceToAtm(transferDto.getAmount(), SystemUtils.usd);
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
                            card.setBalance(card.getBalance() - (balance.getUsd() + balance.getUsd() * commission));
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

                        if (balance.getUzs() < (transferDto.getAmount() * 0.01) + transferDto.getAmount() ||
                                transferDto.getAmount() > atm.getMaxWithdrawAmount() ||
                                transferDto.getAmount() < atm.getMinWithdrawAmount())
                            return new ApiResponse("Summa miqdori notog'ri kiritildi", false);

                        AtmMoneyCase atmMoneyCase1 = calculation.balanceToAtm(transferDto.getAmount(), SystemUtils.uzs);
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
                            if (card.getBank().getName().equalsIgnoreCase(atm.getBank().getName())) {
                                card.setBalance(card.getBalance() - (balance.getUzs() + balance.getUzs() * 0.005));
                            } else {
                                card.setBalance(card.getBalance() - (balance.getUzs() + balance.getUzs() * commission));
                            }
                            cardRepos.save(card);
                            Transfer transfer = new Transfer();
                            transfer.setAtmId(atm.getId());
                            transfer.setAtmMoneyCase(atmMoneyCase1);
                            transfer.setDate(new SimpleDateFormat());
                            transfer.setTransferType(TransferType.OUTCOME);
                            transferRepos.save(transfer);

                            ApiResponse newBalance = calculation.balance(atmMoneyCase);
                            if (newBalance.isSuccess()) {// lessMoney check (no success it's wrong)
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
            return verifyCard;
        }
        return new ApiResponse("Siz tizimga kira olmaysiz", false);
    }
}
