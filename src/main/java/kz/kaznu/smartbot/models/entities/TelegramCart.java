package kz.kaznu.smartbot.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "telegram_cart")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelegramCart extends BaseEntity{

    @ManyToOne
    private Item item;

    @ManyToOne
    private TelegramSession session;
}
