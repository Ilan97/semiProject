package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logic.Message;
import logic.Question;

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
		ArrayList<String> listOfFields;
		ArrayList<String> listOfCourses;
		// create the result Message instance
		Message userMessage = new Message();
		request = msg;
		
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "ShowFieldList":
			listOfFields = getFieldList((String)request.getMsg());
			userMessage.setMsg(listOfFields);
			result = userMessage;
			break;
		case "ShowCourseList":
			String[] TeacherDetails = parsingTheData((String)request.getMsg());
			listOfCourses = getCoursesList(TeacherDetails[0],TeacherDetails[1]);
			userMessage.setMsg(listOfCourses);
			result = userMessage;
			break;
			
		case "GetCourseId":
			String Cid = GetCid((String)request.getMsg());
			userMessage.setMsg(Cid);
			result = userMessage;
			break;
			
		case "GetFieldId":
			String Fid = GetFid((String)request.getMsg());
			userMessage.setMsg(Fid);
			result = userMessage;
			break;
			
		case "GetQid":
			String[] FieldName_CourseName = parsingTheData((String)request.getMsg());
			int Qid = GetQid(FieldName_CourseName[0], FieldName_CourseName[1]);
			userMessage.setMsg(Qid);
			result = userMessage;
			break;
			
		case "SaveQuestion":
			boolean SaveStatus = SaveQuestion((Question)request.getMsg());
			userMessage.setMsg(SaveStatus);
			result = userMessage;
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
		String sql ="INSERT INTO Question VALUES (?,?,?,?,?,?,?,?,?,?)"; 
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
			try {rs.close();} 
			catch (Exception e) {
			}
			try {pstmt.close();} 
			catch (Exception e) {
			}
		}
		UpdateQid(q.getFieldName() , q.getCourseName() , q.getQid());
		return true;
	}
	
	/**
	 * This method responsible to update  qid in DB .
	 *
	 * @param fieldName,courseName,Qid from client.
	 */			
	private static void UpdateQid(String fieldName, String courseName,String Qid) {
		String query = "UPDATE qidtable SET qid = ? WHERE fieldName = ? AND courseName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(query);
			pstmt.setInt(1,  Integer.parseInt(Qid));
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
		int Qid = -1 ;
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
			try {rs.close();} 
			catch (Exception e) {
			}
			try {pstmt.close();} 
			catch (Exception e) {
			}
		}
		return Qid;
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
				Fid = rs.getString("cid");
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {rs.close();} 
			catch (Exception e) {
			}
			try {pstmt.close();} 
			catch (Exception e) {
			}
		}
		return Fid;
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
			try {rs.close();} 
			catch (Exception e) {
			}
			try {pstmt.close();} 
			catch (Exception e) {
			}
		}
		return Cid;
	}	
	
		/**
		 * This method return the CoursesList of a teacher from DB.
		 *
		 * @param UserName,FieldName from client.
		 * @return return list of courses if  found in dataBase else return null
		 */	
	public static ArrayList<String> getCoursesList(String UserName, String FieldName) {
		
		ArrayList<String> listOfCourses = new ArrayList<>();
		String sql = "SELECT field FROM Teacher WHERE userName = ? AND field = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			pstmt.setString(2, FieldName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				listOfCourses.add(rs.getString("field"));
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {rs.close();} 
			catch (Exception e) {
			}
			try {pstmt.close();} 
			catch (Exception e) {
			}
		}
		return listOfCourses;
	}

	/**
	 * This method return the fieldList of a teacher from DB.
	 *
	 * @param UserName from client.
	 * @return return list of fields if  found in dataBase else return null
	 */	
	public static ArrayList<String> getFieldList(String UserName) {
		ArrayList<String> listOfField = new ArrayList<>();
		String sql = "SELECT field FROM Teacher WHERE userName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, UserName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				listOfField.add(rs.getString("field"));
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {rs.close();} 
			catch (Exception e) {
			}
			try {pstmt.close();} 
			catch (Exception e) {
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
