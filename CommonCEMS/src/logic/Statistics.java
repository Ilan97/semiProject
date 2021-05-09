package logic;

import java.io.Serializable;

/**
 * This is an entity class that save details about each statistics that is
 * created. Implements Serilizable to save the state of Statistics object and
 * re-create it as needed.
 *
 * @author Moran Davidov
 * @version May 2021
 */

@SuppressWarnings("serial")
public class Statistics implements Serializable {

	// Instance variables **********************************************

	private int sid;
	private int eid;
	private int fid;
	private int cid;
	private float average;
	private double median;
	private int numFinished;
	private int numStarted;
	private int numNotFinished;

	// Constructors ****************************************************

	public Statistics() {
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
	 * @return the eid
	 */
	public int getEid() {
		return eid;
	}

	/**
	 * @param eid The eid to set
	 */
	public void setEid(int eid) {
		this.eid = eid;
	}

	/**
	 * @return the fid
	 */
	public int getFid() {
		return fid;
	}

	/**
	 * @param fid The fid to set
	 */
	public void setFid(int fid) {
		this.fid = fid;
	}

	/**
	 * @return the cid
	 */
	public int getCid() {
		return cid;
	}

	/**
	 * @param cid The cid to set
	 */
	public void setCid(int cid) {
		this.cid = cid;
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
	public double getMedian() {
		return median;
	}

	/**
	 * @param median The median to set
	 */
	public void setMedian(double median) {
		this.median = median;
	}

	/**
	 * @return the numFinished
	 */
	public int getNumFinished() {
		return numFinished;
	}

	/**
	 * @param numFinished The numFinished to set
	 */
	public void setNumFinished(int numFinished) {
		this.numFinished = numFinished;
	}

	/**
	 * @return the numStarted
	 */
	public int getNumStarted() {
		return numStarted;
	}

	/**
	 * @param numStarted The numStarted to set
	 */
	public void setNumStarted(int numStarted) {
		this.numStarted = numStarted;
	}

	/**
	 * @return the numNotFinished
	 */
	public int getNumNotFinished() {
		return numNotFinished;
	}

	/**
	 * @param setNumNotFinished The setNumNotFinished to set
	 */
	public void setNumNotFinished(int numNotFinished) {
		this.numNotFinished = numNotFinished;
	}

	/**
	 * @return result The hashCode of Statistics object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(average);
		result = prime * result + cid;
		result = prime * result + eid;
		result = prime * result + fid;
		long temp;
		temp = Double.doubleToLongBits(median);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + numFinished;
		result = prime * result + numNotFinished;
		result = prime * result + numStarted;
		result = prime * result + sid;
		return result;
	}

	/**
	 * @return true If object is this Statistics, false otherwise
	 */

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Statistics other = (Statistics) obj;
		if (Float.floatToIntBits(average) != Float.floatToIntBits(other.average))
			return false;
		if (cid != other.cid)
			return false;
		if (eid != other.eid)
			return false;
		if (fid != other.fid)
			return false;
		if (Double.doubleToLongBits(median) != Double.doubleToLongBits(other.median))
			return false;
		if (numFinished != other.numFinished)
			return false;
		if (numNotFinished != other.numNotFinished)
			return false;
		if (numStarted != other.numStarted)
			return false;
		if (sid != other.sid)
			return false;
		return true;
	}

}
//End of Statistics class