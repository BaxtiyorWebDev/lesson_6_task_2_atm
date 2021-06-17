package uz.pdp.online.lesson_6_task_2_atm.component;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_6_task_2_atm.entity.User;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.RoleEnum;

import java.util.Optional;

@Service
public class DetectAuthenticationService {

    public static final boolean detectAuthForDirectorOrEmployee() {
        boolean allowed = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            User user = (User) authentication.getPrincipal();
            for (GrantedAuthority authority : user.getAuthorities()) {
                if (authority.getAuthority().equals(RoleEnum.DIRECTOR.name()) || authority.getAuthority().equals(RoleEnum.EMPLOYEE.name())) {
                    allowed = true;
                    return allowed;
                }
            }
            return allowed;
        }
        return false;
    }

    public static final boolean detectAuthForDirector() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            boolean allowed = false;
            User user = (User) authentication.getPrincipal();
            for (GrantedAuthority authority : user.getAuthorities()) {
                if (authority.getAuthority().equals(RoleEnum.DIRECTOR.name())) {
                    allowed = true;
                    return allowed;
                }
            }
            return allowed;
        }
        return false;
    }

    public static final boolean detectAuthForEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            boolean allowed = false;
            User user = (User) authentication.getPrincipal();
            for (GrantedAuthority authority : user.getAuthorities()) {
                if (authority.getAuthority().equals(RoleEnum.EMPLOYEE.name())) {
                    allowed = true;
                    return allowed;
                }
            }
            return allowed;
        }
        return false;
    }
}
