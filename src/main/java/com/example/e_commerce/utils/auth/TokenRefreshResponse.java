package com.example.e_commerce.utils.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenRefreshResponse {
    private String access_token;
    private String refresh_token;


}
