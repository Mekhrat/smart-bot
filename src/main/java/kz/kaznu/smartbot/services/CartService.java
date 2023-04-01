package kz.kaznu.smartbot.services;

import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.TelegramUser;

import java.util.List;

public interface CartService {

    boolean addItemToCardBySession(TelegramUser user, Long itemId);

    List<Item> getItemsInCartByEmail(String email);

    boolean deleteByUserAndItem(TelegramUser user, Long itemId);

    boolean deleteAllItemsByUser(TelegramUser user);
}
