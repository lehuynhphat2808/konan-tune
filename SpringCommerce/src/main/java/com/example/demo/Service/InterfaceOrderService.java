package com.example.demo.Service;

import com.example.demo.Model.Cart;
import com.example.demo.Model.Order;
import com.example.demo.Model.OrderStatus;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.OrderRepository;
import org.springframework.data.domain.Page;

import java.util.NoSuchElementException;
import java.util.UUID;

public interface InterfaceOrderService extends InterfaceBaseService<Order, OrderRepository>{
    public boolean create(Order newOrder);
    public boolean updateStatus(UUID id, OrderStatus newStatus);
    public Page<Order> getByUserId(OrderStatus orderStatus, UUID userId, int pageIndex, int pageSize);
    public Page<Order> getPageOrder(OrderStatus orderStatus, int pageIndex, int pageSize);
}
