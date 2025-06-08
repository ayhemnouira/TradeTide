package com.example.TradeTide.repo;

import com.example.TradeTide.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByUsername(String username);
}
