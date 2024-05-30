package com.example.demo.Utilities;

import com.example.demo.Model.User;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuthenticationResponse {

    @JsonView(Views.Public.class)
    private String token;

    @JsonView(Views.Public.class)
    private User user;
}
