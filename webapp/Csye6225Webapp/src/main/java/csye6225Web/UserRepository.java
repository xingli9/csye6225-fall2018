package csye6225Web;

import org.springframework.data.repository.CrudRepository;
import csye6225Web.User;



public interface UserRepository extends CrudRepository<User,Integer>{ }