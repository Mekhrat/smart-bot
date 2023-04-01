package kz.kaznu.smartbot.models.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "photos")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Photo extends BaseEntity{
    private String photoName;

    private boolean main;
    @ManyToOne
    private Item item;
}
