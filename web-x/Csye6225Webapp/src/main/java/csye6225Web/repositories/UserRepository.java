package csye6225Web.repositories;

import csye6225Web.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User repository for CRUD operations.
 */
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
