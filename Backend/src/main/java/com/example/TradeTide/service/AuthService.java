package com.example.TradeTide.service;

import com.example.TradeTide.model.TwoFactorOTP;
import com.example.TradeTide.model.User;
import com.example.TradeTide.repo.UserRepo;

import com.example.TradeTide.service.JWTService;
import com.example.TradeTide.utils.OtpUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.webauthn.api.AuthenticatorResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;
    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private  EmailService emailService;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(User user) {
        User isExist = repo.findByEmail(user.getEmail());
        if (isExist != null) {
            throw new RuntimeException("email is already used with another account");
        }
        if (repo.findByUsername(user.getUsername())!= null) {
            throw new RuntimeException("username already exist!");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return user;
    }

    public String verify(User user) throws MessagingException {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {

            String jwt =jwtService.generateToken(user.getUsername());
            User authUser = repo.findByEmail(user.getUsername());
            if(authUser.getTwoFactorAuth().isEnabled()){
            String otp = OtpUtils.generateOtp();
                TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUser(authUser.getId());
            if (oldTwoFactorOTP != null) {
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
            }
            TwoFactorOTP twoFactorOTP = twoFactorOtpService.createTwoFactorOtp(authUser, otp, jwt);
                System.out.println(twoFactorOTP);
            emailService.sendVerificationEmail(authUser.getEmail(), otp);
                return twoFactorOTP.getId();
            }
            return jwt;
        } else {
            return  "fail";
        }
    }
}