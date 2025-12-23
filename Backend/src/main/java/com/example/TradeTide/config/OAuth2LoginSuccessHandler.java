package com.example.TradeTide.config;

import com.example.TradeTide.model.TwoFactorOTP;
import com.example.TradeTide.model.User;
import com.example.TradeTide.repo.UserRepo;
import com.example.TradeTide.service.EmailService;
import com.example.TradeTide.service.JWTService;
import com.example.TradeTide.service.TwoFactorOtpService;
import com.example.TradeTide.utils.OtpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String email = oauthToken.getPrincipal().getAttribute("email");
        String name = oauthToken.getPrincipal().getAttribute("name");
        String googleId = oauthToken.getPrincipal().getAttribute("sub"); // Google user ID

        User user = userRepo.findByEmail(email);

        if (user == null) {
            // New user - create account with GOOGLE provider
            user = new User();
            user.setEmail(email);
            user.setUsername(name != null ? name : email);
            user.getProviders().add("GOOGLE"); // Add to Set
            user.setGoogleId(googleId);
            userRepo.save(user);
        } else {
            // Existing user found
            if (user.getProviders().contains("LOCAL") && !user.getProviders().contains("GOOGLE")) {
                // Account exists with password only - prevent OAuth login
                String redirectUrl = "http://localhost:4200/login?error=account_exists_with_password";
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                return;
            }

            // If user doesn't have GOOGLE provider yet, add it (account linking)
            if (!user.getProviders().contains("GOOGLE")) {
                user.getProviders().add("GOOGLE");
                user.setGoogleId(googleId);
                userRepo.save(user);
            }
        }

        // Continue with 2FA or normal login flow
        if (user.getTwoFactorAuth().isEnabled()) {
            String jwt = jwtService.generateToken(user.getUsername());
            String otp = OtpUtils.generateOtp();
            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUser(user.getId());
            if (oldTwoFactorOTP != null) {
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
            }
            TwoFactorOTP twoFactorOTP = twoFactorOtpService.createTwoFactorOtp(user, otp, jwt);
            try {
                emailService.sendVerificationEmail(user.getEmail(), otp);
            } catch (MessagingException e) {
                logger.error("Failed to send 2FA email: ", e);
                String redirectUrl = "http://localhost:4200/login?error=email_failed";
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                return;
            }
            String redirectUrl = "http://localhost:4200/verify-2fa?id=" + twoFactorOTP.getId();
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else {
            String jwt = jwtService.generateToken(user.getUsername());
            String redirectUrl = "http://localhost:4200/login?token=" + jwt;
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
    }
}