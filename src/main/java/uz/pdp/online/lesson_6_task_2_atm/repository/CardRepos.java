package uz.pdp.online.lesson_6_task_2_atm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_6_task_2_atm.entity.Card;
import uz.pdp.online.lesson_6_task_2_atm.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface CardRepos extends JpaRepository<Card, UUID> {
    Optional<Card> findByNumber(Long number);

    Optional<Card> findByNumberAndUserId(Long number, UUID user_id);

    Optional<Card> findAllByUserId(UUID user_id);
}
