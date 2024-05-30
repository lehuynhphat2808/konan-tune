package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.Implement.JavaMailService;
import com.example.demo.Service.Implement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MailController {
    @Autowired
    JavaMailService javaMailService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/sendVerification")
    public void sendVerificationEmail(@RequestBody User user) {

        String token = UUID.randomUUID().toString();

        user.setVerificationToken(token);

        javaMailService.sendMail(
                user.getEmail(),
                "Verify your email",
                "Click here to verify: http://localhost:8080/api/verify?token="+token
        );

        userRepository.save(user);

    }

    @GetMapping("/verify")
    public String verifyToken(@RequestParam String token) {

        User user = userRepository.findByVerificationToken(token);

        user.setEmailVerified(true);
        user.setVerificationToken(null);

        userRepository.save(user);

        // Đăng ký thành công
        return "OK";

    }
}
