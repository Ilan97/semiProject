package logic;

import java.io.Serializable;

/**
 * This is an entity class that save details about each exam that is created.
 * Implements Serializable to save the state of ExamOfStudent object and
 * re-create it as needed.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @version May 2021
 */

@SuppressWarnings("serial")

public class ExamOfStudent implements Serializable {

	// Instance variables **********************************************

	private byte[] content;
	private String code;
	private String studentName;
	private String userName;
	private double realTimeDuration;
	private int grade;
	private String answers;
	private String eid;
	private String fid;
	private String fname;
	private String cid;
	private String cname;
	private String edate;

	// Constructors ****************************************************

	public ExamOfStudent() {
	}
	
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
		return "Course:\t" + getCname() + "\nField:\t" + getFname() + "\nExam Date:\t" + getEdate();
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

	/**
	 * @return the eid
	 */
	public String getEid() {
		return eid;
	}

	/**
	 * @param eid the eid to set
	 */
	public void setEid(String eid) {
		this.eid = eid;
	}

	/**
	 * @return the fid
	 */
	public String getFid() {
		return fid;
	}

	/**
	 * @param fid the fid to set
	 */
	public void setFid(String fid) {
		this.fid = fid;
	}

	/**
	 * @return the fname
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * @param fname the fname to set
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}

	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}

	/**
	 * @param cid the cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}

	/**
	 * @return the cname
	 */
	public String getCname() {
		return cname;
	}

	/**
	 * @param cname the cname to set
	 */
	public void setCname(String cname) {
		this.cname = cname;
	}

	/**
	 * @return the edate
	 */
	public String getEdate() {
		return edate;
	}

	/**
	 * @param edate the edate to set
	 * @return 
	 */
	public void setEdate(String edate) {
		this.edate = edate;
	}

	/**
	 * @param realTimeDuration the realTimeDuration to set
	 */
	public void setRealTimeDuration(double realTimeDuration) {
		this.realTimeDuration = realTimeDuration;
	}

	/**
	 * @return the usertName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param usertName the usertName to set
	 */
	public void setUserName(String usertName) {
		this.userName = usertName;
	}
	
	
	
	
}
//End of ExamOfStudent class