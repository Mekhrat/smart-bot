package kz.kaznu.smartbot.repositories;

import kz.kaznu.smartbot.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface CourierRepository extends JpaRepository<User, Long > {

    Optional<User> getFirstByEmail(String email);
}
