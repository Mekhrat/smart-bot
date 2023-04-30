package kz.kaznu.smartbot.services;


import kz.kaznu.smartbot.models.dto.ConsumerInfo;
import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.Order;
import kz.kaznu.smartbot.models.entities.TelegramUser;
import kz.kaznu.smartbot.models.entities.User;
import org.aspectj.weaver.ast.Or;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order createNewOrder(TelegramUser user, ConsumerInfo consumerInfo, List<Item> items);
    List<Order> getOrdersByUserEmail(String email);
    void cancelOrderById(Long orderId);

    List<Order> getAllCourierOrders(String email);
    List<Order> getAllDeliveredCourierOrders(String email);

    Optional<Order> getById(Long id);

    Order save(Order order);
}
