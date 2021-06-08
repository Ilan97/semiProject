package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

	// messages that QuestionController receive from server (request) and sent to
	// it.
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
		// create the result Message instance
		Message questionMessage = new Message();
		request = msg;
		ArrayList<Question> listOfQuestions;
		String[] FieldName_CourseName;
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "GetQid":
			Question q = ((Question) request.getMsg());
			int Qid = GetQid(q.getFieldName(), q.getCourseName());
			questionMessage.setMsg(Qid);
			result = questionMessage;
			break;

		case "SaveQuestion":
			boolean SaveStatus = saveQuestion((Question) request.getMsg());
			questionMessage.setMsg(SaveStatus);
			result = questionMessage;
			break;

		case "ShowQuestionList":
			FieldName_CourseName = parsingTheData((String) request.getMsg());
			listOfQuestions = getQuestions(FieldName_CourseName[0], FieldName_CourseName[1]);
			questionMessage.setMsg(listOfQuestions);
			result = questionMessage;
			break;
		}
		
		return result;
	}

	/**
	 * This method responsible to get list of questions from DB .
	 *
	 * @param Fname The name of the field.
	 * @param Cname The name of the course.
	 * @return listOfQuestions {@link ArrayList} The questions that belong to those
	 *         field and course. If there no questions, return null.
	 */
	public static ArrayList<Question> getQuestions(String Fname, String Cname) {
		ArrayList<Question> listOfQuestions = new ArrayList<>();
		// get the data from the relevant controllers
		String Fid = FieldOfStudyController.GetFid(Fname);
		String Cid = CourseController.GetCid(Cname);
		// execute query
		String sql = "SELECT * FROM Question WHERE fid = ? AND cid = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, Fid);
			pstmt.setString(2, Cid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Question q = new Question();
				q.setFieldName(Fname);
				q.setCourseName(Cname);
				q.setQid(rs.getString("qid"));
				q.setFid(Fid);
				q.setCid(Cid);
				q.setQuestionID(q.getFid() + q.getCid() + q.getQid());
				q.setAuthor(rs.getString("author"));
				q.setContent(rs.getString("content"));
				q.setInstructions(rs.getString("instructions"));
				q.setRightAnswer(rs.getString("rightAnswer"));
				q.setWrongAnswer1(rs.getString("wrongAnswer1"));
				q.setWrongAnswer2(rs.getString("wrongAnswer2"));
				q.setWrongAnswer3(rs.getString("wrongAnswer3"));
				listOfQuestions.add(q);
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
		return listOfQuestions;
	}

	/**
	 * This method responsible to save a question in DB .
	 *
	 * @param question {@link Question} from client.
	 * @return boolean result if the save succeed.
	 */
	public static boolean saveQuestion(Question q) {
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
			return false;
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		UpdateQid(q.getFieldName(), q.getCourseName(), q.getQid());
		int newQid = GetQid(q.getFieldName(), q.getCourseName());
		questionToCourse(q.getFid(), q.getCid(), String.format("%03d", newQid));
		return true;
	}

	/**
	 * This method responsible to update table questionInCourse in DB .
	 *
	 * @param Fid The field id from client.
	 * @param Cid The course id from client.
	 * @param Qid The question id from client.
	 */
	public static void questionToCourse(String Fid, String Cid, String Qid) {
		String sql = "INSERT INTO questionInCourse VALUES (?,?,?)";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, Fid);
			pstmt.setString(2, Cid);
			pstmt.setString(3, Qid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
	}

	/**
	 * This method responsible to update qid in DB .
	 *
	 * @param fieldName  The field name from client.
	 * @param courseName The course name from client.
	 * @param Qid        question id from client.
	 */
	public static void UpdateQid(String fieldName, String courseName, String Qid) {
		String query = "UPDATE qidtable SET qid = ? WHERE fieldName = ? AND courseName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(Qid));
			pstmt.setString(2, fieldName);
			pstmt.setString(3, courseName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
	}

	/**
	 * This method return the qid from DB.
	 *
	 * @param fieldName  The field name from client.
	 * @param courseName The course name from client.
	 * @return qid if found in dataBase else return -1.
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
		return Qid;
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
//End of QuestionController class