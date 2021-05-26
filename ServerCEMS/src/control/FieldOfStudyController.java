package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import logic.Message;

/**
 * This class execute all queries that related to FieldOfStudy object and handle
 * with all objects that connect to these queries.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version May 2021
 */

public class FieldOfStudyController {

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
		ArrayList<String> AllFields;
		// create the result Message instance
		Message fielsMessage = new Message();
		request = msg;
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "GetFieldId":
			String Fid = GetFid((String) request.getMsg());
			fielsMessage.setMsg(Fid);
			result = fielsMessage;
			break;

		case "ShowAllFields":
			AllFields = getAllFields();
			fielsMessage.setMsg(AllFields);
			result = fielsMessage;
			break;
		}
		return result;
	}

	/**
	 * This method return the fid from DB.
	 *
	 * @param fieldName from client.
	 * @return return Fid if Field found in dataBase else return null
	 */
	public static String GetFid(String FieldName) {
		String Fid = null;
		String sql = "SELECT fid FROM fieldofstudy WHERE fname = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, FieldName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Fid = rs.getString("fid");
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
		return Fid;
	}

	/**
	 * This method return the list of all fields from DB.
	 *
	 * @return fieldList The list of all fields. if there isn't any field - return
	 *         null.
	 */
	public static ArrayList<String> getAllFields() {
		ArrayList<String> fieldList = new ArrayList<>();
		String sql = "SELECT fname FROM fieldofstudy";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				fieldList.add(rs.getString("fname"));
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
		return fieldList;
	}
}
//End of FieldOfStudyController class