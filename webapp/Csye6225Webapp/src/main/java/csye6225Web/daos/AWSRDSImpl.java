package csye6225Web.daos;

import csye6225Web.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AWSRDSImpl implements AWSRDSDao {

    private static AWSRDSImpl instance = null;

    private AWSRDSImpl() {
    }
    public static AWSRDSImpl getInstance() {
        if (instance == null)
            instance = new AWSRDSImpl();
        return instance;
    }
    String FIND_ALL_TABLES = "SHOW TABLES";
    String DROP_TABLE_USER = "DROP TABLE user;";
    String DROP_TABLE_TRANSLATION = "DROP TABLE transaction_table;";
    String DROP_TABLE_REPOSIT = "DROP TABLE receipts;";
    String CREATE_USER = "CREATE TABLE `user` (\n" +
            "  `userid` varchar(100) NOT NULL,\n" +
            "  `username` varchar(100) NOT NULL,\n" +
            "  `password` varchar(100) NOT NULL,\n" +
            "  `active` int(11) NOT NULL,\n" +
            "  PRIMARY KEY (`userid`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
    String CREATE_TRANSACTION_TABLE = "CREATE TABLE `transaction_table` (\n" +
            "  `id` varchar(100) NOT NULL,\n" +
            "  `description` varchar(100) NOT NULL,\n" +
            "  `merchant` varchar(100) NOT NULL,\n" +
            "  `amount` varchar(100) NOT NULL,\n" +
            "  `date` varchar(100) NOT NULL,\n" +
            "  `category` varchar(100) NOT NULL,\n" +
            "  `user_id` varchar(45) NOT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  KEY `user_id_idx` (`user_id`),\n" +
            "  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

    String CREATE_RECEIPTS = "CREATE TABLE `receipts` (\n" +
            "  `id` varchar(100) NOT NULL,\n" +
            "  `url` varchar(100) DEFAULT NULL,\n" +
            "  `transaction_id` varchar(100) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  KEY `transaction_id_idx` (`transaction_id`),\n" +
            "  CONSTRAINT `transaction_id` FOREIGN KEY (`transaction_id`) REFERENCES `transaction_table` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n";


    @Override
    public void setupDatabase() {
        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet result = null;
        try {
            connection = MyConnection.getConnection();
            preStatement = connection.prepareStatement(FIND_ALL_TABLES);
            result = preStatement.executeQuery();
            Set<String> tables = new HashSet<>();

            while (result.next()) {

                String rst = result.getString("Tables_in_csye6225");
                tables.add(rst);
            }

            if (tables.contains("user")) {
                preStatement = connection.prepareStatement(DROP_TABLE_USER);
                preStatement.execute();
            }
            preStatement = connection.prepareStatement(CREATE_USER);
            preStatement.executeUpdate();
            System.out.println("Setup user successful!");

            if (tables.contains("transaction_table")) {
                preStatement = connection.prepareStatement(DROP_TABLE_TRANSLATION);
                preStatement.executeUpdate();
            }
                preStatement = connection.prepareStatement(CREATE_TRANSACTION_TABLE);
                preStatement.executeUpdate();
                System.out.println("Setup transaction successful!");

            if (tables.contains("receipts")) {
                preStatement = connection.prepareStatement(DROP_TABLE_REPOSIT);
                preStatement.executeUpdate();
            }
            preStatement = connection.prepareStatement(CREATE_RECEIPTS);
            preStatement.executeUpdate();
            System.out.println("Setup receipt successful!");



            System.out.println("Setup database successful!");

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
    }
}
