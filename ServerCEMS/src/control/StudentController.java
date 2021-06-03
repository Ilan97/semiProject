package control;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import logic.ExamOfStudent;
import logic.Message;

/**
 * This class execute all queries that related to Teacher object and handle with
 * all objects that connect to these queries.
 *
 * @author Ilan Meikler
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @version May 2021
 */

public class StudentController {
	// Instance variables **********************************************

	/**
	 * The time that takes to the student to answer the exam.
	 */
	private static long startTime = 0;

	/**
	 * messages that UserController receive from server (request) and sent to it
	 * (result).
	 **/
	private static Message request;
	private static Message result;

	/**
	 * variables for execute queries and handle the results from DB.
	 **/
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
		boolean res;
		Message teacherMessage = new Message();
		Message studentMessage = new Message();
		request = msg;
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "SubmitExam":
			res = submitExam((ExamOfStudent) request.getMsg());
			teacherMessage.setMsg(res);
			result = teacherMessage;
			break;

		// to be continue
		case "StartTimer":
			res = startTimer();
			teacherMessage.setMsg(res);
			result = teacherMessage;
			break;

		case "StopTimer":
			double difference = stopTimer();
			teacherMessage.setMsg(difference);
			result = teacherMessage;
			break;
			
		case "ShowExamOfStudentList":
			String userName = (String) request.getMsg();
			ArrayList<ExamOfStudent> ExamsOfStudentList;
			ExamsOfStudentList = getExamsOfStudent(userName);
			studentMessage.setMsg(ExamsOfStudentList);
			result = studentMessage;
			break;
		}
		return result;
	}
	
	/**
	 * This method gets all the exams of student by userName .
	 */
	public static ArrayList<ExamOfStudent> getExamsOfStudent(String userName){
		ArrayList<ExamOfStudent> ExamsOfStudentList = new ArrayList<>();
		ResultSet rs1 = null;
		// execute query
		String sql = "SELECT * FROM examofstudent WHERE userName = ? AND teacher_check = 1";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			rs1 = pstmt.executeQuery();
			while (rs1.next()) {
				ExamOfStudent es = new ExamOfStudent();
				es.setUserName(userName);
				es.setFid(rs1.getString("fid"));
				es.setFname(FieldOfStudyController.GetFname(rs1.getString("fid")));
				es.setCid(rs1.getString("cid"));
				es.setCname(CourseController.GetCname(rs1.getString("cid")));
				es.setEid(rs1.getString("eid"));
				es.setGrade(rs1.getInt("grade"));
				es.setAnswers(rs1.getString("ans"));
				es.setCode(rs1.getString("exam_code"));
				es.setEdate(getExamDate(es.getCode()));
				ExamsOfStudentList.add(es);
			}
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
		} finally {
			try {
				rs1.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		return ExamsOfStudentList;
	}
	
	/**
	 * This method get the exam of student date.
	 */
	public static String getExamDate(String code){
		String date = new String();
		ResultSet newRs = null;
		String sql = "SELECT * FROM examtoperform WHERE ecode = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			newRs = pstmt.executeQuery();
			while (newRs.next()) {
				ExamOfStudent es = new ExamOfStudent();
				es.setEdate(newRs.getString("edate"));
				date = es.getEdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				newRs.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		return date;
	}

	/**
	 * This method stop the timer of student in the exam.
	 */
	public static double stopTimer() {
		return (System.currentTimeMillis() - startTime) / 60000.0;
	}

	/**
	 * This method start the timer of student in the exam.
	 */
	public static boolean startTimer() {
		// need to thing about the time limit
		startTime = System.currentTimeMillis();
		return true;
	}

	/**
	 * This method save student's finished exam in DB.
	 *
	 * @param file The exam to be upload by student.
	 * @return true if saved, else return false.
	 */
	public static boolean submitExam(ExamOfStudent exam) {
		InputStream inputStream = null;
		// get exam's id
		String examFullID = ExamController.getExamCode(exam.getCode());
		String[] examIDcomponents = parsingTheExamId(examFullID);
		String type = ExamController.getExamType(examFullID);
		// data from parsingTheExamId method
		String fieldID = examIDcomponents[0];
		String courseID = examIDcomponents[1];
		String examID = examIDcomponents[2];
		// save details in db
		if (exam.getContent() != null)
			inputStream = new ByteArrayInputStream(exam.getContent());
		String sql = "INSERT INTO examOfStudent VALUES (?,?,?,?,?,?,?,?,?,?)";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, exam.getStudentName());
			pstmt.setString(2, fieldID);
			pstmt.setString(3, courseID);
			pstmt.setString(4, examID);
			pstmt.setDouble(5, exam.getRealTimeDuration());
			pstmt.setInt(6, exam.getGrade());
			pstmt.setString(8, exam.getAnswers());
			pstmt.setBoolean(9, false);
			pstmt.setString(10, exam.getCode());
			switch (type) {
			case "computerized":
				byte[] empty = {};
				pstmt.setBlob(7, new ByteArrayInputStream(empty));
				break;
			case "manual":
				pstmt.setBlob(7, inputStream);
				break;
			}
			pstmt.executeUpdate();
			display("Exam saved in DB");
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
			display("fail to save exam in DB");
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
//End of StudentController class