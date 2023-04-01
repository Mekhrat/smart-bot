package kz.kaznu.smartbot.services.impl;

import kz.kaznu.smartbot.models.entities.Favorite;
import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.TelegramUser;
import kz.kaznu.smartbot.models.entities.User;
import kz.kaznu.smartbot.repositories.FavoriteRepository;
import kz.kaznu.smartbot.services.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;

    @Override
    public List<Item> getFavoriteItemsByUser(String userEmail) {
        return favoriteRepository.getFavoritesByUser(userEmail);
    }

    @Override
    public Optional<Favorite> getFavoriteItemsByUserAndItem(String userEmail, Item item) {
        return favoriteRepository.getByUserAndItem(userEmail, item);
    }

    @Override
    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }

    @Override
    public boolean createNewFavorite(TelegramUser user, Item item) {
        Optional<Favorite> favorite = favoriteRepository.getByUserAndItem(user.getEmail(), item);
        if (favorite.isEmpty()) {
            Favorite newFavorite = Favorite.builder()
                    .item(item)
                    .userEmail(user.getEmail())
                    .build();
            save(newFavorite);
            return true;
        }
        return false;
    }
}
