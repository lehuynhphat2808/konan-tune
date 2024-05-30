package com.example.demo.Service.Implement;

import com.example.demo.Model.CartProduct;
import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Model.User;
import com.example.demo.Repository.CartProductRepository;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.InterfaceProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductService extends BaseService<Product, ProductRepository> implements InterfaceProductService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    private UserRepository userRepository;
    public Page<Product> getByCategoryId(UUID categoryId, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Product> getByUserId(UUID userId, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findByUserId(userId, pageable);    }

    public Page<Product> search(String keyword, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.search(keyword, pageable);
    }

    @Override
    public Page<Product> filter(UUID categoryId, String keyword, String brand, String color, BigDecimal minPrice,
                                BigDecimal maxPrice, boolean deleted, int pageIndex, int pageSize, Sort sort) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize,sort);
        return repository.filter(categoryId, keyword, brand, color, minPrice, maxPrice, deleted, pageable);
    }

    @Override
    public boolean updateProduct(UUID productId, Product newProduct) throws NoSuchElementException{
        Optional<Product> productOptional = repository.findById(productId);
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            newProduct.setId(productId);
            UUID categoryId = newProduct.getCategoryId();
            if(categoryId == null) {
                Category oldCategory = product.getCategory();
                if(oldCategory != null) {
                    newProduct.setCategoryId(product.getCategoryId());
                }
            } else {
                Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
                if(categoryOptional.isEmpty()) {
                    throw new NoSuchElementException("Category have id: " + categoryId + " is not exist");
                }
            }
            UUID userId = newProduct.getUserId();
            if(userId == null) {
                User oldUser = product.getUser();
                if(oldUser != null) {
                    newProduct.setUserId(product.getUserId());
                }
            } else {
                Optional<User> userOptional = userRepository.findById(userId);
                if(userOptional.isEmpty()) {
                    throw new NoSuchElementException("User have id: " + userId + " is not exist");
                }
            }
            repository.save(newProduct);
            return true;
        } else {
            throw new NoSuchElementException("Can't find product with id: " + productId.toString());
        }
    }

    @Override
    public String insertProduct(Product newProduct) throws NoSuchElementException {

        if(newProduct.getCategoryId() != null) {
            Optional<Category> categoryOptional = categoryRepository.findById(newProduct.getCategoryId());
            if(categoryOptional.isEmpty()){
                throw new NoSuchElementException("categoryId: " + newProduct.getCategoryId() + " is not exists");
            }
        }
        if(newProduct.getUserId() != null) {
            Optional<User> userOptional = userRepository.findById(newProduct.getUserId());
            if(userOptional.isEmpty()){
                throw new NoSuchElementException("userId: " + newProduct.getCategoryId() + " is not exists");
            }
        }
        Product product = repository.save(newProduct);
        return product.getId().toString();
    }

    @Override
    public boolean deleteProduct(UUID id) throws NoSuchElementException {
        Optional <Product> productOptional = repository.findById(id);
        if(productOptional.isPresent()) {
            Product product = productOptional.get();
            boolean isInCart = cartProductRepository.existsByProductId(id);
            if(isInCart) {
                product.setDeleted(true);
                repository.save(product);
            } else {
                repository.deleteById(id);
            }
            return true;
        } else {
            throw new NoSuchElementException("Can't found productId: " + id + " to delete");
        }
    }

    @Override
    public Page<Product> getPage(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    public Page<Product> getHighestSelled(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return repository.findHighestSelled(pageable);
    }

    @Override
    public boolean enableSelling(UUID id) throws NoSuchElementException {
        Optional <Product> productOptional = repository.findById(id);
        if(productOptional.isPresent()) {
            Product product = productOptional.get();
                product.setDeleted(!product.isDeleted());
                repository.save(product);
                return product.isDeleted();
        } else {
            throw new NoSuchElementException("Can't found productId: " + id + " to delete");
        }
    }

}
