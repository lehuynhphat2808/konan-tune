package com.example.demo.Model;

import com.example.demo.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@Data
@Table(name = "cart")
public class Cart extends BaseModel {
    @JsonView(Views.Public.class)
    @Column(name = "totalAmount")
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JsonIgnoreProperties("cart")
    @EqualsAndHashCode.Exclude
    @JsonView(Views.CartProduct.class)
    private Set<CartProduct> cartProducts;

    public Cart() {
        totalAmount = BigDecimal.ZERO;
        cartProducts = new HashSet<>();
    }

    public BigDecimal calcTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for(CartProduct cartProduct : cartProducts) {
            if(cartProduct.isSelected()){
                total = total.add(cartProduct.getProduct().getPrice().multiply(new BigDecimal(cartProduct.getQuantity())));
            }
        }
        return total;
    }

    public CartProduct changeQuantityCartProduct(UUID productId, Integer quantity) {
        CartProduct cartProduct = findCardProductByProductId(productId);
        if(cartProduct != null) {
            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
            if(cartProduct.getQuantity() < 1) {
                cartProducts.remove(cartProduct);
            }
            return cartProduct;
        }
        return null;
    }

    public CartProduct findCardProductByProductId(UUID productId) {
        for(CartProduct cartProduct : cartProducts) {
            if(cartProduct.getProduct().getId().equals(productId)) {
                return cartProduct;
            }
        }
        return null;
    }

    public boolean containProduct(UUID productId) {
        for(CartProduct cartProduct : cartProducts) {
            if(cartProduct.getProduct().getId().equals(productId)) {
                return true;
            }
        }
        return false;
    }

    public void removeCartProduct(CartProduct cartProduct) {
        cartProducts.remove(cartProduct);
    }

    public void addCartProduct(CartProduct cartProduct) {
        cartProducts.add(cartProduct);
    }

    public BigDecimal getTotalAmount() {
        this.totalAmount = calcTotal();
        return this.totalAmount;
    }

    @Override
    public String toString() {
        return "Cart{" +
                ", id=" + id +
                "totalAmount=" + totalAmount +
                ", cartProducts=" + cartProducts +
                '}';
    }
}
