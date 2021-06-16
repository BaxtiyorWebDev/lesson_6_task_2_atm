package uz.pdp.online.lesson_6_task_2_atm.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.pdp.online.lesson_6_task_2_atm.entity.AtmMoneyCase;
import uz.pdp.online.lesson_6_task_2_atm.entity.CardType;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.CardTypeEnum;
import uz.pdp.online.lesson_6_task_2_atm.repository.AtmRepos;

@Component
public class Calculation {

    @Autowired
    AtmRepos atmRepos;

    public CardTypeEnum contains(String test) {
        for (CardTypeEnum value : CardTypeEnum.values()) {
            if (value.name().equalsIgnoreCase(test))
                return value;
        }
        return null;
    }

    public Integer balance(AtmMoneyCase moneyCase) {
        return 1000 * moneyCase.getUzs1000() +
                5000 * moneyCase.getUzs5000() +
                10000 * moneyCase.getUzs10000() +
                50000 * moneyCase.getUzs50000() +
                100000 * moneyCase.getUzs100000() +

                moneyCase.getUsd1() +
                5 * moneyCase.getUsd5() +
                10 * moneyCase.getUsd10() +
                50 * moneyCase.getUsd50() +
                100 * moneyCase.getUsd100();
    }

    public AtmMoneyCase balanceToAtm(Integer balance, String type) {
        AtmMoneyCase atmMoneyCase = new AtmMoneyCase();
        if (type.equals("uzs")) {
            atmMoneyCase.setUzs100000(balance / 100000);
            atmMoneyCase.setUzs50000((balance % 100000) / 50000);
            atmMoneyCase.setUzs10000(balance % 100000 % 50000 / 10000);
            atmMoneyCase.setUzs5000(balance % 100000 % 50000 % 10000 / 5000);
            atmMoneyCase.setUzs1000(balance % 100000 % 50000 % 10000 % 5000 / 1000);
            if (balance % 100000 % 50000 % 10000 % 5000 % 1000 == 0)
                return atmMoneyCase;
            return null;
        } else
            atmMoneyCase.setUsd100(balance / 100);
        atmMoneyCase.setUsd50((balance % 100) / 50);
        atmMoneyCase.setUsd10(balance % 100 % 50 / 10);
        atmMoneyCase.setUsd5(balance % 100 % 50 % 10 / 5);
        atmMoneyCase.setUsd1(balance % 100 % 50 % 10 % 5);
        if (balance % 100 % 50 % 10 % 5 ==0)
            return atmMoneyCase;
        return null;
    }
}
