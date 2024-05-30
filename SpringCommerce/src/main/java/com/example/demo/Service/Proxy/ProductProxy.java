package com.example.demo.Service.Proxy;

import com.example.demo.Model.Product;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Service.Implement.BaseService;
import com.example.demo.Service.Implement.ProductService;
import com.example.demo.Service.InterfaceProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service

public class ProductProxy extends BaseService<Product, ProductRepository> implements InterfaceProductService {

    private final ProductService productService;
    public ProductProxy(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public boolean updateProduct(UUID productId, Product newProduct) throws NoSuchElementException, IOException{
        boolean res = false;
        res = productService.updateProduct(productId, newProduct);
        writeLog("UPDATE", "Product Id: " + productId);
        return res;
    }

    @Override
    public boolean deleteProduct(UUID id) throws IOException {
        boolean res = false;
        res = productService.deleteProduct(id);
        writeLog("DELETE", "Product Id: " + id);
        return res;
    }

    @Override
    public String insertProduct(Product newProduct) throws NoSuchElementException, IOException {
        String result = "";

        result = productService.insertProduct(newProduct);
        writeLog("INSERT", "Product Id: " + result);

        return result;
    }

    @Override
    public Page<Product> filter(UUID categoryId, String keyword, String brand, String color,
                                BigDecimal minPrice, BigDecimal maxPrice, boolean deleted,
                                int pageIndex, int pageSize, Sort sort) {
        return productService.filter(categoryId, keyword, brand, color, minPrice,
                maxPrice, deleted, pageIndex, pageSize, sort);
    }

    @Override
    public Page<Product> getByCategoryId(UUID categoryId, int pageIndex, int pageSize) {
        return productService.getByCategoryId(categoryId, pageIndex, pageSize);
    }

    @Override
    public Page<Product> getByUserId(UUID userId, int pageIndex, int pageSize) {
        return productService.getByUserId(userId, pageIndex, pageSize);
    }

    @Override
    public Page<Product> getHighestSelled(int pageIndex, int pageSize) {
        return productService.getHighestSelled(pageIndex, pageSize);
    }

    @Override
    public Page<Product> search(String keyword, int pageIndex, int pageSize) {
        return productService.search(keyword, pageIndex, pageSize);
    }

    @Override
    public boolean enableSelling(UUID id) {
        return productService.enableSelling(id);
    }

    private void writeLog(String action, String result) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        FileWriter fw = new FileWriter("./my_log/product_log.txt", true);
        fw.write(action + " product at: " + formatter.format(LocalDateTime.now()) + "\n" + result + "\n--------------------\n");
        fw.close();
    }
}
