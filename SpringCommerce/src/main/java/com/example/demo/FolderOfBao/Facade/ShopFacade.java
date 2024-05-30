package com.example.demo.FolderOfBao.Facade;
import com.example.demo.Service.Implement.CartService;
import com.example.demo.Service.Implement.JavaMailService;
import com.example.demo.Service.Implement.OrderService;
import com.example.demo.Service.Implement.UserService;
import com.example.demo.Model.*;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ShopFacade {

    private static ShopFacade _instance;
    private UserService userService;
    private JavaMailService javaMailService;
    private CartService cartService;
    private OrderService orderService;
    private ShopFacade() {
        userService = new UserService();
        javaMailService = new JavaMailService();
        cartService = new CartService();
        orderService = new OrderService();
    }

    public static ShopFacade getInstance() {
        if (_instance == null)
            _instance = new ShopFacade();
        return _instance;
    }

    public void buyProductCashOnDelivery(UUID userId, UUID productId, int quantity, UUID cartId ,String email, Order newOrder, OrderStatus orderStatus) {
        cartService.addProductToCart(userId, productId, quantity);
        cartService.getTotalAmount(cartId);
        orderService.create(newOrder);
        orderService.updateStatus(cartId, orderStatus.PROCESSING );
        javaMailService.sendMail(email, "Musical Shop", "You have placed your order successfully. Shipper will deliver the goods to you soon. Thanks!" );
    }
}
