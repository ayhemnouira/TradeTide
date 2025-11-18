package com.example.TradeTide.controller;

import com.example.TradeTide.domain.VerificationType;
import com.example.TradeTide.model.*;
import com.example.TradeTide.request.ForgotPasswordTokenRequest;
import com.example.TradeTide.request.ResetPasswordRequest;
import com.example.TradeTide.response.ApiResponse;
import com.example.TradeTide.response.AuthResponse;
import com.example.TradeTide.service.*;
import com.example.TradeTide.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(Authentication authentication) throws Exception {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(Authentication authentication,
                                                      @PathVariable VerificationType verificationType) throws Exception {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
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
            Authentication authentication) throws Exception {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
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

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp
            (@RequestBody ForgotPasswordTokenRequest req) throws Exception {
        User user = userService.findUserByEmail(req.getSendTo());
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        System.out.println("========== SENDING OTP ==========");
        System.out.println("Generated OTP: " + otp);
        System.out.println("Session ID: " + id);
        System.out.println("User Email: " + user.getEmail());
        System.out.println("=================================");

        ForgotPasswordToken token = forgotPasswordService.createToken(user, id, otp, req.getVerificationType(), req.getSendTo());
        if (req.getVerificationType().equals(VerificationType.EMAIL)) {
            emailService.sendVerificationEmail(user.getEmail(), token.getOtp());
        }
        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password reset otp sent successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtpForReset(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest req) throws Exception {

        System.out.println("========== VERIFYING OTP ==========");
        System.out.println("Session ID received: " + id);
        System.out.println("OTP received: " + req.getOtp());

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

        if (forgotPasswordToken == null) {
            System.out.println("ERROR: Token not found for ID: " + id);
            throw new Exception("Invalid or expired token");
        }

        System.out.println("Token found!");
        System.out.println("Stored OTP: " + forgotPasswordToken.getOtp());
        System.out.println("Received OTP: " + req.getOtp());
        System.out.println("OTP Type - Stored: " + forgotPasswordToken.getOtp().getClass().getName());
        System.out.println("OTP Type - Received: " + req.getOtp().getClass().getName());
        System.out.println("Stored OTP length: " + forgotPasswordToken.getOtp().length());
        System.out.println("Received OTP length: " + req.getOtp().length());
        System.out.println("Stored OTP trimmed: '" + forgotPasswordToken.getOtp().trim() + "'");
        System.out.println("Received OTP trimmed: '" + req.getOtp().trim() + "'");
        System.out.println("Are they equal? " + forgotPasswordToken.getOtp().equals(req.getOtp()));
        System.out.println("Are they equal (trimmed)? " + forgotPasswordToken.getOtp().trim().equals(req.getOtp().trim()));
        System.out.println("===================================");

        boolean isVerified = forgotPasswordToken.getOtp().equals(req.getOtp());
        if (isVerified) {
            ApiResponse response = new ApiResponse();
            response.setMessage("OTP verified successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new Exception("Invalid OTP provided for resetting password");
    }

    @PatchMapping("/auth/users/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest req) throws Exception {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);
        if (forgotPasswordToken == null) {
            throw new Exception("Invalid or expired token");
        }
        userService.updatePassword(forgotPasswordToken.getUser(), req.getNewPassword());
        forgotPasswordService.deleteToken(forgotPasswordToken);
        ApiResponse response = new ApiResponse();
        response.setMessage("Password reset successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}