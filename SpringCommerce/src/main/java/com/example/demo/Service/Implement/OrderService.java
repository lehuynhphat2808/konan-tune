package com.example.demo.Service.Implement;

import com.example.demo.Model.*;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.InterfaceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService extends BaseService<Order, OrderRepository> implements InterfaceOrderService{
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public boolean create(Order newOrder) throws NoSuchElementException {
        if(newOrder.getUserId() != null){
            Optional<User> userOptional= userRepository.findById(newOrder.getUserId());
            if(userOptional.isPresent()) {
                newOrder.setUser(userOptional.get());
            } else {
                throw new NoSuchElementException("User have id: " + newOrder.getUserId() + " is not exists");
            }
        }
        if(newOrder.getCartId() != null) {
            Optional<Cart> cartOptional= cartRepository.findById(newOrder.getCartId());
            if(cartOptional.isPresent()) {
                Cart cart = cartOptional.get();
                newOrder.setCart(cart);
                Set<CartProduct> cartProducts = cart.getCartProducts();
                for(CartProduct cartProduct: cartProducts) {
                    Product product = cartProduct.getProduct();
                    product.setSelled(product.getSelled() + cartProduct.getQuantity());
                    if(product.getQuantity() < cartProduct.getQuantity()) {
                        return false;
                    } else {
                        product.setQuantity(product.getQuantity() - cartProduct.getQuantity());
                    }
                    productRepository.save(product);
                }
            } else {
                throw new NoSuchElementException("Cart have id: " + newOrder.getCartId() + " is not exists");
            }
        } else {
            throw new NoSuchElementException("Cart id cannot be null");
        }

        repository.save(newOrder);
        return true;
    }

    @Override
    public boolean updateStatus(UUID id, OrderStatus newStatus) throws NoSuchElementException {
        Optional<Order> orderOptional = repository.findById(id);
        if(orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(newStatus);
            repository.save(order);
            if(order.getStatus() == OrderStatus.COMPLETED) {
                Set<CartProduct> cartProducts = order.getCart().getCartProducts();
                for(CartProduct cartProduct : cartProducts) {
                    User user = cartProduct.getProduct().getUser();
                    if(user != null) {
                        user.addCoin(cartProduct.getProduct().getPrice().multiply(new BigDecimal(cartProduct.getQuantity())));
                        userRepository.save(user);
                    }
                }
            }
            if(order.getStatus() == OrderStatus.CANCELED) {
                User user = order.getUser();
                user.addCoin(order.getCart().getTotalAmount());
                System.out.println(user);
                userRepository.save(user);


                if(order.getCartId() != null) {
                    Optional<Cart> cartOptional= cartRepository.findById(order.getCartId());
                    if(cartOptional.isPresent()) {
                        Cart cart = cartOptional.get();
                        Set<CartProduct> cartProducts = cart.getCartProducts();
                        for(CartProduct cartProduct: cartProducts) {
                            Product product = cartProduct.getProduct();
                            product.setSelled(product.getSelled() - cartProduct.getQuantity());
                            product.setQuantity(product.getQuantity() + cartProduct.getQuantity());
                            productRepository.save(product);
                        }
                    } else {
                        throw new NoSuchElementException("Cart have id: " + order.getCartId() + " is not exists");
                    }
                } else {
                    throw new NoSuchElementException("Cart id cannot be null");
                }
            }
            return true;
        } else {
            throw new NoSuchElementException("Order id: " + id + "is not exist");
        }
    }

    @Override
    public Page<Order> getByUserId(OrderStatus orderStatus, UUID userId, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findByUserIdAndStatus(userId, orderStatus, pageable);
    }

    @Override
    public Page<Order> getPageOrder(OrderStatus orderStatus, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findByStatus(orderStatus, pageable);
    }
}
