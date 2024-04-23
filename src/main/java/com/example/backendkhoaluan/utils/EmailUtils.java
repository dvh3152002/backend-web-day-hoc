package com.example.backendkhoaluan.utils;

import com.example.backendkhoaluan.exception.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {
    @Autowired
    private JavaMailSender javaMailSender;

    public void  sendOtpEmail(String email,String otp){
        try {
            MimeMessage mimeMessage=javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Xác thực OTP");
            mimeMessageHelper.setText("""
                    Your OTP: <b>%s</b>
                    """.formatted(otp),true);
            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            throw new EmailException(e.getMessage());
        }
    }
}
