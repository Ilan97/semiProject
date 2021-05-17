package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logic.Message;

/**
 * This class execute all queries that related to Course object and handle with
 * all objects that connect to these queries.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version May 2021
 */

public class CourseController {
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
		ArrayList<String> listOfCoursesIds;
		ArrayList<String> listOfCourses = new ArrayList<>();
		ArrayList<String> AllCourses;
		// create the result Message instance
		Message courseMessage = new Message();
		request = msg;
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "GetCourseId":
			String Cid = GetCid((String) request.getMsg());
			courseMessage.setMsg(Cid);
			result = courseMessage;
			break;

		case "ShowCourseList":
			listOfCoursesIds = getCoursesIdList((String) request.getMsg());
			for (String cid : listOfCoursesIds) {
				if (cid != null)
					listOfCourses.add(GetCname(cid));
			}
			courseMessage.setMsg(listOfCourses);
			result = courseMessage;
			break;

		case "ShowAllCourses":
			AllCourses = getAllCourses();
			courseMessage.setMsg(AllCourses);
			result = courseMessage;
			break;
		}
		return result;
	}

	/**
	 * This method return the cid from DB.
	 *
	 * @param CourseName from client.
	 * @return return Cid if Course found in dataBase else return null
	 */
	public static String GetCid(String CourseName) {
		String Cid = null;
		String sql = "SELECT cid FROM course WHERE cname = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, CourseName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Cid = rs.getString("cid");
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
		return Cid;
	}

	/**
	 * This method return the course name from DB.
	 *
	 * @param cid The course id.
	 * @return cname if Course found in dataBase else return null
	 */
	public static String GetCname(String Cid) {
		String Cname = null;
		String sql = "SELECT cname FROM course WHERE cid = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, Cid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Cname = rs.getString("cname");
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
		return Cname;
	}

	/**
	 * This method return list of courses id's that belong to specific course.
	 *
	 * @param fid The field id.
	 * @return return list of courses id's if found in dataBase else return null
	 */
	public static ArrayList<String> getCoursesIdList(String fid) {
		ArrayList<String> listOfCoursesIds = new ArrayList<>();
		String sql = "SELECT cid FROM courseinfield WHERE fid = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, fid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				listOfCoursesIds.add(rs.getString("cid"));
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
		return listOfCoursesIds;
	}

	/**
	 * This method return the list of all courses from DB.
	 *
	 * @return courseList The list of all courses. if there isn't any course, return
	 *         null.
	 */
	public static ArrayList<String> getAllCourses() {
		ArrayList<String> courseList = new ArrayList<>();
		String sql = "SELECT cname FROM course";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				courseList.add(rs.getString("cname"));
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
		return courseList;
	}
}
//End of CourseController class