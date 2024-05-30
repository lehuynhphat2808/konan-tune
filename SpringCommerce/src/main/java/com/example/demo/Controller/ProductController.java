package com.example.demo.Controller;

import com.example.demo.FolderOfBao.Decorator.ProductServiceDecorator;
import com.example.demo.Model.Product;
import com.example.demo.Service.Implement.ProductService;
import com.example.demo.Service.InterfaceProductService;
import com.example.demo.Service.Proxy.ProductProxy;
import com.example.demo.Utilities.Response;
import com.example.demo.Utilities.PaginatedResponse;
import com.example.demo.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("/api")
public class ProductController {
    //Using Interface and DI allows to easily change/extend dependencies without modifying the modules in use.
    @Autowired
    ProductProxy productService =  new ProductProxy(new ProductService());

    @Autowired
    ProductServiceDecorator ProductServiceDecorator;

    @JsonView(Views.HaveCategoty.class)
    @GetMapping("/products")
    public ResponseEntity<?> getAll() {
        return Response.createResponse(HttpStatus.OK,
                "get all product successfully",
                productService.getAll());
    }


    @JsonView(Views.Public.class)
    @GetMapping("/products/page")
    public ResponseEntity<?> getPage(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<Product> products;
        if(categoryId != null) {
            products = ProductServiceDecorator.getByCategoryId(categoryId, pageIndex, pageSize);
        } else if (keyword != null) {
            products = ProductServiceDecorator.search(keyword, pageIndex, pageSize);
        } else {
            products = ProductServiceDecorator.getPage(pageIndex, pageSize);
        }
        PaginatedResponse<Product> paginatedResponse = new PaginatedResponse<>(
                products.getContent(), products.getTotalElements(), products.getTotalPages()
        );
        return Response.createResponse(HttpStatus.OK, "get products successfully", paginatedResponse);
    }

    @JsonView(Views.Public.class)
    @GetMapping("/products/best-selling")
    public ResponseEntity<?> getHighestSelled(
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<Product> products;
        products = productService.getHighestSelled(pageIndex, pageSize);
        PaginatedResponse<Product> paginatedResponse = new PaginatedResponse<>(
                products.getContent(), products.getTotalElements(), products.getTotalPages()
        );
        return Response.createResponse(HttpStatus.OK, "get products successfully", paginatedResponse);
    }


    private Sort createSortObject(String sortField) {
        if (sortField != null) {
            String[] parts = sortField.split("\\.");
            String fieldName = parts[0];
            String direction = parts.length > 1 ? parts[1] : "asc"; // Default to ascending if direction is not specified
            return Sort.by(Sort.Direction.fromString(direction), fieldName);
        }
        return Sort.unsorted();
    }

    @JsonView(Views.Public.class)
    @GetMapping("/products/page/filter")
    public ResponseEntity<?> getPageFilter(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String color,
            @RequestParam(defaultValue = "-1") BigDecimal minPrice,
            @RequestParam(defaultValue = "999999999999") BigDecimal maxPrice,
            @RequestParam(defaultValue = "false") boolean deleted,
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String sortField
    ) {
        Sort sort = createSortObject(sortField);

        Page<Product> products;
        products = productService.filter(categoryId, keyword, brand, color, minPrice, maxPrice, deleted, pageIndex, pageSize, sort);
        PaginatedResponse<Product> paginatedResponse = new PaginatedResponse<>(
                products.getContent(), products.getTotalElements(), products.getTotalPages()
        );
        return Response.createResponse(HttpStatus.OK, "get products successfully", paginatedResponse);
    }

    @JsonView(Views.Public.class)
    @GetMapping("/products/page/user-selling")
    public ResponseEntity<?> getPage(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<Product> products;
        products = productService.getByUserId(userId, pageIndex, pageSize);
        PaginatedResponse<Product> paginatedResponse = new PaginatedResponse<>(
                products.getContent(), products.getTotalElements(), products.getTotalPages()
        );
        return Response.createResponse(HttpStatus.OK, "get selling products of user successfully", paginatedResponse);
    }


    @JsonView(Views.Detail.class)
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) throws NoSuchElementException{
        try {
            return Response.createResponse(HttpStatus.OK,
                    "get product by id successfully",
                    productService.getById(id));
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    @PostMapping("/product/insert")
    public ResponseEntity<?> insert(
            @RequestBody Product newProduct
    ) throws NoSuchElementException {
        try {
            return Response.createResponse(
                    HttpStatus.OK,
                    "insert product successfully",
                    productService.insertProduct(newProduct)
            );
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) throws NoSuchElementException {
        try {
            productService.deleteProduct(id);
        } catch (Exception e) {
            return Response.createResponse(HttpStatus.NOT_IMPLEMENTED, e.getMessage(), null);
        }
        return Response.createResponse(HttpStatus.OK,
                "deleted product have id: " + id.toString(),
                true);

    }

    @PutMapping("/product/enableSelling/{id}")
    public ResponseEntity<?> enableSelling(@PathVariable UUID id) throws NoSuchElementException {
        try {
            return Response.createResponse(HttpStatus.OK, "enable product have id: " + id.toString(),
                    productService.enableSelling(id));
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_IMPLEMENTED, e.getMessage(), null);
        }

    }

    @PutMapping("/product/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody Product newProduct
    ) throws NoSuchElementException{
        try {
            return Response.createResponse(HttpStatus.OK, "update product successfully", productService.updateProduct(id, newProduct));
        } catch (Exception e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);

        }
    }

}
