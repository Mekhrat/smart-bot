package kz.kaznu.smartbot.repositories;


import kz.kaznu.smartbot.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.consumerEmail = :email and o.status <> 'CANCELED'")
    List<Order> getAllOrdersByUserEmail(String email);
}
