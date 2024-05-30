package com.example.demo.Service.Implement;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.nntFolder.Observer;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SmsService implements Observer {
    final
    UserRepository userRepository;

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.from.number}")
    private String fromNumber;

    public SmsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void sendSms(String toNumber, String message) {
        Twilio.init(accountSid, authToken);

        Message.creator(
                        new PhoneNumber(toNumber),
                        new PhoneNumber(fromNumber),
                        message)
                .create();
    }

    @Override
    public void onVoucherApply(UUID userId, String voucherCode) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            String phone = user.getPhone();
            sendSms(phone, "Mã giảm giá : "+voucherCode+" đã được sử dụng");

        }
    }
}