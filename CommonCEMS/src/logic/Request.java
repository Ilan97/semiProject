package logic;

import java.io.Serializable;

/**
 * This is an entity class that save details about each request that is created.
 * Implements Serializable to save the state of Request object and re-create it
 * as needed.
 *
 * @author Bat-El Gardin
 * @version June 2021
 */

@SuppressWarnings("serial")
public class Request implements Serializable {
	// Instance variables **********************************************

	private String ecode;
	private double newDur;
	private double oldDur;
	private String explanation;
	private String fname;
	private String cname;
	private String examID;
	private String edate;

	// Constructors ****************************************************

	public Request(String ecode, double oldDur, double newDur, String expalanation) {
		this.ecode = ecode;
		this.oldDur = oldDur;
		this.newDur = newDur;
		this.explanation = expalanation;
	}

	public Request() {
	}

	// Instance methods ************************************************

	/**
	 * @return the ecode
	 */
	public String getEcode() {
		return ecode;
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
	 * @return the examID
	 */
	public String getExamID() {
		return examID;
	}

	/**
	 * @param examID the examID to set
	 */
	public void setExamID(String examID) {
		this.examID = examID;
	}

	/**
	 * @return the edate
	 */
	public String getEdate() {
		return edate;
	}

	/**
	 * @param edate the edate to set
	 */
	public void setEdate(String edate) {
		this.edate = edate;
	}

	/**
	 * @param ecode the ecode to set
	 */
	public void setEcode(String ecode) {
		this.ecode = ecode;
	}

	/**
	 * @return the newDur
	 */
	public double getNewDur() {
		return newDur;
	}

	/**
	 * @param newDur the newDur to set
	 */
	public void setNewDur(double newDur) {
		this.newDur = newDur;
	}

	/**
	 * @return the oldDur
	 */
	public double getOldDur() {
		return oldDur;
	}

	/**
	 * @param oldDur the oldDur to set
	 */
	public void setOldDur(double oldDur) {
		this.oldDur = oldDur;
	}

	/**
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * @param expalanation the explanation to set
	 */
	public void setExplanation(String expalanation) {
		this.explanation = expalanation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ecode == null) ? 0 : ecode.hashCode());
		result = prime * result + ((explanation == null) ? 0 : explanation.hashCode());
		long temp;
		temp = Double.doubleToLongBits(newDur);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(oldDur);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Request other = (Request) obj;
		if (ecode == null) {
			if (other.ecode != null)
				return false;
		} else if (!ecode.equals(other.ecode))
			return false;
		if (explanation == null) {
			if (other.explanation != null)
				return false;
		} else if (!explanation.equals(other.explanation))
			return false;
		if (Double.doubleToLongBits(newDur) != Double.doubleToLongBits(other.newDur))
			return false;
		if (Double.doubleToLongBits(oldDur) != Double.doubleToLongBits(other.oldDur))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Field:\t" + getFname() + "\nCourse:\t" + getCname() + "\nDate:\t" + getEdate();
	}
}
//End of Request class