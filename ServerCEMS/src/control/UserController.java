package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logic.Message;
import logic.User;
import logic.UserType;

/**
 * This class execute all queries that related to User object and handle with
 * all objects that connect to these queries.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version May 2021
 */

public class UserController {

	// Instance variables **********************************************

	/**
	 * messages that UserController receive from server (request) and sent to it
	 * (result).
	 **/
	private static Message request;
	private static Message result;

	/**
	 * variables for execute queries and handle the results from DB.
	 **/
	private static Statement stmt;
	private static ResultSet rs;
	private static PreparedStatement pstmt;

	// Instance methods ************************************************

	/**
	 * This method handles all requests that comes in from the server.
	 *
	 * @param msg The request message from the server.
	 * @return result The result message for the server.
	 */
	public static Message handleRequest(Message msg) {
		request = msg;
		Boolean statusOfUpdate;
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "isUserExists":
			// receive a complete User object
			User res = viewTableUserQuery((String) request.getMsg());
			// create the result Message instance
			Message userMessage = new Message();
			userMessage.setMsg(res);
			result = userMessage;
			break;

		case "updateConnectionStatus":
			boolean ConnectionStatus;
			// get the current user's connection status
			ConnectionStatus = getConnectionStatus((String) request.getMsg());
			// update the status
			statusOfUpdate = updateConnectionStatus((String) request.getMsg(), ConnectionStatus);
			// check if update success
			if (statusOfUpdate == true)
				display("updateConnectionStatus successfully");
			else
				display("updateConnectionStatus faild");
			// create the result Message instance
			Message updateStatusMessage = new Message();
			updateStatusMessage.setMsg(statusOfUpdate);
			result = updateStatusMessage;
			break;
		}
		return result;
	}

	/**
	 * This method execute query for watch User table, by specific userName and
	 * password.
	 *
	 * @param UserDetails The userName and password.
	 * @return user The complete User instance.
	 */
	public static User viewTableUserQuery(String UserDetails) {
		String[] User = parsingTheData(UserDetails);
		// data from parsingTheData method
		String UserName = User[0];
		String Password = User[1];
		// the new instance
		User user = new User();
		// the query
		String sql = "SELECT * FROM users WHERE userName = ? AND upassword = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			pstmt.setString(2, Password);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user.setLogedIN(rs.getBoolean("isLogedIn"));
				user.setEmail(rs.getString("email"));
				user.setFirstName(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				user.setUid(rs.getString("uid"));
				user.setUpassword(rs.getString("upassword"));
				user.setUsername(rs.getString("userName"));
				// set the user's role
				switch (rs.getString("urole")) {
				case "student":
					user.setUserType(UserType.STUDENT);
					break;
				case "teacher":
					user.setUserType(UserType.TEACHER);
					break;
				case "principal":
					user.setUserType(UserType.PRINCIPAL);
					break;
				}
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				pstmt.close();
			} catch (Exception e) {
			}
		}
		// user dosen't exist
		return null;
	}

	/**
	 * This method execute query for get user's connection status, by specific
	 * userName.
	 *
	 * @param userName The userName.
	 * @return boolean value The status.
	 */
	private static boolean getConnectionStatus(String userName) {
		String Query = "SELECT isLogedIn FROM users WHERE userName = ?";
		boolean currStatus = false;
		try {
			pstmt = DBconnector.conn.prepareStatement(Query);
			pstmt.setString(1, userName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				currStatus = rs.getBoolean("isLogedIn");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				pstmt.close();
			} catch (Exception e) {
			}
		}
		return currStatus;
	}

	/**
	 * This method execute query for update user's connection status, by specific
	 * userName.
	 *
	 * @param userName The userName.
	 * @param status   The connection status of user.
	 * @return boolean value The successes/fail of the update.
	 */
	private static boolean updateConnectionStatus(String userName, boolean status) {
		String query;
		boolean StatusAfterUpdate = status;
		query = "UPDATE users SET isLogedIn = ? WHERE userName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(query);
			pstmt.setBoolean(1, !status);
			pstmt.setString(2, userName);
			pstmt.executeUpdate();
			// get the updated status
			StatusAfterUpdate = getConnectionStatus(userName);
			// check if updated successfully
			if (StatusAfterUpdate != status)
				return true;
		} catch (SQLException e) {
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
			}
		}
		// update was fail
		return false;
	}

	/**
	 * This method parsing the data received from msg.
	 * 
	 * @param msg The message received.
	 * @return pArray The parsed data.
	 */
	private static String[] parsingTheData(String msg) {
		String[] pArray = msg.split(" ");
		return pArray;
	}

	/**
	 * This method displays a message into the console.
	 *
	 * @param message The string to be displayed.
	 */
	public static void display(String message) {
		System.out.println("> " + message);
	}
}// End of UserController class