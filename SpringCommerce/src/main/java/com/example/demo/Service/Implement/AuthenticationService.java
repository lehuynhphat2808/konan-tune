package com.example.demo.Service.Implement;

import com.example.demo.Utilities.AuthenticationRequest;
import com.example.demo.Utilities.AuthenticationResponse;
import com.example.demo.Model.Role;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Security.JwtService;
import com.example.demo.Utilities.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private  final AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    JavaMailService javaMailService;
    @Autowired
    UserRepository userRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        var newUser = User.builder().
                username(request.getUsername()).
                email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .nickname(request.getNickname())
                .role(Role.USER)
                .coin(BigDecimal.ZERO)
                .build();

        User user = userService.insert(newUser);
        String token = UUID.randomUUID().toString();
        var jwtToken = jwtService.generateToken(user);
        user.setVerificationToken(token);
        userRepository.save(user);
        javaMailService.sendMail(
                user.getEmail(),
                "Verify your email",
                "Click here to verify: http://localhost:8080/api/verify?token="+token
        );
        return AuthenticationResponse.builder().token(jwtToken).user(user).build();
    }

    public Boolean forgotPassword(String email) {
        Optional<User> userOptional = userRepository.findTopByEmail(email);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            UUID id = UUID.randomUUID();
            user.setPassword(passwordEncoder.encode(id.toString()));
            userRepository.save(user);
            javaMailService.sendMail(
                    email,
                    "Forgot Password",
                    "Dear " + user.getUsername() + "Your new password is: " + id.toString()
            );
            System.out.println(user);

            return true;
        } else {
            throw new NotImplementedException("Cannot found user have email: " + email);
        }
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (request.getUsername(),request.getPassword())
        );
        var user= repository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).user(user).build();
    }

}









