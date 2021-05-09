package logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is an entity class that save details about each field of Study that is
 * created. Implements Serilizable to save the state of FieldofStudy object and
 * re-create it as needed.
 *
 * @author Moran Davidov
 * @version May 2021
 */

@SuppressWarnings("serial")
public class FieldofStudy implements Serializable {

	// Instance variables **********************************************

	private String fid;
	private String fname;
	private ArrayList<Course> courses = new ArrayList<>();

	// Constructors ****************************************************

	public FieldofStudy() {

	}

	// Instance methods ************************************************

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
	 * @return the courses
	 */
	public ArrayList<Course> getCourses() {
		return courses;
	}

	/**
	 * @param courses The courses to set
	 */
	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}

	/**
	 * @return result The hashCode of FieldofStuday object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courses == null) ? 0 : courses.hashCode());
		result = prime * result + ((fid == null) ? 0 : fid.hashCode());
		result = prime * result + ((fname == null) ? 0 : fname.hashCode());
		return result;
	}

	/**
	 * @return true If object is this FieldofStuday, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldofStudy other = (FieldofStudy) obj;
		if (courses == null) {
			if (other.courses != null)
				return false;
		} else if (!courses.equals(other.courses))
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

}
//End of FieldofStuday class