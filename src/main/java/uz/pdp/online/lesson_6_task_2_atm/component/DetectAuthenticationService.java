package uz.pdp.online.lesson_6_task_2_atm.component;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_6_task_2_atm.entity.User;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.RoleEnum;

@Service
public class DetectAuthenticationService {

    public static final boolean detectAuthForDirectorOrEmployee() {
        boolean allowed = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleEnum.DIRECTOR.name())||authority.getAuthority().equals(RoleEnum.EMPLOYEE.name())) {
                allowed = true;
                return allowed;
            }
        }
        return allowed;
    }

    public static final boolean detectAuthForDirector() {
        boolean allowed = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleEnum.DIRECTOR.name())) {
                allowed = true;
                return allowed;
            }
        }
        return allowed;
    }

    public static final boolean detectAuthForEmployee() {
        boolean allowed = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleEnum.EMPLOYEE.name())) {
                allowed = true;
                return allowed;
            }
        }
        return allowed;
    }
}
