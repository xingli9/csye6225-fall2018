package csye6225Web.daos;

import csye6225Web.models.User;

import java.util.Collection;

public interface UserDao {

    String register(String username, String password);

    void createUser(User user);


    Collection<User> findAllUsers();
}
