package com.example.demo.Controller;

import com.example.demo.Model.Order;
import com.example.demo.Model.OrderStatus;
import com.example.demo.Model.Product;
import com.example.demo.Service.Implement.OrderService;
import com.example.demo.Service.InterfaceOrderService;
import com.example.demo.Utilities.PaginatedResponse;
import com.example.demo.Utilities.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class OrderController {
    //Using Interface and DI allows to easily change/extend dependencies without modifying the modules in use.

    @Autowired
    InterfaceOrderService orderService;
    @GetMapping("/orders")
    public ResponseEntity<?> getAll() {
        return Response.createResponse(HttpStatus.OK, "get all order successfully", orderService.getAll());
    }

    @GetMapping("/orders/page")
    public ResponseEntity<?> getPage(
            @RequestParam OrderStatus orderStatus,
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize
    ) {

        Page<Order> orderPage = orderService.getPageOrder(orderStatus, pageIndex, pageSize);
        PaginatedResponse<Order> paginatedResponse = new PaginatedResponse<>(
                orderPage.getContent(), orderPage.getTotalElements(), orderPage.getTotalPages()
        );

        return Response.createResponse(HttpStatus.OK, "get page successfully", paginatedResponse);
    }

    @GetMapping("/orders/getByUserId")
    public ResponseEntity<?> getByUserId(
            @RequestParam UUID userId,
            @RequestParam OrderStatus orderStatus,
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<Order> orderPage = orderService.getByUserId(orderStatus, userId, pageIndex, pageSize);
        PaginatedResponse<Order> paginatedResponse = new PaginatedResponse<>(
                orderPage.getContent(), orderPage.getTotalElements(), orderPage.getTotalPages()
        );
        return Response.createResponse(HttpStatus.OK, "get page successfully", paginatedResponse);
    }


    @GetMapping("/order/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            Order order = orderService.getById(id);
            return Response.createResponse(HttpStatus.OK, "get order by id successfully", order);
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    @PostMapping("/order/insert")
    public ResponseEntity<?> insert(
            @RequestBody Order order
    ) throws NoSuchElementException {
        try {
            boolean result = orderService.create(order);
            if(result) {
                return Response.createResponse(
                        HttpStatus.OK,
                        "insert order successfully",
                        true
                );
            } else {
                return Response.createResponse(
                        HttpStatus.OK,
                        "insert order successfully",
                        false
                );
            }

        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

//    @DeleteMapping("/order/delete/{id}")
//    public ResponseEntity<?> delete(@PathVariable UUID id) throws NoSuchElementException {
//        try {
//            orderService.deleteById(id);
//            return Response.createResponse(HttpStatus.OK,
//                    "deleted order have id: " + id.toString(),
//                    null);
//
//        } catch (NoSuchElementException e) {
//            return Response.createResponse(HttpStatus.OK, e.getMessage(), null);
//        }
//    }

    @PutMapping("/order/updateStatus/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestParam OrderStatus newStatus) {
        try {
            return Response.createResponse(HttpStatus.OK, "update order status successfully", orderService.updateStatus(id, newStatus));
        } catch (NoSuchElementException e) {
            return Response.createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);

        }
    }
}
