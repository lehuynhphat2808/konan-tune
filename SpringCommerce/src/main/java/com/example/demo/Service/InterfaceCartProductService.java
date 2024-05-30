package com.example.demo.Service;

import com.example.demo.Model.Cart;
import com.example.demo.Model.CartProduct;
import com.example.demo.Repository.CartProductRepository;
import com.example.demo.Repository.CartRepository;

import java.util.Map;
import java.util.UUID;

public interface InterfaceCartProductService extends InterfaceBaseService<CartProduct, CartProductRepository> {
    public boolean updateIsSelected(UUID cartProductId, boolean isSelected);

}
