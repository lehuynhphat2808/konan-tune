package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Service.Implement.AuthenticationService;
import com.example.demo.Service.Implement.OrderService;
import com.example.demo.Service.Implement.UserService;
import com.example.demo.Service.InterfaceOrderService;
import com.example.demo.Service.InterfaceUserService;
import com.example.demo.Utilities.RegisterRequest;
import com.example.demo.Utilities.Response;
import com.example.demo.Utilities.UpdateUserInfoRequest;
import com.example.demo.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {
    //Using Interface and DI allows to easily change/extend dependencies without modifying the modules in use.

    @Autowired
    InterfaceUserService userService;
    @Autowired
    InterfaceOrderService orderService;
    @Autowired
    AuthenticationService authenticationService;

    @JsonView(Views.Public.class)
    @GetMapping("/users")
    public ResponseEntity<?> getAll() {
        return Response.createResponse(HttpStatus.OK, "get all user successfully", userService.getAll());
    }

    @JsonView(Views.Public.class)
    @GetMapping("/users/page")
    public ResponseEntity<?> getPage(
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<User> userPage = userService.getPage(pageIndex, pageSize);
        return Response.createResponse(HttpStatus.OK, "get page successfully", userPage.getContent());
    }

    @JsonView(Views.Detail.class)
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            User user = userService.getById(id);
            return Response.createResponse(HttpStatus.OK, "get user by id successfully", user);
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
    @PutMapping("/users/addProductToCart")
    public ResponseEntity<?> addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId, @RequestParam int quantity) {
        try {
            boolean result = userService.addProduct(userId, productId, quantity);
            if(!result) {
                return Response.createResponse(HttpStatus.NOT_IMPLEMENTED, "quantity must > 0 for case add new product to cart", null);
            }
            return Response.createResponse(
                    HttpStatus.OK,
                    "add product in user's cart successfully",
                    true
            );
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.OK, e.getMessage(), null);
        }
    }

    @PutMapping("/users/removeProductFromCart")
    public ResponseEntity<?> removeProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        try {
            return Response.createResponse(
                    HttpStatus.OK,
                    "remove product in user's cart successfully",
                    userService.removeProduct(userId, productId)
            );
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.OK, e.getMessage(), null);
        }
    }

    @PostMapping("/user/insert")
    public ResponseEntity<?> insert(@RequestBody User user) throws NotImplementedException, NoSuchElementException {
        try {
            return Response.createResponse(HttpStatus.OK, "insert user successfully", userService.insert(user));
        } catch (Exception e) {
            return Response.createResponse(HttpStatus.NOT_IMPLEMENTED, e.getMessage(), null);
        }
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) throws NotImplementedException, NoSuchElementException {
        try {
            return Response.createResponse(HttpStatus.OK, "insert user successfully", authenticationService.register(registerRequest));
        } catch (Exception e) {
            return Response.createResponse(HttpStatus.NOT_IMPLEMENTED, e.getMessage(), null);
        }
    }

    @PostMapping("/user/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) throws NotImplementedException, NoSuchElementException {
        try {
            return Response.createResponse(HttpStatus.OK, "insert user successfully", authenticationService.forgotPassword(email));
        } catch (Exception e) {
            return Response.createResponse(HttpStatus.NOT_IMPLEMENTED, e.getMessage(), null);
        }
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) throws NoSuchElementException {
        try {
            userService.deleteById(id);
            return Response.createResponse(HttpStatus.OK,
                    "deleted user have id: " + id.toString(),
                    null);

        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.OK, e.getMessage(), null);
        }
    }

    @JsonView(Views.Detail.class)
    @PutMapping("/user/updateInfo/{id}")
    public ResponseEntity<?> updateInfo(@PathVariable UUID id, @RequestBody UpdateUserInfoRequest newUser) {
        try {
            return Response.createResponse(HttpStatus.OK, "update user successfully", userService.updateInfo(id, newUser));
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);

        }
    }
}
