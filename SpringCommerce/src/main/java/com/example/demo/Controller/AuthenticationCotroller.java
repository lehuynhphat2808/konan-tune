package com.example.demo.Controller;


import com.example.demo.Repository.UserRepository;
import com.example.demo.Security.JwtService;
import com.example.demo.Service.Implement.AuthenticationService;
import com.example.demo.Service.Implement.UserService;
import com.example.demo.Utilities.AuthenticationRequest;
import com.example.demo.Utilities.AuthenticationResponse;
import com.example.demo.Utilities.Response;
import com.example.demo.Utilities.Views;
import com.example.demo.nntFolder.Strategy.FileLog;
import com.example.demo.nntFolder.Strategy.LogContext;
import com.example.demo.nntFolder.Strategy.LogStrategy;
import com.fasterxml.jackson.annotation.JsonView;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")

public class AuthenticationCotroller {
    private final PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserRepository repository;
    @Autowired
    JwtService jwtService;



    @JsonView(Views.Public.class)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            if(response.getUser().isEmailVerified()){
                LogStrategy fileLog = new FileLog();
                LogContext logContext = new LogContext();
                logContext.setLogStrategy(fileLog);
                logContext.writeLog(response.getUser().getEmail());
                return Response.createResponse(HttpStatus.OK, "login successfully", response);
            } else {
                return Response.createResponse(HttpStatus.NOT_ACCEPTABLE, "The account has not been verified, please check your email", null);
            }
        } catch (BadCredentialsException e) {
            // Xử lý trường hợp xác thực thất bại: Sai tên người dùng hoặc mật khẩu
            return Response.createResponse(HttpStatus.UNAUTHORIZED, "Wrong username or password", null);
        } catch (UsernameNotFoundException e) {
            // Xử lý trường hợp người dùng không tồn tại
            return Response.createResponse(HttpStatus.NOT_FOUND, "Username is not exists", null);
        } catch (IllegalArgumentException e) {
            return Response.createResponse(HttpStatus.NOT_IMPLEMENTED, e.getMessage(), null);
        } catch (Exception e) {
            return Response.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", null);
        }
    }


}

