package control;

import java.sql.PreparedStatement;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import logic.Exam;
import logic.ExamType;
import logic.Message;
import logic.Question;
import logic.UserType;

/**
 * This class execute all queries that related to Exam object and handle with
 * all objects that connect to these queries.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version April 2021
 */

public class ExamController {

	// Instance variables **********************************************

	/**
	 * messages that ExamController receive from server (request) and sent to it
	 * (result).
	 */
	private static Message request;
	private static Message result;
	/**
	 * all components of the entire exam identification number.
	 */
	private static String fieldID;
	private static String courseID;
	private static String examID;
	/**
	 * variables for execute queries and handle the results from DB.
	 */
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	// private static PreparedStatement pstmt;

	// Instance methods ************************************************

	/**
	 * This method handles all requests that comes in from the server.
	 *
	 * @param msg The request message from the server.
	 * @return result The result message for the server.
	 */
	public static Message handleRequest(Message msg) {
		Message examMessage = new Message();
		request = msg;
		ArrayList<Exam> listOfExams;
		String[] FieldName_CourseName;
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "GetEid":
			FieldName_CourseName = parsingTheData((String) request.getMsg());
			int Qid = GetEid(FieldName_CourseName[0], FieldName_CourseName[1]);
			examMessage.setMsg(Qid);
			result = examMessage;
			break;
			
		case "SaveExam":
			boolean SaveStatus = saveExam((Exam) request.getMsg());
			examMessage.setMsg(SaveStatus);
			result = examMessage;
			break;
			
		case "viewTableExam":
			// receive data from DB and save it in HashMap
			HashMap<String, String> resultMap;
			resultMap = viewTableExamQuery((String) request.getMsg());
			// create an Exam instance
			Exam exam = new Exam();
			// check if exam exists in DB
			if ((!resultMap.containsKey("fieldID")) || (!resultMap.containsKey("courseID"))
					|| (!resultMap.containsKey("examID")))
				exam.setExamID("Error");
			// exam exists
			else {
				// set the Exam instance
				exam.setFid(resultMap.get("fieldID"));
				exam.setFname(resultMap.get("fieldName"));
				exam.setCid(resultMap.get("courseID"));
				exam.setCname(resultMap.get("courseName"));
				exam.setEid(resultMap.get("examID"));
				exam.setExamID(exam.getFid() + exam.getCid() + exam.getEid());
				exam.setDuration(Integer.parseInt(resultMap.get("duration")));
				// set the questionsInExam HashMap (variable in Exam instance)
				HashMap<Question, Integer> examQuestions;
				examQuestions = exam.getQuestionsInExam();
				int index = 1;
				Question[] allQuestions = new Question[resultMap.size() - 6];
				while (resultMap.containsKey("question" + index)) {
					String[] tmp = parsingTheData(resultMap.get("question" + index));
					Question q = new Question();
					q.setFid(tmp[0]);
					q.setQid(tmp[1]);
					q.setCid(exam.getCid());
					q.setQuestionID(q.getFid() + q.getQid());

					allQuestions[index - 1] = q;
					examQuestions.put(allQuestions[index - 1], Integer.parseInt(tmp[2]));
					index++;
				}
			}
			// create the result Message instance
			examMessage.setMsg((Exam) exam);
			result = examMessage;
			break;

		case "updateExamDurationTime":
			// this msg contains the new duration time and exam's entire ID
			String[] updateDetails = parsingTheData((String) request.getMsg());
			int duration = Integer.parseInt(updateDetails[0]);
			String examID = updateDetails[1];
			updateExamDurationQuery(examID, duration);
			// create the result Message instance
			examMessage.setMsg(requestDurationTimeQuery(examID)); // return the new duration time
			result = examMessage;
			break;
		
		case "ShowExamList":
			FieldName_CourseName = parsingTheData((String) request.getMsg());
			listOfExams = getExams(FieldName_CourseName[0], FieldName_CourseName[1]);
			examMessage.setMsg(listOfExams);
			result = examMessage;
			break;
	} // end switch case
	return result;
	}
	
	/**
	 * This method responsible to get list of exams from DB .
	 *
	 * @param Fid The id of the field.
	 * @param Cid The id of the course.
	 * @return listOfexams The exams that belong to those field and course.
	 *         If there is no such exams, return null.
	 */
	public static ArrayList<Exam> getExams(String Fname, String Cname) {
		ArrayList<Exam> listOfExams = new ArrayList<>();
		// get the data from the relevant controllers
		String Fid = FieldOfStudyController.GetFid(Fname);
		String Cid = CourseController.GetCid(Cname);
		// execute query
		String sql = "SELECT * FROM Exam WHERE fid = ? AND cid = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, Fid);
			pstmt.setString(2, Cid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Exam e = new Exam();
				e.setFname(Fname);
				e.setCname(Cname);
				e.setEid(rs.getString("eid"));
				e.setFid(Fid);
				e.setCid(Cid);
				e.setExamID(e.getFid() + e.getCid() + e.getEid());
				e.setAuthor(rs.getString("author"));
				e.setDuration(rs.getInt("Duration"));
				e.setEtype(ExamType.valueOf(ExamType.class,rs.getString("etype")));
				listOfExams.add(e);
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
		return listOfExams;
	}

	/**
	 * This method responsible to save an exam in DB .
	 *
	 * @param exam The exam from client.
	 * @return boolean result if the save succeed.
	 */
	public static boolean saveExam(Exam exam) {
		String sql = "INSERT INTO Exam VALUES (?,?,?,?,?,?)";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, exam.getFid());
			pstmt.setString(2, exam.getCid());
			pstmt.setString(3, exam.getEid());
			pstmt.setString(4, exam.getAuthor());
			pstmt.setInt(5, exam.getDuration());
			// set the exam type
			pstmt.setString(6, exam.getEtype().name());
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
		UpdateEid(exam.getFname(), exam.getCname(), exam.getEid());
		return true;
	}

	/**
	 * This method responsible to update eid in DB .
	 *
	 * @param fieldName,courseName,Eid from client.
	 */
	private static void UpdateEid(String fieldName, String courseName, String Eid) {
		String query = "UPDATE eidtable SET eid = ? WHERE fieldName = ? AND courseName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(Eid));
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
	 * This method return the eid from DB.
	 *
	 * @param fieldName,courseName from client.
	 * @return return eid if found in dataBase else return -1
	 */
	public static int GetEid(String FieldName, String CourseName) {
		int Eid = -1;
		String sql = "SELECT eid FROM eidtable WHERE fieldName = ? AND CourseName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, FieldName);
			pstmt.setString(2, CourseName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Eid = rs.getInt("eid");
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
		return Eid;
	}

	/**
	 * This method execute query for watch exam table, by specific ID.
	 *
	 * @param examKey The entire exam ID.
	 * @return examDataMap The whole exam table data.
	 */
	public static HashMap<String, String> viewTableExamQuery(String examKey) {
		HashMap<String, String> examDataMap = new HashMap<>();
		String[] examIDcomponents = parsingTheExamId(examKey);
		// data from parsingTheExamId method
		fieldID = examIDcomponents[0];
		courseID = examIDcomponents[1];
		examID = examIDcomponents[2];
		/**
		 * Execute query for exam's field name. Table FieldOfStudy. In case exam isn't
		 * exists in DB, the map wont contain field's name and id.
		 */
		String fieldQuery = "SELECT fname FROM FieldOfStudy WHERE fid = " + fieldID;
		try {
			stmt = DBconnector.conn.createStatement();
			rs = stmt.executeQuery(fieldQuery);
			while (rs.next()) {
				examDataMap.put("fieldID", fieldID);
				examDataMap.put("fieldName", rs.getString("fname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}
		}
		/**
		 * Execute query for exam's course name. Table Course. In case exam isn't exists
		 * in DB, the map wont contain course's name and id.
		 */
		String courseQuery = "SELECT cname FROM Course WHERE cid = " + courseID;
		try {
			stmt = DBconnector.conn.createStatement();
			rs = stmt.executeQuery(courseQuery);
			while (rs.next()) {
				examDataMap.put("courseID", courseID);
				examDataMap.put("courseName", rs.getString("cname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}
		}
		/**
		 * Execute query for exam's details. Join between tables Exam & QuestionInExam.
		 */
		String examQuery = "SELECT e.duration, qe.qid, qe.score "
				+ "FROM Exam e JOIN QuestionInExam qe using(fid, cid, eid)" + " WHERE fid = " + fieldID + " AND "
				+ "cid = " + courseID + " AND " + "eid = " + examID;
		try {
			stmt = DBconnector.conn.createStatement();
			rs = stmt.executeQuery(examQuery);
			int index = 1;
			while (rs.next()) {
				examDataMap.put("examID", examID);
				examDataMap.put("duration", "" + rs.getInt("duration"));
				// put exam questions into the result map
				String questionID = rs.getString("qid");
				int questionScore = rs.getInt("score");
				examDataMap.put("question" + index, fieldID + " " + questionID + " " + questionScore);
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}
		}
		return examDataMap;
	}

	/**
	 * This method update the exam table in 'duration' column, by specific ID.
	 *
	 * @param examKey     The entire exam ID.
	 * @param newDuration For the new exam duration time.
	 */
	public static void updateExamDurationQuery(String examKey, int newDuration) {
		String[] examIDcomponents = parsingTheExamId(examKey);
		// data from parsingTheExamId method
		fieldID = examIDcomponents[0];
		courseID = examIDcomponents[1];
		examID = examIDcomponents[2];
		/**
		 * Execute query for update the exam's duration time. Table Exam.
		 */
		String query = "UPDATE Exam SET duration = " + newDuration + " WHERE fid = " + fieldID + " AND " + "cid = "
				+ courseID + " AND " + "eid = " + examID;
		try {
			stmt = DBconnector.conn.createStatement();
			stmt.executeUpdate(query);
			display("DB: New exam duration time was updated");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * This method execute query for get exam's duration time, by specific ID.
	 *
	 * @param examKey The entire exam ID.
	 * @return duration The exam's duration time.
	 */
	public static int requestDurationTimeQuery(String examKey) {
		String[] examIDcomponents = parsingTheExamId(examKey);
		// data from parsingTheExamId method
		fieldID = examIDcomponents[0];
		courseID = examIDcomponents[1];
		examID = examIDcomponents[2];
		int duration = 0;
		/**
		 * Execute query for get the content of 'Duration' column. Table Exam.
		 */
		String query = "SELECT duration FROM Exam WHERE fid = " + fieldID + " AND " + "cid = " + courseID + " AND "
				+ "eid = " + examID;
		try {
			stmt = DBconnector.conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next())
				duration = rs.getInt("duration");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}
		}
		return duration;
	}

	/**
	 * This method parsing the data received from msg.
	 * 
	 * @param msg The message received.
	 * @return pArray The parsed data.
	 */
	private static String[] parsingTheData(String msg) {
		String[] pArray = msg.split(" ");
		return pArray;
	}

	/**
	 * This method parsing the exam's entire id.
	 * 
	 * @param examId The exam's id.
	 * @return pArray The parsed id.
	 */
	private static String[] parsingTheExamId(String examId) {
		String[] pArray = new String[3];
		pArray[0] = examId.substring(0, 2);
		pArray[1] = examId.substring(2, 4);
		pArray[2] = examId.substring(4, 6);
		return pArray;
	}

	/**
	 * This method displays a message into the console.
	 *
	 * @param message The string to be displayed.
	 */
	public static void display(String message) {
		System.out.println("> " + message);
	}
}
//End of ExamController class