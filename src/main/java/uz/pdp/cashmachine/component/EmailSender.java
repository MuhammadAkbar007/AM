package uz.pdp.cashmachine.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    @Autowired
    JavaMailSender javaMailSender;

    public boolean sendEmail(String sendingEmail, String text) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("moreply@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Yangi xabar");
            mailMessage.setText(text);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
