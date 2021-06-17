package uz.pdp.online.lesson_6_task_2_atm.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_6_task_2_atm.payload.ApiResponse;

@Component
public class EmailSender {
    @Autowired
    private JavaMailSender javaMailSender;

    public ApiResponse sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("GreatBank");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Tizimga kirishni tasdiqlash");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?email=" + sendingEmail + "&emailCode=" + emailCode + "'>Ushbu havolaga o'ting</a>");
            javaMailSender.send(mailMessage);
            return new ApiResponse("e-pochtasiga tasdiqlash uchun havola yuborildi", true);
        } catch (Exception e) {
            return new ApiResponse("e-pochtasiga xabar yuborishda xatolik yuz berdi", false);
        }
    }

    public ApiResponse sendEmailToEmployee(String sendingEmail, String title, String text) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("GreatBank");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject(title);
            mailMessage.setText(text);
            javaMailSender.send(mailMessage);
            return new ApiResponse("Xabar yuborildi", true);
        } catch (Exception e) {
            return new ApiResponse("Xabar yuborishda xatolik yuz berdi", false);
        }
    }
}
