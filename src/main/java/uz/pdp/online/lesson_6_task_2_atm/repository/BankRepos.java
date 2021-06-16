package uz.pdp.online.lesson_6_task_2_atm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_6_task_2_atm.entity.Bank;

public interface BankRepos extends JpaRepository<Bank, Integer> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);
}
