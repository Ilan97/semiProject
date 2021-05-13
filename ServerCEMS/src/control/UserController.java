package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import logic.Exam;
import logic.Message;
import logic.User;
import logic.UserType;

public class UserController {

	/**
	 * messages that ExamController receive from server (request) and sent to it
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
		switch (request.getOperation())
		{
		case "isUserExists":
			User res = viewTableUserQuery((String)request.getMsg());
			Message userMessage = new Message();
			userMessage.setMsg(res);
			result = userMessage;
			break;
		 
		case "updateConnectionStatus":
			boolean ConnectionStatus;
			ConnectionStatus = getConnectionStatus((String)request.getMsg());
			if(ConnectionStatus)
				statusOfUpdate  = updateConnectionStatus((String)request.getMsg(),true);
			else
				statusOfUpdate = updateConnectionStatus((String)request.getMsg(),false);
		 
			if(statusOfUpdate == true)
				System.out.println("updateConnectionStatus successfully");
			else 
				System.out.println("updateConnectionStatus faild");
			Message updateStatusMessage = new Message();
			updateStatusMessage.setMsg(statusOfUpdate);
			result = updateStatusMessage;
			break;
		}
		return result;
		}
	
	private static boolean updateConnectionStatus(String userName, boolean Status ) {
		String query;
		boolean StatusAfterUpdate = Status;
		query = "UPDATE users SET isLogedIn = ? WHERE userName = ?";
		
		try {
			pstmt = DBconnector.conn.prepareStatement(query);
			//if (Status)
				pstmt.setBoolean(1, !Status);
			//else
				//pstmt.setBoolean(1, Status);
			pstmt.setString(2, userName);
			System.out.println(pstmt.executeUpdate());
			
 			System.out.println("after update query");
			
			StatusAfterUpdate = getConnectionStatus(userName);
			System.out.println("After while Rs");
	} catch (SQLException e) {e.printStackTrace();} 
	finally 
	{
		try {rs.close();} catch (Exception e) {}
		try {pstmt.close();} catch (Exception e) {}
		System.out.println("In finally");
	}
		if ( StatusAfterUpdate != Status )
			return true;
		return false;
		
	}



	/**
	 * This method execute query for watch exam table, by specific ID.
	 *
	 * @param examKey The entire exam ID.
	 * @return examDataMap The whole exam table data.
	 */
	public static User viewTableUserQuery(String UserDetails){
		String[] User = UserDetails.split(" ");
		// data from parsingTheExamId method
		
		String UserName = User[0];
		String Password = User[1];
		User user = new User();
		/**
		 * Execute query for exam's field name. Table FieldOfStudy. In case exam isn't
		 * exists in DB, the map wont contain field's name and id.
		 */
		String fieldQuery = "SELECT * FROM users WHERE userName = ? AND upassword = ?";
//		String fieldQuery = "SELECT userName,upassword FROM users WHERE userName = "
//		 + UserName+ " AND "+ "upassword = "+Password;
		try {
			pstmt = DBconnector.conn.prepareStatement(fieldQuery);
			pstmt.setString(1, UserName);
			pstmt.setString(2, Password);
			rs = pstmt.executeQuery();
		//	rs = stmt.executeQuery(fieldQuery);
			while (rs.next()) 
			{
					user.setLogedIN(rs.getBoolean("isLogedIn"));
					user.setEmail(rs.getString("email"));
					user.setFirstName(rs.getString("firstName"));
					user.setLastName(rs.getString("lastName"));
					user.setUid(rs.getString("uid"));
					user.setUpassword(rs.getString("upassword"));
					user.setUsername(rs.getString("userName"));
					
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
			
			
		}catch (SQLException e) {e.printStackTrace();} 
		finally 
		{
			try {rs.close();} catch (Exception e) {}
			try {pstmt.close();} catch (Exception e) {}
		}
		return null;
}
	
	private static boolean getConnectionStatus(String userName) {
		
		String Query = "SELECT isLogedIn FROM users WHERE userName = ?";
		boolean currStatus = false;
		try {
			pstmt = DBconnector.conn.prepareStatement(Query);
			pstmt.setString(1, userName);
			rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				currStatus = rs.getBoolean("isLogedIn")	;
			}
			
		}catch (SQLException e) {e.printStackTrace();} 
		finally 
		{
			try {rs.close();} catch (Exception e) {}
			try {pstmt.close();} catch (Exception e) {}
		}
		return currStatus;
	}
}