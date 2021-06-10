package control;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import logic.ExamOfStudent;
import logic.ExamType;
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

	// messages that StudentController receive from server (request) and sent to it.
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
		boolean res;
		ArrayList<String> listOfNames;
		Message studentMessage = new Message();
		request = msg;
		// switch case is on the operations this controller ask to operate.
		switch (request.getOperation()) {
		case "SubmitExam":
			Message MyMessage = new Message();
			ExamOfStudent examOfStudent = (ExamOfStudent) request.getMsg();
			res = submitExam((ExamOfStudent) request.getMsg());
			studentMessage.setMsg(res);
			result = studentMessage;
			// Decrease action of countPerformers by -1 (as student submitted his exam)
			MyMessage.setMsg(examOfStudent.getCode());
			MyMessage.setControllerName("ExamController");
			MyMessage.setOperation("DecreaseExam");
			ExamController.handleRequest(MyMessage);
			break;

		case "StartTimer":
			res = startTimer();
			studentMessage.setMsg(res);
			result = studentMessage;
			break;

		case "StopTimer":
			double difference = stopTimer();
			studentMessage.setMsg(difference);
			result = studentMessage;
			break;

		case "GetUserName":
			String studentName = getUserName((String) request.getMsg());
			studentMessage.setMsg(studentName);
			result = studentMessage;
			break;

		case "UpdateExam":
			boolean isUpdate = examWasChecked((ExamOfStudent) request.getMsg());
			studentMessage.setMsg(isUpdate);
			result = studentMessage;
			break;

		case "GetExamOfStudent":
			ExamOfStudent ExamOfStudent;
			String[] data = parsingTheData((String) request.getMsg());
			ExamOfStudent = getExam(data[0], data[1]);
			studentMessage.setMsg(ExamOfStudent);
			result = studentMessage;
			break;

		case "ShowExamOfStudentList":
			String userName = (String) request.getMsg();
			ArrayList<ExamOfStudent> ExamsOfStudentList;
			ExamsOfStudentList = getExamsOfStudent(userName);
			studentMessage.setMsg(ExamsOfStudentList);
			result = studentMessage;
			break;

		case "GetGradeList":
			ArrayList<Integer> listOfGrades;
			listOfGrades = getGrades((String) request.getMsg());
			studentMessage.setMsg(listOfGrades);
			result = studentMessage;
			break;

		case "GetAllStudents":
			listOfNames = getNames((String) request.getMsg());
			studentMessage.setMsg(listOfNames);
			result = studentMessage;
			break;

		case "GetAllStudentsThatChecked":
			listOfNames = getNamesWhoChecked();
			studentMessage.setMsg(listOfNames);
			result = studentMessage;
			break;
		}
		return result;
	}

	/**
	 * This method get list of all students names that did specific exam and wasn't
	 * checked yet.
	 * 
	 * @param code The code of the wanted exam.
	 * @return listOfNames {@link ArrayList} if found in DB success, null otherwise.
	 */
	public static ArrayList<String> getNames(String code) {
		ArrayList<String> listOfNames = new ArrayList<>();
		ResultSet newRs = null;
		String sql = "SELECT userName FROM examOfStudent WHERE exam_code = ? AND teacher_check = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			pstmt.setBoolean(2, false);
			newRs = pstmt.executeQuery();
			while (newRs.next()) {
				listOfNames.add(UserController.getName(newRs.getString("userName")));
			}
		} catch (SQLException e) {
			return listOfNames;
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
		if (listOfNames.isEmpty())
			listOfNames = null;
		return listOfNames;
	}

	/**
	 * This method get list of all students names that did specific exam and was
	 * checked.
	 * 
	 * @return listOfNames {@link ArrayList} if found in DB success, null otherwise.
	 */
	public static ArrayList<String> getNamesWhoChecked() {
		ArrayList<String> listOfNames = new ArrayList<>();
		String examID;
		ResultSet newRs = null;
		String sql = "SELECT * FROM examOfStudent WHERE teacher_check = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setBoolean(1, true);
			newRs = pstmt.executeQuery();
			while (newRs.next()) {
				String fullName = UserController.getName(newRs.getString("userName"));
				examID = newRs.getString("fid") + newRs.getString("cid") + newRs.getString("eid");
				if (ExamController.getExamType(examID).equals("computerized") && !(listOfNames.contains(fullName)))
					listOfNames.add(fullName);
			}
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
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
		if (listOfNames.isEmpty())
			listOfNames = null;
		return listOfNames;
	}

	/**
	 * This method update exam of student that was checked by teacher.
	 *
	 * @param exam The {@link ExamOfStudent} that was checked.
	 * @return true if update success, false otherwise.
	 */
	public static boolean examWasChecked(ExamOfStudent exam) {
		String query;
		query = "UPDATE examOfStudent SET teacher_check = ? , teacher_note = ?, grade = ? "
				+ "WHERE exam_code = ? AND userName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(query);
			pstmt.setBoolean(1, true);
			pstmt.setString(2, exam.getTeachNote());
			pstmt.setInt(3, exam.getGrade());
			pstmt.setString(4, exam.getCode());
			pstmt.setString(5, exam.getUserName());
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
		return true;
	}

	/**
	 * This method get computerized exam of specific student.
	 * 
	 * @return {@link ExamOfStudent} if found in dataBase else return null.
	 */
	public static ExamOfStudent getExam(String userName, String code) {
		ExamOfStudent ExamOfStudent = new ExamOfStudent();
		// execute query
		String sql = "SELECT * FROM examofstudent WHERE userName = ? AND exam_code = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			pstmt.setString(2, code);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ExamOfStudent.setUserName(userName);
				ExamOfStudent.setFid(rs.getString("fid"));
				ExamOfStudent.setFname(FieldOfStudyController.GetFname(rs.getString("fid")));
				ExamOfStudent.setCid(rs.getString("cid"));
				ExamOfStudent.setCname(CourseController.GetCname(rs.getString("cid")));
				ExamOfStudent.setEid(rs.getString("eid"));
				ExamOfStudent.setGrade(rs.getInt("grade"));
				ExamOfStudent.setAnswers(rs.getString("ans"));
				ExamOfStudent.setCode(rs.getString("exam_code"));
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
		return ExamOfStudent;
	}

	/**
	 * This method return the gradesList of exams that student did.
	 *
	 * @param name the student's name.
	 * @return listOfGrades {@link ArrayList} if found in dataBase else return null.
	 */
	public static ArrayList<Integer> getGrades(String name) {
		String userName = getUserName(name);
		ArrayList<Integer> listOfGrades = new ArrayList<>();
		String sql = "SELECT grade FROM ExamOfStudent WHERE userName = ? AND teacher_check = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			pstmt.setBoolean(2, true);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				listOfGrades.add(rs.getInt("grade"));
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
		if (listOfGrades.isEmpty())
			listOfGrades = null;
		return listOfGrades;
	}

	/**
	 * This method return the userName of student with specific name.
	 *
	 * @param name the student's name.
	 * @return userName if found in dataBase else return null.
	 */
	public static String getUserName(String name) {
		String[] res = parsingTheData(name);
		String firstName = res[0];
		String lastName = res[1];
		String userName = null;
		String sql = "SELECT userName FROM Users WHERE firstName = ? AND lastName = ? AND urole = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setString(3, "student");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				userName = rs.getString("userName");
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
		return userName;
	}

	/**
	 * This method gets all the exams of student by userName.
	 * 
	 * @return {@link ExamOfStudent} {@link ArrayList} if found in dataBase else
	 *         return null.
	 */
	public static ArrayList<ExamOfStudent> getExamsOfStudent(String userName) {
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
				es.setTeachNote(rs1.getString("teacher_note"));
				es.setEdate(getExamDate(es.getCode()));
				getExamType(es, es.getFid(), es.getCid(), es.getEid());
				if (es.getEtype() == ExamType.COMPUTERIZED)
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
		if (ExamsOfStudentList.isEmpty())
			ExamsOfStudentList = null;
		return ExamsOfStudentList;
	}

	/**
	 * This method get the exam status.
	 */
	public static void getExamType(ExamOfStudent es, String Fid, String Cid, String Eid) {
		ResultSet newRs = null;
		String sql = "SELECT etype FROM exam WHERE fid = ? AND cid = ? AND eid = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, Fid);
			pstmt.setString(2, Cid);
			pstmt.setString(3, Eid);
			newRs = pstmt.executeQuery();
			while (newRs.next()) {
				switch (newRs.getString("etype")) {
				case "computerized":
					es.setEtype(ExamType.COMPUTERIZED);
					break;
				case "manual":
					es.setEtype(ExamType.MANUAL);
					break;
				}
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
	}

	/**
	 * This method get the exam of student date.
	 */
	public static String getExamDate(String code) {
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
		String sql = "INSERT INTO examOfStudent VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, exam.getStudentName());
			pstmt.setString(2, fieldID);
			pstmt.setString(3, courseID);
			pstmt.setString(4, examID);
			pstmt.setDouble(5, exam.getRealTimeDuration());
			pstmt.setInt(6, exam.getGrade());
			pstmt.setString(8, exam.getAnswers());
			pstmt.setString(10, exam.getCode());
			pstmt.setString(11, "");
			switch (type) {
			case "computerized":
				byte[] empty = {};
				pstmt.setBlob(7, new ByteArrayInputStream(empty));
				pstmt.setBoolean(9, false);
				break;
			case "manual":
				pstmt.setBlob(7, inputStream);
				// teacher checks the exam manually
				pstmt.setBoolean(9, true);
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
}
//End of StudentController class