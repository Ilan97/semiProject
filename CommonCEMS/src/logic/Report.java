package logic;

import java.io.Serializable;

/**
 * This is an entity class that save details about each report that is
 * created. Implements Serilizable to save the state of Report object and
 * re-create it as needed.
 *
 * @author Moran Davidov
 * @version May 2021
 */

@SuppressWarnings("serial")
public class Report implements Serializable{

	// Instance variables **********************************************
	private int sid;
	private int rid;
	private float average;
	private int median;
	private GradeDistribution gradeDistribution;
	


	// Constructors ****************************************************
	public Report() {
	}

	// Instance methods ************************************************
	
	/**
	 * @return the gradeDistribution
	 */
	public GradeDistribution getGradeDistribution() {
		return gradeDistribution;
	}

	public void setGradeDistribution(GradeDistribution gradeDistribution) {
		this.gradeDistribution = gradeDistribution;
	}
	
	/**
	 * @return the sid
	 */
	public int getSid() {
		return sid;
	}

	/**
	 * @return the sid
	 */
	public void setSid(int sid) {
		this.sid = sid;
	}

	/**
	 * @return the rid
	 */
	public int getRid() {
		return rid;
	}

	/**
	 * @param rid The rid to set
	 */
	public void setRid(int rid) {
		this.rid = rid;
	}

	/**
	 * @return the average
	 */
	public float getAverage() {
		return average;
	}

	/**
	 * @param average The average to set
	 */
	public void setAverage(float average) {
		this.average = average;
	}

	/**
	 * @return the median
	 */
	public int getMedian() {
		return median;
	}

	/**
	 * @param median The median to set
	 */
	public void setMedian(int median) {
		this.median = median;
	}

	/**
	 * @return result The hashCode of Report object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(average);
		result = prime * result + ((gradeDistribution == null) ? 0 : gradeDistribution.hashCode());
		result = prime * result + median;
		result = prime * result + rid;
		result = prime * result + sid;
		return result;
	}
	
	/**
	 * @return true If object is this Report, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Report other = (Report) obj;
		if (Float.floatToIntBits(average) != Float.floatToIntBits(other.average))
			return false;
		if (gradeDistribution == null) {
			if (other.gradeDistribution != null)
				return false;
		} else if (!gradeDistribution.equals(other.gradeDistribution))
			return false;
		if (median != other.median)
			return false;
		if (rid != other.rid)
			return false;
		if (sid != other.sid)
			return false;
		return true;
	}
}
//End of Report class