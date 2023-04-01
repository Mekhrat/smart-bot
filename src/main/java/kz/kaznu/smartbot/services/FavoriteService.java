package kz.kaznu.smartbot.services;

import kz.kaznu.smartbot.models.entities.Favorite;
import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.TelegramUser;
import kz.kaznu.smartbot.models.entities.User;

import java.util.List;
import java.util.Optional;

public interface FavoriteService {

    List<Item> getFavoriteItemsByUser(String userEmail);
    Optional<Favorite> getFavoriteItemsByUserAndItem(String userEmail, Item item);
    Favorite save(Favorite favorite);
    void delete(Favorite favorite);

    boolean createNewFavorite(TelegramUser user, Item itemId);
}
