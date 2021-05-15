package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import logic.Message;
import logic.Question;

/**
 * This class execute all queries that related to Question object and handle
 * with all objects that connect to these queries.
 *
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version May 2021
 */

public class QuestionController {

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
		// create the result Message instance
		Message questionMessage = new Message();
		request = msg;
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "GetQid":
			String[] FieldName_CourseName = parsingTheData((String) request.getMsg());
			int Qid = GetQid(FieldName_CourseName[0], FieldName_CourseName[1]);
			questionMessage.setMsg(Qid);
			result = questionMessage;
			break;

		case "SaveQuestion":
			boolean SaveStatus = SaveQuestion((Question) request.getMsg());
			questionMessage.setMsg(SaveStatus);
			result = questionMessage;
			break;
		}
		return result;
	}

	/**
	 * This method responsible to save a question in DB .
	 *
	 * @param question from client.
	 * @return boolean result if the save succeed.
	 */
	public static boolean SaveQuestion(Question q) {
		String sql = "INSERT INTO Question VALUES (?,?,?,?,?,?,?,?,?,?)";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, q.getFid());
			pstmt.setString(2, q.getCid());
			pstmt.setString(3, q.getQid());
			pstmt.setString(4, q.getAuthor());
			pstmt.setString(5, q.getContent());
			pstmt.setString(6, q.getInstructions());
			pstmt.setString(7, q.getRightAnswer());
			pstmt.setString(8, q.getWrongAnswer1());
			pstmt.setString(9, q.getWrongAnswer2());
			pstmt.setString(10, q.getWrongAnswer3());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
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
		UpdateQid(q.getFieldName(), q.getCourseName(), q.getQid());
		return true;
	}

	/**
	 * This method responsible to update qid in DB .
	 *
	 * @param fieldName,courseName,Qid from client.
	 */
	private static void UpdateQid(String fieldName, String courseName, String Qid) {
		String query = "UPDATE qidtable SET qid = ? WHERE fieldName = ? AND courseName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(Qid));
			pstmt.setString(2, fieldName);
			pstmt.setString(3, courseName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * This method return the qid from DB.
	 *
	 * @param fieldName,courseName from client.
	 * @return return qid if found in dataBase else return -1
	 */
	public static int GetQid(String FieldName, String CourseName) {
		int Qid = -1;
		String sql = "SELECT qid FROM qidtable WHERE fieldName = ? AND CourseName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, FieldName);
			pstmt.setString(2, CourseName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Qid = rs.getInt("qid");
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
		return Qid;
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
//End of QuestionController class