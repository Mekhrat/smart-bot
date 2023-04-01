package kz.kaznu.smartbot.services;

import kz.kaznu.smartbot.models.entities.TelegramUser;
import kz.kaznu.smartbot.models.entities.User;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TUserService {

    TelegramUser getOrCreateUser(Message message);
    TelegramUser save(TelegramUser user);
}
