package kz.kaznu.smartbot.models.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity{
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private boolean isBlocked;
    private LocalDateTime birthday;
    private boolean activated;


    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
