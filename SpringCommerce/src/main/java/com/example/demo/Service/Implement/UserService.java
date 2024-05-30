package com.example.demo.Service.Implement;

import com.example.demo.Model.Cart;
import com.example.demo.Model.CartProduct;
import com.example.demo.Model.Product;
import com.example.demo.Model.User;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.InterfaceUserService;
import com.example.demo.Utilities.UpdateUserInfoRequest;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService extends BaseService<User, UserRepository> implements InterfaceUserService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public boolean addProduct(UUID userId, UUID productId, int quantity) throws NoSuchElementException {
        Optional<User> userOptional = repository.findById(userId);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            boolean result = cartService.addProductToCart(user.getCart().getId(), productId, quantity);
            if(!result){
                return false;
            }
            repository.save(user);
            return true;
        } else {
            throw new NoSuchElementException("userId: " + userId + "is not exists");
        }
    }

    public User findByUserName(String username)
    {
        return  repository.findByUsername(username).orElse(null);
    }

    public boolean removeProduct(UUID userId, UUID productId) throws NoSuchElementException {
        Optional<User> userOptional = repository.findById(userId);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            cartService.removeProductFromCart(user.getCart().getId(), productId);
            repository.save(user);
            return true;
        } else {
            throw new NoSuchElementException("userId: " + userId + "is not exists");
        }
    }

    @Override
    public User insert(User newUser) throws NotImplementedException, NoSuchElementException {
        Optional<User> userOptional = repository.findByUsername(newUser.getUsername());
        if(userOptional.isPresent()) {
            throw new NotImplementedException("username was exits");
        }
        newUser.setId(null);
        if(newUser.getCartId() == null) {
            Cart cart = cartService.insert();
            newUser.setCartId(cart.getId());
        } else {
            Optional<Cart> cartOptional = cartRepository.findById(newUser.getCartId());
            if(cartOptional.isEmpty()) {
                throw new NoSuchElementException("Cannot find cart have id: " + newUser.getCartId());
            }
        }
        return repository.save(newUser);
    }

    @Override
    public boolean updateInfo(UUID userId, UpdateUserInfoRequest newUser) throws NotImplementedException, NoSuchElementException {
        Optional<User> userOptional = repository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new NoSuchElementException("User have id: " + userId + " is not exist");
        }
        User user = userOptional.get();
        String nickname = newUser.getNickname();
        String password = newUser.getPassword();
        String phone = newUser.getPhone();
        String email = newUser.getEmail();
        String linkImage = newUser.getLinkImage();
        if(isValidString(nickname)) {
            user.setNickname(nickname);
        }
        if(isValidString(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        if(isValidString(phone)) {
            user.setPhone(phone);
        }
        if(isValidString(email)) {
            user.setEmail(email);
        }
        if(isValidString(linkImage)) {
            user.setLinkImage(linkImage);
        }
        repository.save(user);
        return true;
    }

    private static boolean isValidString(String s) {
        if(s == null) {
            return false;
        }
        return !s.isEmpty();
    }
}
