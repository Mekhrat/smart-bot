package kz.kaznu.smartbot.repositories;

import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.TelegramCart;
import kz.kaznu.smartbot.models.entities.TelegramSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<TelegramCart, Long> {

    Optional<TelegramCart> getFirstByItemAndSession(Item item, TelegramSession session);

    @Query("select c.item from TelegramCart c where c.session = ?1")
    List<Item> findAllItemsBySession(TelegramSession session);

    List<TelegramCart> findAllBySession(TelegramSession session);
}
