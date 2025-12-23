package com.example.TradeTide.config;

import com.example.TradeTide.service.JWTService;
import com.example.TradeTide.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

  @Autowired private JWTService jwtService;

  @Autowired ApplicationContext context;

  @Override
  protected void doFilterInternal(
          HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    // Get the request path
    String path = request.getRequestURI();

    // Skip JWT validation for public endpoints
    if (path.startsWith("/coins") ||
            path.equals("/login") ||
            path.equals("/register") ||
            path.startsWith("/two-factor/otp") ||
            path.startsWith("/auth/users/reset-password") ||
            path.startsWith("/auth/google") ||
            path.startsWith("/oauth2/authorization/google") ||
            path.startsWith("/login/oauth2/code/google")) {

      filterChain.doFilter(request, response);
      return;  // Exit early, don't process JWT
    }

    // Existing JWT logic for protected endpoints
    String authHeader = request.getHeader("Authorization");
    String token = null;
    String email = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7).trim();
      email = jwtService.extractEmail(token);
    }

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails =
              context.getBean(MyUserDetailsService.class).loadUserByUsername(email);
      if (jwtService.validateToken(token, userDetails)) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}