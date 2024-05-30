package com.example.demo.Service.Implement;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.nntFolder.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class JavaMailService  implements Observer {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserRepository userRepository;

    public void sendMail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);

    }

    public void sendVoucherApplied(String userEmail, String voucherCode){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@gmail.com");
        message.setTo(userEmail);
        message.setSubject("Bạn đã sử dụng mã giảm giá!");
        message.setText("Mã giảm giá : " + voucherCode);

        javaMailSender.send(message);
    }
    @Override
    public void onVoucherApply(UUID userId, String voucherCode) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            String email = user.getEmail();
            sendVoucherApplied(email, voucherCode);
        }
    }

}
