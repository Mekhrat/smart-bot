package kz.kaznu.smartbot.services.impl;

import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.TelegramCart;
import kz.kaznu.smartbot.models.entities.TelegramSession;
import kz.kaznu.smartbot.models.entities.TelegramUser;
import kz.kaznu.smartbot.repositories.CartRepository;
import kz.kaznu.smartbot.repositories.TelegramSessionRepository;
import kz.kaznu.smartbot.services.ItemService;
import kz.kaznu.smartbot.services.TelegramSessionService;
import kz.kaznu.smartbot.utils.BotUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TelegramSessionServiceImpl implements TelegramSessionService {

    private final TelegramSessionRepository sessionRepository;

    @Override
    public TelegramSession save(TelegramSession session) {
        return sessionRepository.save(session);
    }

    @Override
    public Optional<TelegramSession> getByEmail(String email) {
        return sessionRepository.getTelegramSessionByEmail(email);
    }

    @Override
    public boolean checkSessionByUserEmail(TelegramUser user) {
        Optional<TelegramSession> opSession = sessionRepository.getFirstByEmailOrderByIdDesc(user.getEmail());
        if (opSession.isEmpty())
            return false;

        TelegramSession session = opSession.get();
        return !session.isLogout() && BotUtils.checkLocalDateByHours(session.getLastLogin(), 2);
    }

    @Override
    public void login(TelegramUser user) {
        if (!checkSessionByUserEmail(user)) {
            TelegramSession session = TelegramSession.builder()
                    .email(user.getEmail())
                    .lastLogin(LocalDateTime.now())
                    .logout(false)
                    .build();
            sessionRepository.save(session);
        }
    }

    @Override
    public void logout(TelegramUser user) {
        Optional<TelegramSession> opSession = sessionRepository.getFirstByEmailOrderByIdDesc(user.getEmail());
        opSession.ifPresent(session ->  {
            session.setLogout(true);
            sessionRepository.save(session);
        });
    }
}
