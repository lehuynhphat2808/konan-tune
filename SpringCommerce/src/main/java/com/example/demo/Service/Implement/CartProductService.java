package com.example.demo.Service.Implement;

import com.example.demo.Model.CartProduct;
import com.example.demo.Repository.CartProductRepository;
import com.example.demo.Service.InterfaceCartProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartProductService extends BaseService<CartProduct, CartProductRepository> implements InterfaceCartProductService {
    @Autowired
    CartProductRepository cartProductRepository;
    @Override
    public boolean updateIsSelected(UUID cartProductId, boolean isSelected) {
        Optional<CartProduct> cartProductOptional = repository.findById(cartProductId);
        if(cartProductOptional.isPresent()) {
            CartProduct cartProduct = cartProductOptional.get();
            cartProduct.setSelected(isSelected);
            repository.save(cartProduct);
            return true;
        } else {
            throw new NoSuchElementException("cartProduct have id: " + cartProductId + " is not exists");
        }
    }
}
