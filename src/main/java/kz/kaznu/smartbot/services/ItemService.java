package kz.kaznu.smartbot.services;

import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.enums.ItemType;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ItemService {

    Optional<Item> getItemById(Long id);
    Optional<Item> getItemByName(String name);
    Page<Item> getTopItemsByType(ItemType type, Integer page, Integer size) ;
    Page<Item> search(ItemType type , Integer price1, Integer price2, String brand, String color, Integer page, Integer size);
    Page<Item> search(String text, int i, int i1);
}

