package com.example.TradeTide.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String otp;
    private String newPassword;
}
