package com.example.demo.Repository;

import com.example.demo.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    // CartRepository is responsible for connecting and retrieving product data and also acts as an interface between the DAO and Service classes.


}
