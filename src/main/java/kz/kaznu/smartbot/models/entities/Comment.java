package kz.kaznu.smartbot.models.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "comments")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment extends BaseEntity{
    private String text;
    private LocalDateTime createdAt;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Item item;
}
