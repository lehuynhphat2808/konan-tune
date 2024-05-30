package com.example.demo.Repository;

import com.example.demo.Model.Order;
import com.example.demo.Model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    // OrderRepository is responsible for connecting and retrieving product data and also acts as an interface between the DAO and Service classes.

    public Optional<Order> findByCartId(UUID cartId);
    public Page<Order> findByUserIdAndStatus(UUID userId, OrderStatus orderStatus, Pageable pageable);
    public Page<Order> findByStatus(OrderStatus orderStatus, Pageable pageable);

}
