package logic;

import java.io.Serializable;

/**
 * This is an enum class that defines the type of reports. Implements
 * Serilizable to save the state of ReportType object and re-create it as
 * needed.
 *
 * @author Moran Davidov
 * @version May 2021
 */
public enum ReportType implements Serializable {
	COURSE, TEACHER, STUDENT;
}
//End of ReportType class
