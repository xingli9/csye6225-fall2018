package csye6225Web.daos;

import csye6225Web.MyConnection;
import csye6225Web.models.Transaction;
import csye6225Web.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserImpl implements UserDao {

    private static UserImpl instance = null;

    private UserImpl() {
    }

    public static UserImpl getInstance() {
        if (instance == null)
            instance = new UserImpl();
        return instance;
    }


    String FIND_ALL_USERS = "SELECT * FROM user";
    String FIND_TRANSACTIONS_BY_USERID = "SELECT * FROM transaction_table WHERE user_id=?";
    String CREATE_USER = "INSERT INTO user(userid, username, password, active) VALUES(?,?,?,?)";




    @Override
    public String register(String username, String password) {
        String rst = "";

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            System.out.println("Missing username or password, please verify.");
        }

        //System.out.println(username + password);
        Collection<User> users = this.findAllUsers();
        for (User a:users) {
            if (!a.getUsername().equals(username)) {
                continue;
            } else if (BCrypt.checkpw(password, a.getPassword())) {
                System.out.println("Login successful!");
                return a.getUserid();
            } else {
                System.out.println("Password is incorrect!");
                //rst = "Password is incorrect!";
                return rst;
            }
        }
        System.out.println("User name does not exist!");
        //rst = "User name does not exist!";
        return rst;
    }

    @Override
    public void createUser(User user) {
        Connection connection = null;
        PreparedStatement preStatement = null;

        try {
            connection = MyConnection.getConnection();
            preStatement = connection.prepareStatement(CREATE_USER);
            preStatement.setString(1,user.getUserid());
            preStatement.setString(2,user.getUsername());
            preStatement.setString(3, user.getPassword());
            preStatement.setBoolean(4,user.isActive());
            preStatement.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public Collection<User> findAllUsers() {

        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet result = null;
        List<User> users = new ArrayList<User>();
        try {
            connection = MyConnection.getConnection();
            preStatement = connection.prepareStatement(FIND_ALL_USERS);
            result = preStatement.executeQuery();
            while (result.next()) {
                String userid = result.getString("userid");
                String username = result.getString("username");
                String password = result.getString("password");
                boolean active = result.getBoolean("active");
                User user = new User(username, password, active, userid);

                preStatement = connection.prepareStatement(FIND_TRANSACTIONS_BY_USERID);
                preStatement.setString(1, userid);
                ResultSet result1 = preStatement.executeQuery();
                List<Transaction> transactions = new ArrayList<Transaction>();
                while (result1.next()) {
                    String id = result1.getString("id");
                    String description = result1.getString("description");
                    String merchant = result1.getString("merchant");
                    String amount = result1.getString("amount");
                    String date = result1.getString("date");
                    String category = result1.getString("category");
                    Transaction transaction = new Transaction(id, description, merchant, amount, date, category, userid);
                    transactions.add(transaction);
                }
                //user.setTransactions(transactions);
                users.add(user);
            }

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return users;
    }
}
