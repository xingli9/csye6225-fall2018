package csye6225Web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

	private static MyConnection instance = null;

	private MyConnection() {
	}

	public static MyConnection getInstance() {
		if (instance == null)
			instance = new MyConnection();
		return instance;
	}

	private static final String DRIVER = "org.mariadb.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/csye6225";
	private static final String USER = "root";
	private static final String PASSWORD = "666666";

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
    	Class.forName(DRIVER);
    	return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	public static void closeConnection(Connection conn) {
   	 try {
   		 conn.close();
   	 } catch (SQLException e) {
   		 e.printStackTrace();
   	 }
	}

}
