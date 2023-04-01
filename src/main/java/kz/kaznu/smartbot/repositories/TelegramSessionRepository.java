package kz.kaznu.smartbot.repositories;

import kz.kaznu.smartbot.models.entities.TelegramSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface TelegramSessionRepository extends JpaRepository<TelegramSession, Long> {

    Optional<TelegramSession> getTelegramSessionByEmail(String email);

    Optional<TelegramSession> getFirstByEmailOrderByIdDesc(String email);


}
