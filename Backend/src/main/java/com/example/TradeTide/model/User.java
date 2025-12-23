package com.example.TradeTide.model;

import com.example.TradeTide.domain.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;

    @Column(unique = true) // Enforce unique email in database
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_auth_providers")
    private Set<String> providers = new HashSet<>(); // Can have both LOCAL and GOOGLE

    private String googleId; // Store Google user ID for verification
}
