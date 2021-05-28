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

	// Instance methods ************************************************

	/**
	 * This method handles all requests that comes in from the server.
	 *
	 * @param msg {@link Message} The request message from the server.
	 * @return result The result message for the server.
	 */
	public static Message handleRequest(Message msg) {
		Message examMessage = new Message();
		request = msg;
		ArrayList<Exam> listOfExams;
		String[] FieldName_CourseName;
		String[] data;
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
				exam.setDuration(Double.parseDouble(resultMap.get("duration")));
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
			double duration = Double.parseDouble(updateDetails[0]);
			String examID = updateDetails[1];
			updateExamDurationQuery(examID, duration);
			// create the result Message instance
			examMessage.setMsg(requestDurationTimeQuery(examID)); // return the new duration time
			result = examMessage;
			break;

		case "downloadManualExam":
			data = parsingTheData((String) request.getMsg());
			ExamFile res = getExamFile(data[0], data[1]);
			examMessage.setMsg(res);
			result = examMessage;
			break;

		case "ShowExamList":
			FieldName_CourseName = parsingTheData((String) request.getMsg());
			listOfExams = getExams(FieldName_CourseName[0], FieldName_CourseName[1]);
			examMessage.setMsg(listOfExams);
			result = examMessage;
			break;

		case "CheckCodeExists":
			data = parsingTheData((String) request.getMsg());
			Exam resExam = checkComputerizedExamCode(data[0], data[1]);
			examMessage.setMsg(resExam);
			result = examMessage;
			break;

		case "GetExamDuration":
			double dur = getDuration((String) request.getMsg());
			examMessage.setMsg(dur);
			result = examMessage;
			break;
		} // end switch case
		return result;
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
//				switch (Rs.getString("etype")) {
//				case "computerized":
//					exam.setEtype(ExamType.COMPUTERIZED);
//					break;
//				case "manual":
//					exam.setEtype(ExamType.MANUAL);
//					break;
//				}
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
	 * @return listOfexams The exams that belong to those field and course. If there
	 *         is no such exams, return null.
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
		return listOfExams;
	}

	/**
	 * This method get the questions of specific exam.
	 *
	 * @param Fid of the exam.
	 * @param Cid of The exam.
	 * @param Eid of The exam.
	 * @return HashMap<Question, Integer> of all the questions in this specific
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
				getMoreFieldsForQuestion(q);
				questionsInExam.put(q, score);
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
		return questionsInExam;
	}

	/**
	 * This method get a question and add it extra fields.
	 *
	 * @param question.
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
			e.printStackTrace();
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
	 * This method get the exam code from given code.
	 *
	 * @param code The code from examToPerform table.
	 * @return examID The id. if not found return null.
	 */
	public static String getExamCode(String code) {
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
	 * This method get the exam file from table exam.
	 *
	 * @param code The code from examToPerform table.
	 * @param type The exam type.
	 * @return Exam if code exists, null otherwise
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
	 * This method get the exam duration time from table exam.
	 *
	 * @param code - The code from examToPerform table.
	 * @return Exam duration
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
	 * @param exam The exam from client.
	 * @return boolean result if the save succeed.
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
				paragraph.appendText(exam.toString());
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
	 * @param exam The exam from client.
	 */
	private static void questionsInExam(Exam exam) {
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
	 * @param exam The exam from client.
	 */
	private static void UpdateEid(Exam exam) {
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
				DBconnector.printException(e);
			}
			try {
				stmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
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
				examDataMap.put("duration", "" + rs.getDouble("duration"));
				// put exam questions into the result map
				String questionID = rs.getString("qid");
				int questionScore = rs.getInt("score");
				examDataMap.put("question" + index, fieldID + " " + questionID + " " + questionScore);
				index++;
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
				stmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
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
	public static void updateExamDurationQuery(String examKey, double newDuration) {
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
			DBconnector.printSQLException(e);
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
				DBconnector.printException(e);
			}
		}
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
		/**
		 * Execute query for get the content of 'Duration' column. Table Exam.
		 */
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