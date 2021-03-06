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

	// messages that FieldOfStudyController receive from server (request) and sent
	// to it.
	private static Message request;
	private static Message result;

	// Instance methods ************************************************

	/**
	 * This method handles all requests that comes in from the server.
	 *
	 * @param msg {@link Message} The request message from the server.
	 * @return result {@link Message} The result message for the server.
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
	 * @param FieldName from client.
	 * @return return Fid if Field found in dataBase else return null.
	 */
	public static String GetFid(String FieldName) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
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
	 * This method return the fname from DB.
	 *
	 * @param Fid the field id.
	 * @return return Fname if Field found in dataBase else return null.
	 */
	public static String GetFname(String Fid) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String Fname = null;
		String sql = "SELECT fname FROM fieldofstudy WHERE fid = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, Fid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Fname = rs.getString("fname");
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
		return Fname;
	}

	/**
	 * This method return the list of all fields from DB.
	 *
	 * @return fieldList {@link ArrayList} The list of all fields. if there isn't
	 *         any field - return null.
	 */
	public static ArrayList<String> getAllFields() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
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
		if (fieldList.isEmpty())
			fieldList = null;
		return fieldList;
	}
}
//End of FieldOfStudyController class