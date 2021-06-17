package uz.pdp.online.lesson_6_task_2_atm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_6_task_2_atm.component.EmailSender;
import uz.pdp.online.lesson_6_task_2_atm.entity.User;
import uz.pdp.online.lesson_6_task_2_atm.entity.enums.RoleEnum;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;
import uz.pdp.online.lesson_6_task_2_atm.payload.LoginDto;
import uz.pdp.online.lesson_6_task_2_atm.payload.RegisterDto;
import uz.pdp.online.lesson_6_task_2_atm.repository.RoleRepos;
import uz.pdp.online.lesson_6_task_2_atm.repository.UserRepos;
import uz.pdp.online.lesson_6_task_2_atm.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepos userRepos;
    @Autowired
    private RoleRepos roleRepos;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailSender mailSender;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;


    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(user.getUsername(), user.getRoles());
            return new ApiResponse("Token: " + token, true);
        } catch (Exception e) {
            return new ApiResponse("Parol yoki login xato", false);
        }
    }

    public ApiResponse register(RegisterDto registerDto) {
        boolean existsByEmail = userRepos.existsByEmail(registerDto.getEmail());
        if (existsByEmail)
            return new ApiResponse("Bunday foydalanuvchi tizimda mavjud", false);
        User addingUser = new User();
        addingUser.setFullName(registerDto.getFullName());
        addingUser.setEmail(registerDto.getEmail());
        if (registerDto.getRoleId() == 1)
            return new ApiResponse("Tizimga direktor ro'li mavjud", false);
        addingUser.setRoles(Collections.singleton(roleRepos.getById(registerDto.getRoleId())));
        addingUser.setPassword(passwordEncoder.encode("1234"));
        addingUser.setEmailCode(UUID.randomUUID().toString());
        userRepos.save(addingUser);

        mailSender.sendEmail(addingUser.getEmail(), addingUser.getEmailCode());
        return new ApiResponse("Foydalanuvchi MO ga kiritildi", true);
    }

    public ApiResponse verifyEmail(String email, String emailCode, LoginDto loginDto) {
        try {
            Optional<User> byEmailAndEmailCode = userRepos.findByEmailAndEmailCode(email, emailCode);
            if (!byEmailAndEmailCode.isPresent())
                return new ApiResponse("Bunday foydalanuvchi topilmadi", false);

            User user = byEmailAndEmailCode.get();
            if (user.getEmailCode() == null)
                return new ApiResponse("Siz ro'yxatdan allaqachon o'tgansiz", false);
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(loginDto.getPassword()));
            user.setEmailCode(null);
            userRepos.save(user);
            return new ApiResponse("Ro'yxatdan o'tdingiz", true);
        } catch (UsernameNotFoundException e) {
            return new ApiResponse("Xatolik", false);
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepos.findByEmail(username);
        if (byEmail.isPresent())
            return byEmail.get();
        throw new UsernameNotFoundException(byEmail + " nomli username tizimdan ro'yxatdan o'tmagan");
    }
}
