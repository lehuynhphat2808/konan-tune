package com.example.demo.Controller;


import com.example.demo.Service.InterfaceCartProductService;
import com.example.demo.Utilities.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CartProductController {
    //Using Interface and DI allows to easily change/extend dependencies without modifying the modules in use.

    final
    InterfaceCartProductService cartProductService;

    public CartProductController(InterfaceCartProductService cartProductService) {
        this.cartProductService = cartProductService;
    }

    @PutMapping("/cartProduct/updateIsSelected")
    public ResponseEntity<?> updateIsSelected(@RequestParam UUID cartProductId, @RequestParam boolean selected) throws NoSuchElementException {
        try {
            cartProductService.updateIsSelected(cartProductId, selected);
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_IMPLEMENTED, e.getMessage(), false);
        }
        return Response.createResponse(HttpStatus.OK, "update cartProduct isSelected State successfully", true);
    }
}
