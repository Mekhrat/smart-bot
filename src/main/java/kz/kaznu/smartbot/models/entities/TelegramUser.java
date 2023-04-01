package kz.kaznu.smartbot.models.entities;

import kz.kaznu.smartbot.models.enums.Role;
import kz.kaznu.smartbot.models.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "telegram_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelegramUser extends BaseEntity{

    private String chatId;
    private String name;
    private String username;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;
}
