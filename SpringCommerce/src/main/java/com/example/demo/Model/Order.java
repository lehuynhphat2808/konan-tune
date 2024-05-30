package com.example.demo.Model;

import com.example.demo.Utilities.Views;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order extends BaseModel {

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonView(Views.Public.class)
    @Column(name = "order_date")
    private LocalDateTime orderDate = LocalDateTime.now();

    @JsonView(Views.Public.class)
    @Column(name = "address")
    private String address;

    @JsonView(Views.Public.class)
    @Column(name = "contactPhoneNumber")
    private String contactPhoneNumber;

    @JsonView(Views.Public.class)
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "user_id")
    private UUID userId;

    @JsonView(Views.Public.class)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"cart", "orders","phone", "email", "password", "cartId", "role"})
    private User user;

    @Column(name = "cart_id")
    private UUID cartId;

    @JsonView(Views.Public.class)
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"cartProducts.product"})
    private Cart cart;
}
