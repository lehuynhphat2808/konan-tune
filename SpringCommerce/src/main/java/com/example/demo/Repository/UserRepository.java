package com.example.demo.Repository;

import com.example.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // UserRepository is responsible for connecting and retrieving product data and also acts as an interface between the DAO and Service classes.

    public Optional<User> findByCartId(UUID cartId);
    public Optional<User> findByUsername(String username);
    public Optional<User> findTopByEmail(String email);

    public User findByVerificationToken(String token);
}
