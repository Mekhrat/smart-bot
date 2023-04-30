package kz.kaznu.smartbot.repositories;


import kz.kaznu.smartbot.models.entities.Order;
import kz.kaznu.smartbot.models.enums.OrderStatus;
import kz.kaznu.smartbot.models.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.consumerEmail = :email and o.status <> 'CANCELED'")
    List<Order> getAllOrdersByCourierEmail(String email);

    List<Order> findAllByCourierEmailAndStatus(String email, OrderStatus status);
    List<Order> findAllByCourierEmailAndStatusAndOrderDateAfter(String email, OrderStatus status, LocalDateTime date);
}
