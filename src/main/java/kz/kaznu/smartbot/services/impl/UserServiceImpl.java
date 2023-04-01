package kz.kaznu.smartbot.services.impl;

import kz.kaznu.smartbot.models.entities.TelegramUser;
import kz.kaznu.smartbot.models.enums.Role;
import kz.kaznu.smartbot.models.enums.Status;
import kz.kaznu.smartbot.repositories.UserRepository;
import kz.kaznu.smartbot.services.TUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements TUserService {
    private final UserRepository userRepository;

    @Override
    public TelegramUser getOrCreateUser(Message message) {
        Optional<TelegramUser> optionalUser = userRepository.getUserByChatId(message.getChatId().toString());
        TelegramUser user = optionalUser.orElse(
                TelegramUser.builder()
                        .chatId(message.getChatId().toString())
                        .status(Status.EMPTY)
                        .role(Role.USER)
                        .build());
        user.setUsername(message.getChat().getUserName());
        user.setName(message.getChat().getFirstName());
        return userRepository.save(user);
    }

    @Override
    public TelegramUser save(TelegramUser user) {
        return userRepository.save(user);
    }
}
