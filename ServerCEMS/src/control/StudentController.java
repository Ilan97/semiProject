package control;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import logic.ExamOfStudent;
import logic.Message;

/**
 * This class execute all queries that related to Teacher object and handle with
 * all objects that connect to these queries.
 *
 * @author Ilan Meikler
 * @author Bat-El Gardin
 * @version May 2021
 */

public class StudentController {

	// Instance variables **********************************************

	/**
	 * The time that takes to the student to answer the exam.
	 */
	private static long startTime = 0;

	// messages that StudentController receive from server (request) and sent to it.
	private static Message request;
	private static Message result;
	// variables for execute queries and handle the results from DB.
	private static PreparedStatement pstmt;

	// Instance methods ************************************************

	/**
	 * This method handles all requests that comes in from the server.
	 *
	 * @param msg {@link Message} The request message from the server.
	 * @return result {@link Message} The result message for the server.
	 */
	public static Message handleRequest(Message msg) {
		// create the result Message instance
		boolean res;
		Message teacherMessage = new Message();
		request = msg;
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "SubmitExam":
			res = submitExam((ExamOfStudent) request.getMsg());
			teacherMessage.setMsg(res);
			result = teacherMessage;
			break;

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
		}
		return result;
	}

	/**
	 * This method stop the timer of student in the exam.
	 * 
	 * @return difference between current time and start time.
	 */
	public static double stopTimer() {
		return (System.currentTimeMillis() - startTime) / 60000.0;
	}

	/**
	 * This method start the timer of student in the exam.
	 * 
	 * @return true (always).
	 */
	public static boolean startTimer() {
		startTime = System.currentTimeMillis();
		return true;
	}

	/**
	 * This method save student's finished exam in DB.
	 *
	 * @param file {@link ExamOfStudent} The exam to be upload by student.
	 * @return true if saved, else return false.
	 */
	public static boolean submitExam(ExamOfStudent exam) {
		InputStream inputStream = null;
		// get exam's id
		String examFullID = ExamController.getExamID(exam.getCode());
		String[] examIDcomponents = parsingTheExamId(examFullID);
		String type = ExamController.getExamType(examFullID);
		// data from parsingTheExamId method
		String fieldID = examIDcomponents[0];
		String courseID = examIDcomponents[1];
		String examID = examIDcomponents[2];
		// save details in db
		if (exam.getContent() != null)
			inputStream = new ByteArrayInputStream(exam.getContent());
		String sql = "INSERT INTO examOfStudent VALUES (?,?,?,?,?,?,?,?)";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, exam.getStudentName());
			pstmt.setString(2, fieldID);
			pstmt.setString(3, courseID);
			pstmt.setString(4, examID);
			pstmt.setDouble(5, exam.getRealTimeDuration());
			pstmt.setInt(6, exam.getGrade());
			pstmt.setString(8, exam.getAnswers());
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
			DBconnector.display("Exam saved in DB");
		} catch (SQLException e) {
			DBconnector.display("fail to save exam in DB");
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
}
//End of StudentController class