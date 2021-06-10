package control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.ParagraphStyle;
import logic.Exam;
import logic.ExamFile;
import logic.ExamType;
import logic.Message;
import logic.Question;

/**
 * This class execute all queries that related to Exam object and handle with
 * all objects that connect to these queries.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @author Moran Davidov
 * @version April 2021
 */

public class ExamController {

	// Instance variables **********************************************

	static final int MIN = 60 * 1000;
	static final int SEC = 1000;

	// messages that ExamController receive from server (request) and sent to it.
	private static Message request;
	private static Message result;
	// all components of the entire exam identification number.
	private static String fieldID;
	private static String courseID;
	private static String examID;
	// variables for execute queries and handle the results from DB.
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;

	public static boolean flagForTimer = true;
	public static int SecTimer = 0, MinTimer = 0, HourTimer = 0;

	// Instance methods ************************************************

	/**
	 * This method handles all requests that comes in from the server.
	 *
	 * @param msg {@link Message} The request message from the server.
	 * @return result {@link Message} The result message for the server.
	 */
	public static Message handleRequest(Message msg) {
		Message examMessage = new Message();
		request = msg;
		boolean isExists;
		ArrayList<Exam> listOfExams;
		String[] FieldName_CourseName;
		String[] data;
		String examID;
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

		case "updateExamDurationTime":
			String[] updateDetails = parsingTheData((String) request.getMsg());
			double duration = Double.parseDouble(updateDetails[1]);
			examID = updateDetails[0];
			boolean isChanged = updateExamDurationQuery(examID, duration);
			examMessage.setMsg(isChanged);
			result = examMessage;
			break;

		case "downloadManualExam":
			ExamFile res = null;
			data = parsingTheData((String) request.getMsg());
			examID = getExamID(data[0]);
			if (examID != null) {
				if (!checkStudentDidExam(examID, data[2])) {
					res = getExamFile(data[0], data[1]);
					if (CheckTimeOfExam(data[0]) <= 10) {
						examMessage.setMsg(res);
					} else
						examMessage.setMsg("too late to get into the exam");
				} else
					examMessage.setMsg("student is already did the exam");
			}
			result = examMessage;
			break;

		case "ShowExamList":
			FieldName_CourseName = parsingTheData((String) request.getMsg());
			listOfExams = getExams(FieldName_CourseName[0], FieldName_CourseName[1]);
			examMessage.setMsg(listOfExams);
			result = examMessage;
			break;

		case "FindExamOfStudent":
			Exam rExam = null;
			data = parsingTheData((String) request.getMsg());
			examID = getExamID(data[0]);
			if (examID != null)
				rExam = getComputerizedExam(data[0], data[1]);
			examMessage.setMsg(rExam);
			result = examMessage;
			break;

		case "StartComputerizedExam":
			Exam resExam = null;
			data = parsingTheData((String) request.getMsg());
			examID = getExamID(data[0]);
			if (examID != null) {
				if (!checkStudentDidExam(examID, data[2])) {
					if (CheckTimeOfExam(data[0]) <= 10) {
						resExam = checkComputerizedExamCode(data[0], data[1]);
						examMessage.setMsg(resExam);
					} else
						examMessage.setMsg("too late to get into the exam");
				} else
					examMessage.setMsg("student is already did the exam");
			}
			result = examMessage;
			break;

		case "CheckExamCodeIsUnique":
			boolean isUnique = checkExamCodeIsUnique((String) request.getMsg());
			examMessage.setMsg(isUnique);
			result = examMessage;
			break;

		case "InsertExamToExamToPerformTable":
			boolean isInsert = insertExamToExamToPerformTable((Exam) request.getMsg());
			examMessage.setMsg(isInsert);
			result = examMessage;
			break;

		case "GetExamDuration":
			double dur = getDuration((String) request.getMsg());
			examMessage.setMsg(dur);
			result = examMessage;
			break;

		case "CheckCodeExistsForRequest":
			isExists = checkCodeExistsAndOpen((String) request.getMsg());
			examMessage.setMsg(isExists);
			result = examMessage;
			break;

		case "CheckCodeExistsForOpenExam":
			isExists = checkCodeExistsAndLocked((String) request.getMsg());
			examMessage.setMsg(isExists);
			result = examMessage;
			break;

		case "CheckCodeExistsForCheckExam":
			isExists = checkCodeExistsAndDone((String) request.getMsg());
			examMessage.setMsg(isExists);
			result = examMessage;
			break;

		case "CheckStudentsCopies":
			ArrayList<String> students;
			students = checkCopies((String) request.getMsg());
			examMessage.setMsg(students);
			result = examMessage;
			break;

		case "LockExam":
			boolean isLocked = lockExam((String) request.getMsg());
			examMessage.setMsg(isLocked);
			result = examMessage;
			break;

		case "OpenExam":
			boolean isOpend = openExam((String) request.getMsg());
			examMessage.setMsg(isOpend);
			result = examMessage;
			openTimer((String) request.getMsg());
			break;

		case "GetExamStatus":
			String status = getExamStatus((String) request.getMsg());
			examMessage.setMsg(status);
			result = examMessage;
			break;

		case "increaseCounter":
			increaseExamCounter((String) request.getMsg());
			break;

		case "DecreaseExam":
			DecreaseExamCounter((String) request.getMsg());
			checkforDone((String) request.getMsg());
			break;

		case "CheckTimeOfExam":
			int timer = CheckTimeOfExam((String) request.getMsg());
			examMessage.setMsg(timer);
			result = examMessage;
		} // end switch case
		return result;
	}

	/**
	 * This method check copies between students
	 *
	 * @param code - The code from examToPerform table.
	 * @return {@link ArrayList} names of students that are suspected for coping.
	 * 
	 */
	public static ArrayList<String> checkCopies(String code) {
		ArrayList<String> students = new ArrayList<>();
		String sql = "SELECT DISTINCT e1.userName ,e2.userName FROM examOfStudent as e1, examOfStudent as e2 "
				+ "WHERE e1.userName < e2.userName AND e1.exam_code = e2.exam_code AND e1.ans = e2.ans "
				+ "AND e1.exam_code = ? AND e1.teacher_check = e2.teacher_check "
				+ "AND e1.teacher_check = ? AND e2.teacher_check = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			pstmt.setBoolean(2, false);
			pstmt.setBoolean(3, false);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String fullUserName1 = UserController.getName(rs.getString("e1.userName"));
				String fullUserName2 = UserController.getName(rs.getString("e2.userName"));
				if (!students.contains(fullUserName1))
					students.add(fullUserName1);
				if (!students.contains(fullUserName2))
					students.add(fullUserName2);
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
		if (students.isEmpty())
			students = null;
		return students;
	}

	/**
	 * This method check if the code is exists in table examToPerform and the exam
	 * is done.
	 *
	 * @param code The code from examToPerform table.
	 * @return true is code exists, false otherwise.
	 */
	public static boolean checkCodeExistsAndDone(String code) {
		boolean res = false;
		String sql = "SELECT ecode FROM examToPerform WHERE ecode = ? AND estatus = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			pstmt.setString(2, "done");
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
	 * This method open the exam's timer.
	 * 
	 */
	public static void openTimer(String code) {

		new Thread(() -> {
			try {
				while (flagForTimer) {
					Thread.sleep(MIN);
					updateTimer(code);
					checkforDone(code);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	/**
	 * This method update the exam's timer.
	 * 
	 */
	private static void updateTimer(String code) {
		String sql = "UPDATE examtoperform SET timer = timer + 1  WHERE ecode = ? ";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
		}
		try {
			pstmt.close();
		} catch (Exception e) {
			DBconnector.printException(e);
		}
	}

	/**
	 * This method get the countPerformers from examToPerform table
	 *
	 * @param code - The code from examToPerform table.
	 * 
	 */
	public static int GetcountPerformers(String code) {
		int numberOfPerformers = 0;
		String sql = "SELECT countPerformers FROM examtoperform WHERE ecode = ? ";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				numberOfPerformers = rs.getInt("countPerformers");
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
		return numberOfPerformers;

	}

	/**
	 * This method check if the exam is done
	 *
	 * @param code - The code from examToPerform table.
	 * 
	 */
	public static void checkforDone(String code) {
		int currTime = CheckTimeOfExam(code);
		int numberOfPerformers = GetcountPerformers(code);
		if (currTime > 10 && numberOfPerformers == 0) {
			UpdateExamToDone(code);
			flagForTimer = false;
		}
	}

	/**
	 * This method update the Exam status to be done
	 *
	 * @param code - The code from examToPerform table.
	 * 
	 */
	public static void UpdateExamToDone(String code) {
		String sql = "UPDATE examtoperform SET estatus =  ?  WHERE ecode = ? ";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, "done");
			pstmt.setString(2, code);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
		}
		try {
			pstmt.close();
		} catch (Exception e) {
			DBconnector.printException(e);
		}
	}

	/**
	 * This method update the countPerformers to be countPerformers -1 in
	 * examToPerform table
	 *
	 * @param code - The code from examToPerform table.
	 * 
	 */
	public static void DecreaseExamCounter(String code) {
		String sql = "UPDATE examtoperform SET countPerformers =  countPerformers - 1  WHERE ecode = ? ";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
		}
		try {
			pstmt.close();
		} catch (Exception e) {
			DBconnector.printException(e);
		}
	}

	/**
	 * This method update the countPerformers to be countPerformers +1 in
	 * examToPerform table
	 *
	 * @param code - The code from examToPerform table.
	 * 
	 */
	public static void increaseExamCounter(String code) {
		String sql = "UPDATE examtoperform SET countPerformers =  countPerformers + 1  WHERE ecode = ? ";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
		}
		try {
			pstmt.close();
		} catch (Exception e) {
			DBconnector.printException(e);
		}
	}

	/**
	 * This method get the exam timer from table examToPerform.
	 *
	 * @param code - The code from examToPerform table.
	 * @return timer.
	 */
	public static int CheckTimeOfExam(String code) {
		int timer = 0; 
		String sql = "SELECT timer FROM examtoperform WHERE ecode = ? ";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				timer = rs.getInt("timer");
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
		return timer;
	}

	/**
	 * This method get the exam from table exam.
	 *
	 * @param code The code from examToPerform table.
	 * @param type The exam type.
	 * @return exam {@link Exam} if code exists, null otherwise.
	 */
	public static Exam getComputerizedExam(String code, String type) {
		Exam exam = null;
		String Fid = null, Cid = null, Eid = null;
		String sql = "SELECT e.fid, e.cid, e.eid FROM exam as e, examToPerform as ep "
				+ "WHERE e.fid = ep.fid AND e.cid = ep.cid AND e.eid = ep.eid AND e.etype = ? AND ep.ecode = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, type);
			pstmt.setString(2, code);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Fid = rs.getString("fid");
				Cid = rs.getString("cid");
				Eid = rs.getString("eid");
			}
		} catch (SQLException e) {
			return null;
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
		if (Fid != null && Cid != null && Eid != null)
			exam = getExam(Fid, Cid, Eid);
		return exam;
	}

	public static String getExamStatus(String code) {
		String Status = "";
		String sql = "SELECT estatus FROM examtoperform WHERE ecode = ? ";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Status = rs.getString("estatus");
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
		return Status;
	}

	/**
	 * This method lock an exam in examToPerform table.
	 *
	 * @param code The code of the exam we want to lock.
	 * @return true if lock success, false otherwise.
	 */
	public static boolean lockExam(String code) {
		String query;
		query = "UPDATE examToPerform SET estatus = ? WHERE ecode = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(query);
			pstmt.setString(1, "locked");
			pstmt.setString(2, code);
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
	 * This method open an exam in examToPerform table.
	 *
	 * @param code The code of the exam we want to open.
	 * @return true if open success, false otherwise.
	 */
	public static boolean openExam(String code) {
		String query;
		query = "UPDATE examToPerform SET estatus = ? WHERE ecode = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(query);
			pstmt.setString(1, "open");
			pstmt.setString(2, code);
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
	 * This method check if exam code is unique.
	 *
	 * @param code The code of the exam.
	 * @return true if the code is already exist, otherwise return false.
	 */
	public static boolean checkExamCodeIsUnique(String code) {
		String sql = "SELECT ecode FROM examtoperform WHERE ecode = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (code.equals(rs.getString("ecode")))
					return true;
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
		return false;
	}

	/**
	 * This method insert exam to examToPerform table.
	 *
	 * @param exam {@link Exam} we want to insert.
	 * @return true if success, false otherwise.
	 */
	public static boolean insertExamToExamToPerformTable(Exam exam) {
		String sql = "INSERT INTO examToPerform VALUES (?,?,?,?,?,?,?,?)";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, exam.getFid());
			pstmt.setString(2, exam.getCid());
			pstmt.setString(3, exam.getEid());
			pstmt.setString(4, exam.getEcode());
			pstmt.setString(5, "lock");
			pstmt.setString(6, exam.getEdate().toString());
			pstmt.setInt(7, 0);
			pstmt.setInt(8, 0);
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
	 * This method get an exam object from DB.
	 *
	 * @param Fid The id of the field.
	 * @param Cid The id of the course.
	 * @param Eid The id of the exam.
	 * @return exam The exam that we want to show. If not found, return null.
	 */
	public static Exam getExam(String Fid, String Cid, String Eid) {
		Exam exam = new Exam();
		ResultSet Rs = null;
		// get the data from the relevant controllers
		String Fname = FieldOfStudyController.GetFname(Fid);
		String Cname = CourseController.GetCname(Cid);
		// execute query
		String sql = "SELECT author, duration FROM Exam WHERE fid = ? AND cid = ? AND eid = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, Fid);
			pstmt.setString(2, Cid);
			pstmt.setString(3, Eid);
			Rs = pstmt.executeQuery();
			while (Rs.next()) {
				exam.setFname(Fname);
				exam.setCname(Cname);
				exam.setEid(Eid);
				exam.setFid(Fid);
				exam.setCid(Cid);
				exam.setExamID(Fid + Cid + Eid);
				exam.setAuthor(Rs.getString("author"));
				exam.setDuration(Rs.getDouble("duration"));
				exam.setQuestionsInExam(getQuestionsInExam(Fid, Cid, Eid));
			}
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
		} finally {
			try {
				Rs.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
			try {
				pstmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		return exam;
	}

	/**
	 * This method responsible to get list of exams from DB .
	 *
	 * @param Fname The name of the field.
	 * @param Cname The name of the course.
	 * @return listOfexams {@link ArrayList} The exams that belong to those field
	 *         and course. If there is no such exams, return null.
	 */
	public static ArrayList<Exam> getExams(String Fname, String Cname) {
		ArrayList<Exam> listOfExams = new ArrayList<>();
		ResultSet rs1 = null;
		// get the data from the relevant controllers
		String Fid = FieldOfStudyController.GetFid(Fname);
		String Cid = CourseController.GetCid(Cname);
		// execute query
		String sql = "SELECT * FROM Exam WHERE fid = ? AND cid = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, Fid);
			pstmt.setString(2, Cid);
			rs1 = pstmt.executeQuery();
			while (rs1.next()) {
				Exam e = new Exam();
				e.setFname(Fname);
				e.setCname(Cname);
				e.setEid(rs1.getString("eid"));
				e.setFid(Fid);
				e.setCid(Cid);
				e.setExamID(e.getFid() + e.getCid() + e.getEid());
				e.setAuthor(rs1.getString("author"));
				e.setDuration(rs1.getDouble("duration"));
				e.setQuestionsInExam(getQuestionsInExam(e.getFid(), e.getCid(), e.getEid()));
				switch (rs1.getString("etype")) {
				case "computerized":
					e.setEtype(ExamType.COMPUTERIZED);
					break;
				case "manual":
					e.setEtype(ExamType.MANUAL);
					break;
				}
				listOfExams.add(e);
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
		if (listOfExams.isEmpty())
			listOfExams = null;
		return listOfExams;
	}

	/**
	 * This method get the questions of specific exam.
	 *
	 * @param Fid of the exam.
	 * @param Cid of The exam.
	 * @param Eid of The exam.
	 * @return questionsInExam {@link HashMap} of all the questions in this specific
	 *         exam.
	 */
	public static HashMap<Question, Integer> getQuestionsInExam(String Fid, String Cid, String Eid) {
		HashMap<Question, Integer> questionsInExam = new HashMap<>();
		int score;
		ResultSet newRs = null;
		String sql = "SELECT * FROM questioninexam WHERE fid = ? AND cid = ? AND eid = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, Fid);
			pstmt.setString(2, Cid);
			pstmt.setString(3, Eid);
			newRs = pstmt.executeQuery();
			while (newRs.next()) {
				Question q = new Question();
				q.setFid(Fid);
				q.setCid(Cid);
				q.setQid(newRs.getString("qid"));
				score = newRs.getInt("score");
				q.setStudentNote(newRs.getString("studentNote"));
				q.setTeacherNote(newRs.getString("teacherNote"));
				getMoreFieldsForQuestion(q);
				questionsInExam.put(q, score);
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
		return questionsInExam;
	}

	/**
	 * This method get a question and add it extra fields.
	 *
	 * @param question {@link Question} The question we want to get more details
	 *                 about.
	 */
	public static void getMoreFieldsForQuestion(Question question) {
		String sql = "SELECT * FROM question WHERE fid = ? AND cid = ? AND qid = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, question.getFid());
			pstmt.setString(2, question.getCid());
			pstmt.setString(3, question.getQid());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				question.setQuestionID(question.getFid() + question.getCid() + question.getQid());
				question.setContent(rs.getString("content"));
				question.setInstructions(rs.getString("instructions"));
				question.setRightAnswer(rs.getString("rightAnswer"));
				question.setWrongAnswer1(rs.getString("wrongAnswer1"));
				question.setWrongAnswer2(rs.getString("wrongAnswer2"));
				question.setWrongAnswer3(rs.getString("wrongAnswer3"));
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
	}

	/**
	 * This method get the exam id from given code.
	 *
	 * @param code The code from examToPerform table.
	 * @return examID The id. if not found return null.
	 */
	public static String getExamID(String code) {
		String sql = "SELECT fid, cid, eid FROM examToPerform WHERE ecode = ?";
		String examID = null;
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				examID = rs.getString("fid");
				examID += rs.getString("cid");
				examID += rs.getString("eid");
			}
		} catch (SQLException e) {
			return null;
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
		return examID;
	}

	/**
	 * This method get the exam file from table exam.
	 *
	 * @param code The code from examToPerform table.
	 * @param type The exam type.
	 * @return ExamFile {@link ExamFile} The file from DB.
	 */
	public static ExamFile getExamFile(String code, String type) {
		String sql = "SELECT upload_file, file_Name FROM exam as e, examToPerform as ep "
				+ "WHERE e.fid = ep.fid AND e.cid = ep.cid AND e.eid = ep.eid AND ep.ecode = ? AND e.etype = ? AND ep.estatus = ?";
		Blob fileData = null;
		String fileName = null;
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			pstmt.setString(2, type);
			pstmt.setString(3, "open");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				fileName = rs.getString("file_Name");
				fileData = rs.getBlob("upload_file");
			}
			int len = (int) fileData.length();
			return new ExamFile(fileData.getBytes(1, len), fileName);
		} catch (Exception e) {
			return null;
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
	}

	/**
	 * This method check if the student already did this exam.
	 *
	 * @param ExamID   of the exam.
	 * @param UserName of the logged in student.
	 * @return true if already did the specific exam, false otherwise.
	 */
	public static boolean checkStudentDidExam(String ExamID, String UserName) {
		String[] examIDcomponents = parsingTheExamId(ExamID);
		// data from parsingTheExamId method
		fieldID = examIDcomponents[0];
		courseID = examIDcomponents[1];
		examID = examIDcomponents[2];
		boolean res = false;
		String sql = "SELECT * FROM examOfStudent WHERE fid = ? AND cid = ? AND eid = ? AND userName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, fieldID);
			pstmt.setString(2, courseID);
			pstmt.setString(3, examID);
			pstmt.setString(4, UserName);
			rs = pstmt.executeQuery();
			while (rs.next())
				res = true; // student already did this exam or it isn't exists
		} catch (SQLException e) {
			return res;
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
		return res;
	}

	/**
	 * This method get the exam from table exam.
	 *
	 * @param code The code from examToPerform table.
	 * @param type The exam type.
	 * @return exam {@link Exam} if code exists, null otherwise.
	 */
	public static Exam checkComputerizedExamCode(String code, String type) {
		Exam exam = null;
		String Fid = null, Cid = null, Eid = null;
		String sql = "SELECT e.fid, e.cid, e.eid FROM exam as e, examToPerform as ep "
				+ "WHERE e.fid = ep.fid AND e.cid = ep.cid AND e.eid = ep.eid AND e.etype = ? AND ep.ecode = ? AND ep.estatus = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, type);
			pstmt.setString(2, code);
			pstmt.setString(3, "open");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Fid = rs.getString("fid");
				Cid = rs.getString("cid");
				Eid = rs.getString("eid");
			}
		} catch (SQLException e) {
			return null;
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
		if (Fid != null && Cid != null && Eid != null)
			exam = getExam(Fid, Cid, Eid);
		return exam;
	}

	/**
	 * This method check if the code is exists in table examToPerform.
	 *
	 * @param code The code from examToPerform table.
	 * @return true is code exists, false otherwise.
	 */
	public static boolean checkCodeExistsAndOpen(String code) {
		boolean res = false;
		String sql = "SELECT ecode FROM examToPerform WHERE ecode = ? AND estatus = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			pstmt.setString(2, "open");
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
	 * This method check if the code is exists in table examToPerform.
	 *
	 * @param code The code from examToPerform table.
	 * @return true is code exists, false otherwise.
	 */
	public static boolean checkCodeExistsAndLocked(String code) {
		boolean res = false;
		String sql = "SELECT ecode FROM examToPerform WHERE ecode = ? AND estatus = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			pstmt.setString(2, "locked");
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
	 * This method get the exam duration time from table exam.
	 *
	 * @param code The code from examToPerform table.
	 * @return exam's duration.
	 */
	public static double getDuration(String code) {
		double duration = 0.0;
		String sql = "SELECT e.duration FROM exam as e, examToPerform as ep "
				+ "WHERE e.fid = ep.fid AND e.cid = ep.cid AND e.eid = ep.eid AND ep.ecode = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, code);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				duration = rs.getDouble("duration");
			}
		} catch (SQLException e) {
			return 0.0;
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
		return duration;
	}

	/**
	 * This method return the exam's type.
	 *
	 * @param examKey The entire exam ID.
	 * @return type The type of the exam.
	 */
	public static String getExamType(String examKey) {
		String[] examIDcomponents = parsingTheExamId(examKey);
		String type = null;
		// data from parsingTheExamId method
		fieldID = examIDcomponents[0];
		courseID = examIDcomponents[1];
		examID = examIDcomponents[2];
		String sql = "SELECT etype FROM exam WHERE fid = ? AND cid = ? AND eid = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, fieldID);
			pstmt.setString(2, courseID);
			pstmt.setString(3, examID);
			rs = pstmt.executeQuery();
			while (rs.next())
				type = rs.getString("etype");
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
		return type;
	}

	/**
	 * This method responsible to save an exam in DB .
	 *
	 * @param exam {@link Exam} The exam from client.
	 * @return true if the save succeed, false otherwise.
	 */
	public static boolean saveExam(Exam exam) {
		String sql = "INSERT INTO Exam VALUES (?,?,?,?,?,?,?,?)";
		try {
			pstmt = DBconnector.conn.prepareStatement(sql);
			pstmt.setString(1, exam.getFid());
			pstmt.setString(2, exam.getCid());
			pstmt.setString(3, exam.getEid());
			pstmt.setString(4, exam.getAuthor());
			pstmt.setDouble(5, exam.getDuration());
			pstmt.setString(8, exam.getExamID() + ".docx");
			// set the exam type
			pstmt.setString(6, exam.getEtype().name());
			switch (exam.getEtype()) {
			// save exam file
			case COMPUTERIZED:
				byte[] empty = {};
				pstmt.setBlob(7, new ByteArrayInputStream(empty));
				break;
			case MANUAL:
				// create word document
				Document doc = new Document();
				Section section = doc.addSection();
				Paragraph paragraph = section.addParagraph();
				paragraph.appendText(exam.printExamToString());
				// text style
				ParagraphStyle style = new ParagraphStyle(doc);
				style.setName("titleStyle");
				style.getCharacterFormat().setFontName("Calibri");
				style.getCharacterFormat().setFontSize(12f);
				doc.getStyles().add(style);
				paragraph.applyStyle("titleStyle");
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				doc.saveToFile(outStream, FileFormat.Docx);
				byte[] docBytes = outStream.toByteArray();
				// save the file
				InputStream inputStream = new ByteArrayInputStream(docBytes);
				pstmt.setBlob(7, inputStream);
				break;
			}
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
		UpdateEid(exam);
		questionsInExam(exam);
		return true;
	}

	/**
	 * This method update the table questions in exam.
	 *
	 * @param exam {@link Exam} The exam from client.
	 */
	public static void questionsInExam(Exam exam) {
		Set<Question> QuestionSet = exam.getQuestionsInExam().keySet();
		for (Question q : QuestionSet) {
			String sql = "INSERT INTO questionInExam VALUES (?,?,?,?,?,?,?)";
			try {
				pstmt = DBconnector.conn.prepareStatement(sql);
				pstmt.setString(1, exam.getFid());
				pstmt.setString(2, exam.getCid());
				pstmt.setString(3, exam.getEid());
				pstmt.setString(4, q.getQid());
				pstmt.setInt(5, exam.getQuestionsInExam().get(q));
				pstmt.setString(6, q.getTeacherNote());
				pstmt.setString(7, q.getStudentNote());
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
	}

	/**
	 * This method responsible to update eid in DB .
	 *
	 * @param exam {@link Exam} The exam from client.
	 */
	public static void UpdateEid(Exam exam) {
		String query = "UPDATE eidtable SET eid = ? WHERE fieldName = ? AND courseName = ?";
		try {
			pstmt = DBconnector.conn.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(exam.getEid()));
			pstmt.setString(2, exam.getFname());
			pstmt.setString(3, exam.getCname());
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
	 * This method return the eid from DB.
	 *
	 * @param fieldName  from client.
	 * @param courseName from client.
	 * @return return eid if found in dataBase else return -1.
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
		return Eid;
	}

	/**
	 * This method update the exam table in 'duration' column, by specific ID.
	 *
	 * @param examKey     The entire exam ID.
	 * @param newDuration For the new exam duration time.
	 * @return true if success, false otherwise.
	 */
	public static boolean updateExamDurationQuery(String examKey, double newDuration) {
		boolean res = false;
		String[] examIDcomponents = parsingTheExamId(examKey);
		// data from parsingTheExamId method
		fieldID = examIDcomponents[0];
		courseID = examIDcomponents[1];
		examID = examIDcomponents[2];
		// Execute query for update the exam's duration time. Table Exam.
		String query = "UPDATE Exam SET duration = " + newDuration + " WHERE fid = " + fieldID + " AND " + "cid = "
				+ courseID + " AND " + "eid = " + examID;
		try {
			stmt = DBconnector.conn.createStatement();
			stmt.executeUpdate(query);
			DBconnector.display("DB: New exam duration time was updated");
		} catch (SQLException e) {
			return res;
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
		res = true;
		return res;
	}

	/**
	 * This method execute query for get exam's duration time, by specific ID.
	 *
	 * @param examKey The entire exam ID.
	 * @return duration The exam's duration time.
	 */
	public static double requestDurationTimeQuery(String examKey) {
		String[] examIDcomponents = parsingTheExamId(examKey);
		// data from parsingTheExamId method
		fieldID = examIDcomponents[0];
		courseID = examIDcomponents[1];
		examID = examIDcomponents[2];
		double duration = 0.0;
		// Execute query for get the content of 'Duration' column. Table Exam.
		String query = "SELECT duration FROM Exam WHERE fid = " + fieldID + " AND " + "cid = " + courseID + " AND "
				+ "eid = " + examID;
		try {
			stmt = DBconnector.conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next())
				duration = rs.getDouble("duration");
		} catch (SQLException e) {
			DBconnector.printSQLException(e);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
			try {
				stmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
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
}
//End of ExamController class