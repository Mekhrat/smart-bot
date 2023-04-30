package kz.kaznu.smartbot.repositories;

import kz.kaznu.smartbot.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getRoleByRole(String role);
}
