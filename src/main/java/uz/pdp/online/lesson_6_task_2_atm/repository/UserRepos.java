package uz.pdp.online.lesson_6_task_2_atm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_6_task_2_atm.entity.Role;
import uz.pdp.online.lesson_6_task_2_atm.entity.User;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepos extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);

    Optional<User> findByEmail(String email);

    User findByRolesId(Integer roles_id);

}
