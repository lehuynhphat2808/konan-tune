package com.example.demo.Utilities;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoRequest {
    private String nickname;
    private String phone;
    private String email;
    private String password;
    private String linkImage;
}
