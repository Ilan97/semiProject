package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import logic.Message;
import logic.Request;

/**
 * This class execute all queries that related to Teacher object and handle with
 * all objects that connect to these queries.
 *
 * @author Bat-El Gardin
 * @version June 2021
 */

public class PrincipalController {

	// Instance variables **********************************************

	// messages that PrincipalController receive from server (request) and sent to
	// it.
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
		// create the result Message instance
		Message principalMessage = new Message();
		request = msg;
		boolean res;
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "AddRequest":
			res = addNewRequest((Request) msg.getMsg());
			principalMessage.setMsg(res);
			result = principalMessage;
			break;

		case "DeleteRequest":
			res = deleteRequest((String) msg.getMsg());
			principalMessage.setMsg(res);
			result = principalMessage;
			break;

		case "CheckRequestExists":
			res = checkRequestExists((String) msg.getMsg());
			principalMessage.setMsg(res);
			result = principalMessage;
			break;

		case "GetAllRequests":
			ArrayList<Request> AllRequests;
			AllRequests = getAllRequests();
			principalMessage.setMsg(AllRequests);
			result = principalMessage;
			break;

		}
		return result;
	}

	/**
	 * This method insert request to Request table.
	 *
	 * @param request {@link Request} we want to insert.
	 * @return true if success, false otherwise.
	 */
	public static boolean addNewRequest(Request request) {
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO request VALUES (?,?,?,?)";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, request.getEcode());
			pstmt.setDouble(2, request.getNewDur());
			pstmt.setDouble(3, request.getOldDur());
			pstmt.setString(4, request.getExplanation());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
			return false;
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		return true;
	}

	/**
	 * This method insert remove request from Request table.
	 *
	 * @param code of exam we want to delete from this table.
	 * @return true if success, false otherwise.
	 */
	public static boolean deleteRequest(String code) {
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM request WHERE ecode = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
			return false;
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		return true;
	}

	/**
	 * This method check if there is already request for specific exam.
	 *
	 * @param code The code of the exam we want to check.
	 * @return true is request exists, false otherwise.
	 */
	public static boolean checkRequestExists(String code) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean res = false;
		String sql = "SELECT ecode FROM request WHERE ecode = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				res = true;
			}
		} catch (SQLException e) {
			return res;
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
		return res;
	}

	/**
	 * This method return the list of all requests from DB.
	 *
	 * @return requestsList {@link Request} The list of all requests. if not found
	 *         in dataBase return null.
	 */
	public static ArrayList<Request> getAllRequests() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Request> requestsList = new ArrayList<>();
		String sql = "SELECT * FROM request AS r, examToPerform As ep WHERE r.ecode = ep.ecode";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Request req = new Request();
				req.setEcode(rs.getString("ecode"));
				req.setNewDur(rs.getDouble("new_duration"));
				req.setOldDur(rs.getDouble("old_duration"));
				req.setExplanation(rs.getString("explanation"));
				req.setEdate(rs.getString("edate"));
				req.setExamID(ExamController.getExamID(req.getEcode()));
				req.setFname(FieldOfStudyController.GetFname(rs.getString("fid")));
				req.setCname(CourseController.GetCname(rs.getString("cid")));
				requestsList.add(req);
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
		if (requestsList.isEmpty())
			requestsList = null;
		return requestsList;
	}
}
//End of PrincipalController class