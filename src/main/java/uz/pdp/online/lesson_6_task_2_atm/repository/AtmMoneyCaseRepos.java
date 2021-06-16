package uz.pdp.online.lesson_6_task_2_atm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_6_task_2_atm.entity.AtmMoneyCase;

import java.util.UUID;

public interface AtmMoneyCaseRepos extends JpaRepository<AtmMoneyCase, UUID> {
}
