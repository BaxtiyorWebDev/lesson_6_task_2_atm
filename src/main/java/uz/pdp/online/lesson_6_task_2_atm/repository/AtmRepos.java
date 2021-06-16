package uz.pdp.online.lesson_6_task_2_atm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_6_task_2_atm.entity.Atm;

import java.util.UUID;

public interface AtmRepos extends JpaRepository<Atm, UUID> {
}
