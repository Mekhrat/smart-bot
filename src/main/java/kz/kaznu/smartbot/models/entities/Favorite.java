package kz.kaznu.smartbot.models.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "favorites")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Favorite extends BaseEntity{

    private String userEmail;

    @ManyToOne
    private Item item;
}
