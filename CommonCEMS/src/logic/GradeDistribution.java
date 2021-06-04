package logic;

import java.io.Serializable;

/**
 * This is an entity class that save details about each grade Distribution that
 * is created. Implements Serializable to save the state of GradeDistribution
 * object and re-create it as needed.
 *
 * @author Moran Davidov
 * @version May 2021
 */

@SuppressWarnings("serial")
public class GradeDistribution implements Serializable {

	// Instance variables **********************************************

	private int sid;
	private String gradeRange;
	private int studentCounter;

	// Constructors ****************************************************

	public GradeDistribution() {
	}

	// Instance methods ************************************************

	/**
	 * @return the sid
	 */
	public int getSid() {
		return sid;
	}

	/**
	 * @param sid The sid to set
	 */
	public void setSid(int sid) {
		this.sid = sid;
	}

	/**
	 * @return the gradeRange
	 */
	public String getGradeRange() {
		return gradeRange;
	}

	/**
	 * @param gradeRange The gradeRange to set
	 */
	public void setGradeRange(String gradeRange) {
		this.gradeRange = gradeRange;
	}

	/**
	 * @return the studentCounter
	 */
	public int getStudentCounter() {
		return studentCounter;
	}

	/**
	 * @param studentCounter The studentCounter to set
	 */
	public void setStudentCounter(int studentCounter) {
		this.studentCounter = studentCounter;
	}

	/**
	 * @return result The hashCode of GradeDistribution object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gradeRange == null) ? 0 : gradeRange.hashCode());
		result = prime * result + sid;
		result = prime * result + studentCounter;
		return result;
	}

	/**
	 * @return true If object is this GradeDistribution, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GradeDistribution other = (GradeDistribution) obj;
		if (gradeRange == null) {
			if (other.gradeRange != null)
				return false;
		} else if (!gradeRange.equals(other.gradeRange))
			return false;
		if (sid != other.sid)
			return false;
		if (studentCounter != other.studentCounter)
			return false;
		return true;
	}
}
//End of GradeDistribution class