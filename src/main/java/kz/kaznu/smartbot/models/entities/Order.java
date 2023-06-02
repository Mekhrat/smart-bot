package kz.kaznu.smartbot.models.entities;


import kz.kaznu.smartbot.models.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "orders")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order extends BaseEntity{

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Item> items;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Integer total;
    private String consumerName;
    private String deliveryAddress;
    private String consumerEmail;
    private boolean isPaid;
    private String consumerPhone;
    private String consumerIndex;
    private String courierEmail;
}
