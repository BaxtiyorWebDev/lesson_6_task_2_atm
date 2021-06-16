package uz.pdp.online.lesson_6_task_2_atm.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.online.lesson_6_task_2_atm.entity.CardType;
import uz.pdp.online.lesson_6_task_2_atm.entity.Role;
import uz.pdp.online.lesson_6_task_2_atm.entity.User;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.CardTypeEnum;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.RoleEnum;
import uz.pdp.online.lesson_6_task_2_atm.repository.CardTypeRepos;
import uz.pdp.online.lesson_6_task_2_atm.repository.RoleRepos;
import uz.pdp.online.lesson_6_task_2_atm.repository.UserRepos;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    CardTypeRepos cardTypeRepos;
    @Autowired
    RoleRepos roleRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.sql.init.enabled}")
    private String mode;

    @Override
    public void run(String... args) throws Exception {
        if (mode.equals("true")) {
            for (CardTypeEnum value : CardTypeEnum.values()) {
                CardType cardType = new CardType();
                cardType.setCardTypeEnum(value);
                cardTypeRepos.save(cardType);
            }

            for (RoleEnum value : RoleEnum.values()) {
                Role role = new Role();
                role.setRoleEnum(value);
                roleRepos.save(role);
            }


            User user = new User();
            user.setFullName("Baxtiyor Muhammadaliyev");
            user.setEmail("baxtiyormuxammadaliyev@mail.ru");
            user.setPassword(passwordEncoder.encode("1234"));
            user.setEnabled(true);
            user.setRoles(Collections.singleton(roleRepos.getById(1)));
            userRepos.save(user);
        }
    }
}
