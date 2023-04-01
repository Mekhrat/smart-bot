package kz.kaznu.smartbot.repositories;

import kz.kaznu.smartbot.models.entities.Favorite;
import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("select f.item from Favorite f where f.userEmail = ?1")
    List<Item> getFavoritesByUser(String userEmail);

    @Query("select f from Favorite f where f.userEmail = :userEmail and f.item = :item")
    Optional<Favorite> getByUserAndItem(String userEmail, Item item);
}
