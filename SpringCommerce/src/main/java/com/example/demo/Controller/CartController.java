package com.example.demo.Controller;

import com.example.demo.Model.Cart;
import com.example.demo.Service.Implement.CartService;
import com.example.demo.Service.InterfaceCartService;
import com.example.demo.Utilities.PaginatedResponse;
import com.example.demo.Utilities.Response;
import com.example.demo.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class CartController {
    //Using Interface and DI allows to easily change/extend dependencies without modifying the modules in use.

    @Autowired
    InterfaceCartService cartService;

//    InterfaceCart cartServiceInterface = factoryService.getInteface('cartService');

    @GetMapping("/carts")
    public ResponseEntity<?> getAll() {
        return Response.createResponse(HttpStatus.OK, "get all cart successfully", cartService.getAll());
    }

    @JsonView(Views.CartProduct.class)
    @GetMapping("/carts/page")
    public ResponseEntity<?> getPage(
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<Cart> cartPage = cartService.getPage(pageIndex, pageSize);
        PaginatedResponse<Cart> paginatedResponse = new PaginatedResponse<>(
                cartPage.getContent(), cartPage.getTotalElements(), cartPage.getTotalPages()
        );
        return Response.createResponse(HttpStatus.OK, "get products successfully", paginatedResponse);

    }


    @JsonView(Views.CartProduct.class)
    @GetMapping("/cart/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            Cart cart = cartService.getById(id);
            return Response.createResponse(HttpStatus.OK, "get cart by id successfully", cart);
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    @GetMapping("/cart/getTotalAmount/{cartId}")
    public ResponseEntity<?> getTotalAmount(@PathVariable UUID cartId) {
        try {
            return Response.createResponse(HttpStatus.OK, "get totalAmount successfully", cartService.getTotalAmount(cartId));
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    @PostMapping("/cart/insert")
    public ResponseEntity<?> insert(@RequestBody (required = false) List<UUID> cartProductIdList) throws NoSuchElementException{
        if(cartProductIdList == null) {
            cartProductIdList = Collections.emptyList();
        }
        try {
            return Response.createResponse(HttpStatus.OK, "insert cart successfully", cartService.insert(cartProductIdList));
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    @PutMapping("/cart/addProductToCart")
    public ResponseEntity<?> addProductToCart(@RequestParam UUID cartId, @RequestParam UUID productId, @RequestParam int quantity) throws NoSuchElementException {
        try {
            boolean result = cartService.addProductToCart(cartId, productId, quantity);
            if(!result) {
                return Response.createResponse(HttpStatus.NOT_IMPLEMENTED, "quantity must > 0 for case add new product to cart", null);
            }
            return Response.createResponse(HttpStatus.OK, "insert cart successfully", true);
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_IMPLEMENTED, e.getMessage(), false);
        }
    }

    @DeleteMapping("/cart/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) throws NoSuchElementException, NotImplementedException {
        try {
            cartService.deleteById(id);
            return Response.createResponse(HttpStatus.OK,
                    "deleted cart have id: " + id.toString(),
                    null);

        } catch (Exception e) {
            return Response.createResponse(HttpStatus.NOT_IMPLEMENTED, e.getMessage(), null);
        }
    }

    @DeleteMapping("/cart/removeProductFromCart")
    public ResponseEntity<?> removeProductFromCart(@RequestParam UUID cartId, @RequestParam UUID productId) throws NoSuchElementException {
        try {
            cartService.removeProductFromCart(cartId, productId);
            return Response.createResponse(HttpStatus.OK,
                    "deleted product have id: " + productId + " from cart have id: " + cartId,
                    null);

        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.OK, e.getMessage(), null);
        }
    }

    @PutMapping("/cart/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Cart newCart) {
        try {
            return Response.createResponse(HttpStatus.OK, "update cart successfully", cartService.update(id, newCart));
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);

        }
    }



}
