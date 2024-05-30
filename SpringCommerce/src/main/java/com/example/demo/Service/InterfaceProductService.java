package com.example.demo.Service;

import com.example.demo.Model.Product;
import com.example.demo.Repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public interface InterfaceProductService extends InterfaceBaseService<Product, ProductRepository>{
    public boolean updateProduct(UUID productId, Product newProduct) throws IOException;
    public boolean deleteProduct(UUID id) throws IOException;

    public String insertProduct(Product newProduct) throws IOException;
    public Page<Product> filter(UUID categoryId, String keyword, String brand, String color, BigDecimal minPrice, BigDecimal maxPrice, boolean deleted, int pageIndex, int pageSize, Sort sort);
    public Page<Product> getByCategoryId(UUID categoryId, int pageIndex, int pageSize);
    public Page<Product> getByUserId(UUID userId, int pageIndex, int pageSize);
    public Page<Product> getHighestSelled(int pageIndex, int pageSize);
    public Page<Product> search(String keyword, int pageIndex, int pageSize);
    public boolean enableSelling(UUID id);
}
