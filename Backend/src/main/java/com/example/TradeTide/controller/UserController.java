package com.example.TradeTide.controller;

import com.example.TradeTide.domain.VerificationType;
import com.example.TradeTide.model.TwoFactorOTP;
import com.example.TradeTide.model.User;
import com.example.TradeTide.model.VerificationCode;
import com.example.TradeTide.service.EmailService;
import com.example.TradeTide.service.TwoFactorOtpService;
import com.example.TradeTide.service.UserService;
import com.example.TradeTide.service.VerificationCodeService;
import com.example.TradeTide.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    private String jwt;
    @Autowired
    private TwoFactorOtpService twoFactorOtpService;
    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader ("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(@RequestHeader ("Authorization") String jwt,
                                                    @PathVariable VerificationType verificationType) throws Exception {
        User user = userService.findUserByJwt(jwt);
       VerificationCode verificationCode = verificationCodeService
               .getVerificationCodeByUserId(user.getId());
        if (verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }
        if (verificationType.equals(VerificationType.EMAIL)){
            emailService.sendVerificationEmail(user.getEmail(), verificationCode.getOtp());
        }
        return new ResponseEntity<>("Verification otp sent successfully", HttpStatus.OK);
    }
    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @PathVariable String otp,
            @RequestHeader ("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user.getId());
        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL) ? verificationCode.getEmail() : verificationCode.getMobileNumber();
        boolean isVerified = verificationCode.getOtp().equals(otp);
        if (isVerified) {
            User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
            verificationCodeService.deleteVerificationCode(verificationCode);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("Invalid OTP provided for enabling two-factor authentication");
    }
}
