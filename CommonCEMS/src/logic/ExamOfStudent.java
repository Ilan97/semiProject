package logic;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This is an entity class that save details about each exam that is created.
 * Implements Serializable to save the state of ExamOfStudent object and
 * re-create it as needed.
 *
 * @author Bat-El Gardin
 * @version May 2021
 */

@SuppressWarnings("serial")

public class ExamOfStudent implements Serializable {

	// Instance variables **********************************************

	private byte[] content;
	private String code;
	private String studentName;
	private double realTimeDuration;
	private int grade;
	private String answers;

	// Constructors ****************************************************

	/**
	 * @param content
	 * @param code
	 */
	public ExamOfStudent(byte[] content, String code, String studentName, double realTimeDuration) {
		super();
		this.content = content;
		this.code = code;
		this.studentName = studentName;
		this.realTimeDuration = realTimeDuration;
	}

	/**
	 * @param content
	 * @param code
	 */
	public ExamOfStudent(String code, String studentName, double realTimeDuration, int grade, String answers) {
		super();
		this.code = code;
		this.studentName = studentName;
		this.realTimeDuration = realTimeDuration;
		this.grade = grade;
		this.answers = answers;
	}

	// Instance methods ************************************************

	@Override
	public String toString() {
		return "ExamOfStudent [content=" + Arrays.toString(content) + ", code=" + code + ", studentName=" + studentName
				+ ", realTimeDuration=" + realTimeDuration + "]";
	}

	/**
	 * @return the realTimeDuration
	 */
	public double getRealTimeDuration() {
		return realTimeDuration;
	}

	/**
	 * @param realTimeDuration the realTimeDuration to set
	 */
	public void setRealTimeDuration(int realTimeDuration) {
		this.realTimeDuration = realTimeDuration;
	}

	/**
	 * @return the studentName
	 */
	public String getStudentName() {
		return studentName;
	}

	/**
	 * @param studentName the studentName to set
	 */
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the grade
	 */
	public int getGrade() {
		return grade;
	}

	/**
	 * @param grade the grade to set
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}

	/**
	 * @return the answers
	 */
	public String getAnswers() {
		return answers;
	}

	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(String answers) {
		this.answers = answers;
	}
}
//End of ExamOfStudent class