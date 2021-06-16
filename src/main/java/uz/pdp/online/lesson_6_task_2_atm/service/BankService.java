package uz.pdp.online.lesson_6_task_2_atm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_6_task_2_atm.component.DetectAuthenticationService;
import uz.pdp.online.lesson_6_task_2_atm.entity.Address;
import uz.pdp.online.lesson_6_task_2_atm.entity.Bank;
import uz.pdp.online.lesson_6_task_2_atm.payload.AddressDto;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;
import uz.pdp.online.lesson_6_task_2_atm.payload.BankDto;
import uz.pdp.online.lesson_6_task_2_atm.repository.BankRepos;

import java.util.Optional;

@Service
public class BankService {

    @Autowired
    BankRepos bankRepos;
    @Autowired
    DetectAuthenticationService detectAuthenticationService;


    public ApiResponse addBank(BankDto bankDto) {
        boolean detectAuthForDirector = DetectAuthenticationService.detectAuthForDirector();
        if (detectAuthForDirector) {
            boolean existsByName = bankRepos.existsByName(bankDto.getName());
            if (existsByName)
                return new ApiResponse("Bunday bank mavjud", false);

            Bank bank = new Bank();
            bank.setName(bankDto.getName());

            AddressDto addressDto = bankDto.getAddressDto();
            Address address = new Address();
            address.setCountry(addressDto.getCountry());
            address.setRegion(addressDto.getRegion());
            address.setDistrict(addressDto.getDistrict());
            address.setStreet(addressDto.getStreet());

            bank.setAddress(address);
            bankRepos.save(bank);
            return new ApiResponse("Ma'lumot saqlandi", true);
        } else {
            return new ApiResponse("Sizga ushbu amalni bajarishga ruxsat berilmagan",false);
        }
    }

    public Bank getBankById(Integer id) {
        Optional<Bank> optionalBank = bankRepos.findById(id);
        return optionalBank.orElse(null);
    }

    public ApiResponse editBank(Integer id, BankDto bankDto) {
        boolean detectAuthForDirector = DetectAuthenticationService.detectAuthForDirector();
        if (detectAuthForDirector) {
            boolean existsByNameAndIdNot = bankRepos.existsByNameAndIdNot(bankDto.getName(), id);
            if (existsByNameAndIdNot)
                return new ApiResponse("Bunday ma'lumot mavjud", false);
            Optional<Bank> optionalBank = bankRepos.findById(id);
            if (!optionalBank.isPresent())
                return new ApiResponse("Ma'lumot topilmadi", false);

            Bank editingBank = optionalBank.get();
            editingBank.setName(bankDto.getName());
            Address editingBankAddress = editingBank.getAddress();
            AddressDto addressDto = bankDto.getAddressDto();
            editingBankAddress.setCountry(addressDto.getCountry());
            editingBankAddress.setRegion(addressDto.getRegion());
            editingBankAddress.setDistrict(addressDto.getDistrict());
            editingBankAddress.setStreet(addressDto.getStreet());
            editingBank.setAddress(editingBankAddress);
            bankRepos.save(editingBank);
            return new ApiResponse("Ma'lumot saqlandi", true);
        }else {
            return new ApiResponse("Sizga ushbu amalni bajarishga ruxsat berilmagan",false);
        }
    }

    public ApiResponse deleteBankById(Integer id) {
        boolean detectAuthForDirector = DetectAuthenticationService.detectAuthForDirector();
        if (detectAuthForDirector) {
            try {
                bankRepos.deleteById(id);
                return new ApiResponse("Ma'lumot o'chirildi", true);
            } catch (Exception e) {
                return new ApiResponse("Ma'lumot topilmadi", false);
            }
        } else {
            return new ApiResponse("Sizga ushbu amalni bajarishga ruxsat berilmagan",false);
        }
    }
}
