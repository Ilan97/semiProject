package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logic.Message;

/**
 * This class execute all queries that related to Teacher object and handle with
 * all objects that connect to these queries.
 *
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version May 2021
 */

public class TeacherController {

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
		ArrayList<String> listOfFields;
		ArrayList<String> listOfCourses;
		// create the result Message instance
		Message teacherMessage = new Message();
		request = msg;
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "ShowFieldList":
			listOfFields = getFieldList((String) request.getMsg());
			teacherMessage.setMsg(listOfFields);
			result = teacherMessage;
			break;
		case "ShowCourseList":
			String[] TeacherDetails = parsingTheData((String) request.getMsg());
			listOfCourses = getCoursesList(TeacherDetails[0], TeacherDetails[1]);
			teacherMessage.setMsg(listOfCourses);
			result = teacherMessage;
			break;
		}
		return result;
	}

	/**
	 * This method return the CoursesList of a teacher from DB.
	 *
	 * @param UserName,FieldName from client.
	 * @return return list of courses if found in dataBase else return null
	 */
	public static ArrayList<String> getCoursesList(String UserName, String FieldName) {

		ArrayList<String> listOfCourses = new ArrayList<>();
		String sql = "SELECT course FROM Teacher WHERE userName = ? AND field = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			pstmt.setString(2, FieldName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				listOfCourses.add(rs.getString("course"));
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
		return listOfCourses;
	}

	/**
	 * This method return the fieldList of a teacher from DB.
	 *
	 * @param UserName from client.
	 * @return return list of fields if found in dataBase else return null
	 */
	public static ArrayList<String> getFieldList(String UserName) {
		ArrayList<String> listOfField = new ArrayList<>();
		String sql = "SELECT field FROM Teacher WHERE userName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if(!listOfField.contains(rs.getString("field")))
					listOfField.add(rs.getString("field"));
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
		return listOfField;
	}

	/**
	 * This method get a string and parsing it in each space.
	 *
	 * @param string .
	 * @return return array of the words from the string.
	 */
	private static String[] parsingTheData(String msg) {
		String[] pArray = msg.split(" ");
		return pArray;
	}
}
// End of TeacherController class