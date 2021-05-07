package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class connect to the data base using JDBC: mysql.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version April 2021
 */

public class DBconnector {

	// Instance variables **********************************************

	/**
	 * static instance for Connection object. Will be create only once for each run.
	 * this is the connection between java code and DB. will be used for execute
	 * queries.
	 */
	public static Connection conn;

	// Instance methods ************************************************

	/**
	 * Connect the java with the DB.
	 */
	public static void connectToDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			display("Driver definition succeed");
		} catch (Exception ex) {
			display("Driver definition failed");
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/cemserver?serverTimezone=IST", "G3", "123456");
			display("SQL connection succeed");
		} catch (SQLException ex) {
			display("SQLException: " + ex.getMessage());
			display("SQLState: " + ex.getSQLState());
			display("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * This method displays a message into the console.
	 *
	 * @param message The string to be displayed.
	 */
	public static void display(String message) {
		System.out.println("> " + message);
	}
}
//End of DBconnector class