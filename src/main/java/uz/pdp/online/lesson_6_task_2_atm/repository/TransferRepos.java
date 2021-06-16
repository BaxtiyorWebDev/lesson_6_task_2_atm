package uz.pdp.online.lesson_6_task_2_atm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_6_task_2_atm.entity.Transfer;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.TransferType;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public interface TransferRepos extends JpaRepository<Transfer, UUID> {

    List<Transfer> findAllByAtmIdAndTransferTypeAndDate(UUID atmId, TransferType transferType, SimpleDateFormat date);

    List<Transfer> findAllByAtmIdAndCreatedBy(UUID atmId, UUID createdBy);
}
