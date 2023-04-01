package kz.kaznu.smartbot.services;

import kz.kaznu.smartbot.models.entities.TelegramSession;
import kz.kaznu.smartbot.models.entities.TelegramUser;

import java.util.Optional;

public interface TelegramSessionService {

    TelegramSession save(TelegramSession session);
    Optional<TelegramSession> getByEmail(String email);

    boolean checkSessionByUserEmail(TelegramUser user);

    void login(TelegramUser user);
    void logout(TelegramUser user);

}
