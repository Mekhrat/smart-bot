package kz.kaznu.smartbot.models.entities;

import kz.kaznu.smartbot.models.enums.Brand;
import kz.kaznu.smartbot.models.enums.ItemType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "items")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Item extends BaseEntity{
    private String name;
    private String fullName;
    private Integer newPrice;
    private Integer lastPrice;

    @Column(name = "description", length = 3000)
    private String description;

    private Integer quantity;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Enumerated(EnumType.STRING)
    private Brand brand;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemParam> itemParams;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Photo> photos;
}




