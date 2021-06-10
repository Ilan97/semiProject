package logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is an entity class that save details about each course that is created.
 * Implements Serializable to save the state of Course object and re-create it
 * as needed.
 *
 * @author Moran Davidov
 * @version May 2021
 */
@SuppressWarnings("serial")
public class Course implements Serializable {

	// Instance variables **********************************************
	private String cid;
	private String cname;
	private ArrayList<Question> questions = new ArrayList<>();

	// Constructors ****************************************************
	public Course() {

	}

	// Instance methods ************************************************

	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}

	/**
	 * @param cid The courses to set
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
	 * @param cname The cname to set
	 */
	public void setCname(String cname) {
		this.cname = cname;
	}

	/**
	 * @return the questions
	 */
	public ArrayList<Question> getQuestions() {
		return questions;
	}

	/**
	 * @param questions The questions to set
	 */
	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}

	/**
	 * @return result The hashCode of Course object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cid == null) ? 0 : cid.hashCode());
		result = prime * result + ((cname == null) ? 0 : cname.hashCode());
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
		return result;
	}

	/**
	 * @return true If object is this Course, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
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
		if (questions == null) {
			if (other.questions != null)
				return false;
		} else if (!questions.equals(other.questions))
			return false;
		return true;
	}
}
//End of Course class