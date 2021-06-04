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

	// messages that TeacherController receive from server (request) and sent to it.
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
		ArrayList<String> listOfFields;
		ArrayList<String> listOfCourses;
		ArrayList<Integer> listOfGrades;
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

		case "GetGradeList":
			listOfGrades = getGrades((String) request.getMsg());
			if (listOfGrades.isEmpty())
				listOfGrades = null;
			teacherMessage.setMsg(listOfGrades);
			result = teacherMessage;
			break;
		}
		return result;
	}

	/**
	 * This method return the gradesList of exams that teacher wrote.
	 *
	 * @param name the teacher's name.
	 * @return listOfGrades if found in dataBase else return null.
	 */
	public static ArrayList<Integer> getGrades(String name) {
		ArrayList<Integer> listOfGrades = new ArrayList<>();
		String sql = "SELECT grade FROM Exam AS e, ExamOfStudent AS es WHERE " +
		"e.fid = es.fid AND e.cid = es.cid AND e.eid = es.eid AND e.author = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				listOfGrades.add(rs.getInt("grade"));
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
		return listOfGrades;
	}

	/**
	 * This method return the CoursesList of a teacher from DB.
	 *
	 * @param UserName  the course name from client.
	 * @param FieldName the field name from client.
	 * @return listOfCourses {@link ArrayList} if found in dataBase else return
	 *         null.
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
		return listOfCourses;
	}

	/**
	 * This method return the fieldList of a teacher from DB.
	 *
	 * @param UserName from client.
	 * @return return listOfFields {@link ArrayList} if found in dataBase else
	 *         return null.
	 */
	public static ArrayList<String> getFieldList(String UserName) {
		ArrayList<String> listOfField = new ArrayList<>();
		String sql = "SELECT field FROM Teacher WHERE userName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (!listOfField.contains(rs.getString("field")))
					listOfField.add(rs.getString("field"));
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
		return listOfField;
	}

	/**
	 * This method get a string and parsing it in each space.
	 *
	 * @param msg The message received.
	 * @return return array of the words from the string.
	 */
	private static String[] parsingTheData(String msg) {
		String[] pArray = msg.split(" ");
		return pArray;
	}
}
// End of TeacherController class