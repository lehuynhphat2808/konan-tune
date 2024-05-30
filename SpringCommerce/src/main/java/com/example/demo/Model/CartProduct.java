package com.example.demo.Model;

import com.example.demo.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

import java.util.Random;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "cart_product")
public class CartProduct extends BaseModel{
    @ManyToOne
    @JoinColumn(name = "cart_id")
//    @JsonIgnoreProperties("cartProducts")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Cart cart;

    @Column(name = "product_id")
    private UUID productId;

    @ManyToOne
    @JoinColumn(name = "product_id",  referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"category", "categoryId"})
    @EqualsAndHashCode.Exclude
    @JsonView(Views.Public.class)
    private Product product;

    @JoinColumn(name = "quantity")
    @JsonView(Views.Public.class)
    @EqualsAndHashCode.Exclude
    private int quantity;

    @JsonView(Views.Public.class)
    @JoinColumn(name = "selected")
    @EqualsAndHashCode.Exclude
    private boolean selected;


    @Override
    public String toString() {
        return "CartProduct{" +
                ", id=" + id +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    public CartProduct(Cart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.selected = false;
    }

    public CartProduct(Cart cart, UUID productId, int quantity) {
        this.cart = cart;
        this.productId = productId;
        this.quantity = quantity;
        this.selected = false;
    }
}
