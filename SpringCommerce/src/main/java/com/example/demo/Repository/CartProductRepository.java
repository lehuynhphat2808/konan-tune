package com.example.demo.Repository;

import com.example.demo.Model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, UUID> {
    // CartProductRepository is responsible for connecting and retrieving product data and also acts as an interface between the DAO and Service classes.

    public Optional<CartProduct> findByProductId(UUID productId);

    public boolean existsByProductId(UUID productId);
}
