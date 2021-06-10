package logic;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

/**
 * This is an entity class that save details about each exam that is created.
 * Implements Serializable to save the state of Exam object and re-create it as
 * needed.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version April 2021
 */

@SuppressWarnings("serial")
public class Exam implements Serializable {

	// Instance variables **********************************************

	private String eid;
	private String fid;
	private String fname;
	private String cid;
	private String cname;
	private String examID;
	private LocalDate edate;
	private String ecode;
	private String author;
	private double duration;
	private ExamStatus estatus;
	private ExamType etype;
	private HashMap<Question, Integer> questionsInExam = new HashMap<>();

	// Constructors ****************************************************

	public Exam() {
	}

	// Instance methods ************************************************

	/**
	 * @return the eid
	 */
	public String getEid() {
		return eid;
	}

	/**
	 * @param eid The eid to set
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
	 * @param fid The fid to set
	 */
	public void setFid(String fid) {
		this.fid = fid;
	}

	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}

	/**
	 * @param cid The cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}

	/**
	 * @return the edate
	 */
	public LocalDate getEdate() {
		return edate;
	}

	/**
	 * @param edate The edate to set
	 */
	public void setEdate(LocalDate edate) {
		this.edate = edate;
	}

	/**
	 * @return the ecode
	 */
	public String getEcode() {
		return ecode;
	}

	/**
	 * @param ecode The ecode to set
	 */
	public void setEcode(String ecode) {
		this.ecode = ecode;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author The author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the duration
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * @param duration The duration to set
	 */
	public void setDuration(double duration) {
		this.duration = duration;
	}

	/**
	 * @return the estatus
	 */
	public ExamStatus getEstatus() {
		return estatus;
	}

	/**
	 * @param estatus The estatus to set
	 */
	public void setEstatus(ExamStatus estatus) {
		this.estatus = estatus;
	}

	/**
	 * @return the etype
	 */
	public ExamType getEtype() {
		return etype;
	}

	/**
	 * @param etype The etype to set
	 */
	public void setEtype(ExamType etype) {
		this.etype = etype;
	}

	/**
	 * @return the examID
	 */
	public String getExamID() {
		return examID;
	}

	/**
	 * @param examID The examID to set
	 */
	public void setExamID(String examID) {
		this.examID = examID;
	}

	/**
	 * @return the questionsInExam
	 */
	public HashMap<Question, Integer> getQuestionsInExam() {
		return questionsInExam;
	}

	/**
	 * @param questionsInExam the questionsInExam to set
	 */
	public void setQuestionsInExam(HashMap<Question, Integer> questionsInExam) {
		this.questionsInExam = questionsInExam;
	}

	/**
	 * @return the fname
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * @param fname The fname to set
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}

	/**
	 * @return the cname
	 */
	public String getCname() {
		return cname;
	}

	/**
	 * @param cname The cname to set
	 */
	public void setCname(String cname) {
		this.cname = cname;
	}

	/**
	 * @return the string of all questions and their scores in the exam.
	 */
	public String allQuestionsToString() {
		int i = 1;
		String QuestionView = "";
		Set<Question> QuestionSet = questionsInExam.keySet();
		for (Question q : QuestionSet) {
			QuestionView += "Question number " + i + ": " + "(" + questionsInExam.get(q) + " points)\n";
			if (q.getStudentNote() != null)
				QuestionView += q.getStudentNote() + "\n";
			QuestionView += q.toString();
			i++;
		}
		return QuestionView;
	}

	/**
	 * @return the string of all questions and their scores in the exam.
	 */
	public String allQuestionsForTeacherToString() {
		int i = 1;
		String QuestionView = "";
		Set<Question> QuestionSet = questionsInExam.keySet();
		for (Question q : QuestionSet) {
			QuestionView += "Question number " + i + ": " + "(" + questionsInExam.get(q) + " points)\n";

			if (q.getStudentNote() != null && !q.getStudentNote().equals(""))
				QuestionView += "Note for student: " + q.getStudentNote() + "\n";

			if (q.getTeacherNote() != null && !q.getTeacherNote().equals(""))
				QuestionView += "Note for teacher: " + q.getTeacherNote() + "\n";

			QuestionView += q.toString();
			i++;
		}
		return QuestionView;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((cid == null) ? 0 : cid.hashCode());
		result = prime * result + ((cname == null) ? 0 : cname.hashCode());
		long temp;
		temp = Double.doubleToLongBits(duration);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((ecode == null) ? 0 : ecode.hashCode());
		result = prime * result + ((edate == null) ? 0 : edate.hashCode());
		result = prime * result + ((eid == null) ? 0 : eid.hashCode());
		result = prime * result + ((estatus == null) ? 0 : estatus.hashCode());
		result = prime * result + ((etype == null) ? 0 : etype.hashCode());
		result = prime * result + ((examID == null) ? 0 : examID.hashCode());
		result = prime * result + ((fid == null) ? 0 : fid.hashCode());
		result = prime * result + ((fname == null) ? 0 : fname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exam other = (Exam) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (cid == null) {
			if (other.cid != null)
				return false;
		} else if (!cid.equals(other.cid))
			return false;
		if (cname == null) {
			if (other.cname != null)
				return false;
		} else if (!cname.equals(other.cname))
			return false;
		if (Double.doubleToLongBits(duration) != Double.doubleToLongBits(other.duration))
			return false;
		if (ecode == null) {
			if (other.ecode != null)
				return false;
		} else if (!ecode.equals(other.ecode))
			return false;
		if (edate == null) {
			if (other.edate != null)
				return false;
		} else if (!edate.equals(other.edate))
			return false;
		if (eid == null) {
			if (other.eid != null)
				return false;
		} else if (!eid.equals(other.eid))
			return false;
		if (estatus != other.estatus)
			return false;
		if (etype != other.etype)
			return false;
		if (examID == null) {
			if (other.examID != null)
				return false;
		} else if (!examID.equals(other.examID))
			return false;
		if (fid == null) {
			if (other.fid != null)
				return false;
		} else if (!fid.equals(other.fid))
			return false;
		if (fname == null) {
			if (other.fname != null)
				return false;
		} else if (!fname.equals(other.fname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Id:\t" + getExamID() + "\nAuthor:\t" + getAuthor() + "\nExam Type:\t" + getEtype();
	}

	public String printExamToString() {
		return "Field: " + getFname() + "\n" + "Course: " + getCname() + "\n" + "Author: " + getAuthor() + "\n"
				+ "Duration: " + getDuration() + " minutes\n\n" + allQuestionsToString();
	}

}
//End of Exam class