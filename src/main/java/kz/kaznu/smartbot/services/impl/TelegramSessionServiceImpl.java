package kz.kaznu.smartbot.services.impl;

import kz.kaznu.smartbot.models.entities.*;
import kz.kaznu.smartbot.models.enums.Role;
import kz.kaznu.smartbot.repositories.CartRepository;
import kz.kaznu.smartbot.repositories.CourierRepository;
import kz.kaznu.smartbot.repositories.RoleRepository;
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
    private final CourierRepository courierRepository;

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
            Optional<User> courier = courierRepository.getFirstByEmail(user.getEmail());
            courier.ifPresent(cour -> {
                boolean isCourier = cour.getRoles().stream().anyMatch(c -> c.getRole().equals("ROLE_COURIER"));
                if (isCourier)
                    user.setRole(Role.COURIER);
                else
                    user.setRole(Role.USER);
            });
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
            user.setRole(Role.USER);
        });
    }
}
