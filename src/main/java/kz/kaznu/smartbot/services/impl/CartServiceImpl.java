package kz.kaznu.smartbot.services.impl;

import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.TelegramCart;
import kz.kaznu.smartbot.models.entities.TelegramSession;
import kz.kaznu.smartbot.models.entities.TelegramUser;
import kz.kaznu.smartbot.repositories.CartRepository;
import kz.kaznu.smartbot.repositories.TelegramSessionRepository;
import kz.kaznu.smartbot.services.CartService;
import kz.kaznu.smartbot.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final TelegramSessionRepository sessionRepository;
    private final ItemService itemService;
    private final CartRepository cartRepository;

    @Override
    public boolean addItemToCardBySession(TelegramUser user, Long itemId) {
        Optional<TelegramSession> opSession = sessionRepository.getFirstByEmailOrderByIdDesc(user.getEmail());
        Optional<Item> opItem = itemService.getItemById(itemId);

        if (opItem.isPresent() && opSession.isPresent()) {
            Optional<TelegramCart> opCart = cartRepository.getFirstByItemAndSession(opItem.get(), opSession.get());
            if (opCart.isEmpty()) {
                TelegramCart cart = TelegramCart.builder()
                        .item(opItem.get())
                        .session(opSession.get())
                        .build();
                cartRepository.save(cart);
                return true;
            }
        }
        return false;
    }


    @Override
    public List<Item> getItemsInCartByEmail(String email) {
        Optional<TelegramSession> session = sessionRepository.getFirstByEmailOrderByIdDesc(email);
        if (session.isPresent()) {
            return cartRepository.findAllItemsBySession(session.get());
        }
        return new ArrayList<>();
    }


    @Override
    public boolean deleteByUserAndItem(TelegramUser user, Long itemId) {
        Optional<TelegramSession> session = sessionRepository.getFirstByEmailOrderByIdDesc(user.getEmail());
        Optional<Item> item = itemService.getItemById(itemId);
        if (session.isPresent() && item.isPresent()) {
            Optional<TelegramCart> cart = cartRepository.getFirstByItemAndSession(item.get(), session.get());
            if (cart.isPresent()) {
                cartRepository.delete(cart.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteAllItemsByUser(TelegramUser user) {
        Optional<TelegramSession> session = sessionRepository.getFirstByEmailOrderByIdDesc(user.getEmail());
        if (session.isPresent()) {
            List<TelegramCart> allBySession = cartRepository.findAllBySession(session.get());
            cartRepository.deleteAll(allBySession);
            return true;
        }
        return false;
    }
}
