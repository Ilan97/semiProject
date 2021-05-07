package logic;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * This is an entity class that save details about each exam that is created.
 * Implements Serilizable to save the state of Exam object and re-create it as
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
	private Date edate;
	private String ecode;
	private String author;
	private int duration;
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
	public Date getEdate() {
		return edate;
	}

	/**
	 * @param edate The edate to set
	 */
	public void setEdate(Date edate) {
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
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration The duration to set
	 */
	public void setDuration(int duration) {
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
	 * @return result The hashCode of Exam object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((cid == null) ? 0 : cid.hashCode());
		result = prime * result + ((cname == null) ? 0 : cname.hashCode());
		result = prime * result + duration;
		result = prime * result + ((ecode == null) ? 0 : ecode.hashCode());
		result = prime * result + ((edate == null) ? 0 : edate.hashCode());
		result = prime * result + ((eid == null) ? 0 : eid.hashCode());
		result = prime * result + ((estatus == null) ? 0 : estatus.hashCode());
		result = prime * result + ((etype == null) ? 0 : etype.hashCode());
		result = prime * result + ((examID == null) ? 0 : examID.hashCode());
		result = prime * result + ((fid == null) ? 0 : fid.hashCode());
		result = prime * result + ((fname == null) ? 0 : fname.hashCode());
		result = prime * result + ((questionsInExam == null) ? 0 : questionsInExam.hashCode());
		return result;
	}

	/**
	 * @return true If object is this Exam, false otherwise
	 */
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
		if (duration != other.duration)
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
		if (questionsInExam == null) {
			if (other.questionsInExam != null)
				return false;
		} else if (!questionsInExam.equals(other.questionsInExam))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Exam [fname=" + fname + ", cname=" + cname + ", examID=" + examID + ", edate=" + edate + ", ecode="
				+ ecode + ", author=" + author + ", duration=" + duration + ", estatus=" + estatus + ", etype=" + etype
				+ ", questionInExam=" + questionsInExam + "]";
	}
}
//End of Exam class