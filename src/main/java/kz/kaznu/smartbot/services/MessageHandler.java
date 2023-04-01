package kz.kaznu.smartbot.services;

import kz.kaznu.smartbot.models.entities.TelegramUser;
import kz.kaznu.smartbot.models.entities.User;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHandler {

    void handleMessage(Message message);
    void handleCallbackQuery(CallbackQuery message);
    void handleMessageText(TelegramUser user , String message);
    void handleCourierMessageText(TelegramUser user , String message);
}
