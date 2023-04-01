package kz.kaznu.smartbot.models.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "telegram_session")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelegramSession extends BaseEntity{

    private String email;
    private boolean logout;
    private LocalDateTime lastLogin;
}
