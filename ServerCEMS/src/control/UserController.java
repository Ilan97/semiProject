package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

	// messages that UserController receive from server (request) and sent to it.
	private static Message request;
	private static Message result;
	// variables for execute queries and handle the results from DB.
	private static PreparedStatement pstmt;
	private static ResultSet rs;

	// Instance methods ************************************************

	/**
	 * This method handles all requests that comes in from the server.
	 *
	 * @param msg {@link Message} The request message from the server.
	 * @return result {@link Message} The result message for the server.
	 */
	public static Message handleRequest(Message msg) {
		request = msg;
		Boolean statusOfUpdate;
		Message userMessage = new Message();
		ArrayList<String> resList;
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "isUserExists":
			// receive a complete User object
			User res = viewTableUserQuery((String) request.getMsg());
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
				DBconnector.display("updateConnectionStatus successfully");
			else
				DBconnector.display("updateConnectionStatus faild");
			userMessage.setMsg(statusOfUpdate);
			result = userMessage;
			break;

		case "initUsersStatus":
			if (changeStatusToFalse())
				DBconnector.display("initUsersStatus successfully");
			else
				DBconnector.display("initUsersStatus faild");

		case "ShowAllStudents":
			resList = getAllStudents();
			userMessage.setMsg(resList);
			result = userMessage;
			break;

		case "ShowAllTeachers":
			resList = getAllTeachers();
			userMessage.setMsg(resList);
			result = userMessage;
			break;

		} // end of switch case
		return result;
	}

	/**
	 * This method found the full name of user, by given userName.
	 *
	 * @param userName The userName.
	 * @return full name if user is found, null otherwise.
	 */
	public static String getName(String userName) {
		String Query = "SELECT firstName, lastName FROM users WHERE userName = ?";
		String fullName = null;
		try {
			pstmt = DBconnector.conn.prepareStatement(Query);
			pstmt.setString(1, userName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				fullName = rs.getString("firstName") + " " + rs.getString("lastName");
			}

		} catch (SQLException e) {
			return fullName;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		return fullName;
	}

	/**
	 * This method execute query for watch User table, by specific userName and
	 * password.
	 *
	 * @param UserDetails The userName and password.
	 * @return user {@link User} The complete User instance.
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
			DBconnector.printSQLException(e);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
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
	 * @return true if user is logged in, false otherwise.
	 */
	public static boolean getConnectionStatus(String userName) {
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
			DBconnector.printSQLException(e);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		return currStatus;
	}

	/**
	 * This method execute query for changing connection status to false (0) of all
	 * users that their status have been true (1).
	 * 
	 * @return true if the changing success for all users, false otherwise.
	 */
	public static boolean changeStatusToFalse() {
		// return all userNames that their status is true (log in)
		String Query = "SELECT userName FROM users WHERE isLogedIn = ?";
		// save the userNames
		ArrayList<String> userNames = new ArrayList<>();
		try {
			pstmt = DBconnector.conn.prepareStatement(Query);
			pstmt.setBoolean(1, true);
			rs = pstmt.executeQuery(); // the userNames
			while (rs.next()) {
				userNames.add(rs.getString("userName"));
			}
			for (String userName : userNames)
				if (!updateConnectionStatus(userName, true)) // fail to update
					return false;
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		return true;
	}

	/**
	 * This method execute query for update user's connection status, by specific
	 * userName.
	 *
	 * @param userName The userName.
	 * @param status   The connection status of user.
	 * @return true if update success, false otherwise.
	 */
	public static boolean updateConnectionStatus(String userName, boolean status) {
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
			DBconnector.printSQLException(e);
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		// update was fail
		return false;
	}

	/**
	 * This method return the list of all students from DB.
	 *
	 * @return studentList {@link ArrayList} The list of all students. if there
	 *         isn't any student, return null.
	 */
	public static ArrayList<String> getAllStudents() {
		ArrayList<String> studentList = new ArrayList<>();
		String sql = "SELECT firstName, lastName FROM users WHERE urole = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, "student");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				studentList.add(rs.getString("firstName") + " " + rs.getString("lastName"));
			}

		} catch (SQLException e) {
			DBconnector.printSQLException(e);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		if (studentList.isEmpty())
			studentList = null;
		return studentList;
	}

	/**
	 * This method return the list of all teachers from DB.
	 *
	 * @return teacherList {@link ArrayList} The list of all teachers. if there
	 *         isn't any student, return null.
	 */
	public static ArrayList<String> getAllTeachers() {
		ArrayList<String> teacherList = new ArrayList<>();
		String sql = "SELECT firstName, lastName FROM users WHERE urole = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, "teacher");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				teacherList.add(rs.getString("firstName") + " " + rs.getString("lastName"));
			}

		} catch (SQLException e) {
			DBconnector.printSQLException(e);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		if (teacherList.isEmpty())
			teacherList = null;
		return teacherList;
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
}// End of UserController class