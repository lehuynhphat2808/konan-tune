package com.example.demo.Service;

import com.example.demo.Model.Cart;
import com.example.demo.Model.CartProduct;
import com.example.demo.Repository.CartRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface InterfaceCartService extends InterfaceBaseService<Cart, CartRepository> {
    public boolean addProductToCart(UUID cartId, UUID productId, int quantity);
    public void removeProductFromCart(UUID cartId, UUID productId);
    public UUID insert(List<UUID> cartProductIdList);
    public Cart insert();
    public BigDecimal getTotalAmount(UUID cartId);
}
