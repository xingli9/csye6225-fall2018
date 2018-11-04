package csye6225Web.repositories;

import csye6225Web.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
