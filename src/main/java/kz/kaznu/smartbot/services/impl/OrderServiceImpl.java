package kz.kaznu.smartbot.services.impl;

import kz.kaznu.smartbot.models.dto.ConsumerInfo;
import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.Order;
import kz.kaznu.smartbot.models.entities.TelegramUser;
import kz.kaznu.smartbot.models.entities.User;
import kz.kaznu.smartbot.models.enums.OrderStatus;
import kz.kaznu.smartbot.repositories.OrderRepository;
import kz.kaznu.smartbot.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public Order createNewOrder(TelegramUser user, ConsumerInfo consumerInfo, List<Item> items) {
        Integer total = items.stream().map(Item::getNewPrice).reduce(Integer::sum).orElse(0);
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .consumerEmail(user.getEmail())
                .consumerIndex(consumerInfo.getIndex())
                .consumerName(consumerInfo.getName())
                .consumerPhone(consumerInfo.getPhone())
                .deliveryAddress(consumerInfo.getAddress())
                .status(OrderStatus.IN_PROCESSING)
                .items(items)
                .total(total)
                .build();
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUserEmail(String email) {
        return orderRepository.getAllOrdersByUserEmail(email);
    }

    @Override
    public void cancelOrderById(Long orderId) {
        Optional<Order> opOrder = orderRepository.findById(orderId);
        if (opOrder.isPresent()) {
            Order order = opOrder.get();
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
        }
    }
}
