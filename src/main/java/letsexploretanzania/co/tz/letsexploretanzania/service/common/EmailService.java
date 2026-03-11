package letsexploretanzania.co.tz.letsexploretanzania.service.common;

import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp+" This will expire in 5 minutes");
        mailSender.send(message);
    }

    public void sendGenericEmail(String toAddress,String subject ,String bodyText)
    {
        AWSService.sendEmailViaSES(toAddress,subject,bodyText);
    }
}

