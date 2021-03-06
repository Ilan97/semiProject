package logic;

import java.io.Serializable;

/**
 * This is an enum class that defines the exam's optional type that it can have.
 * Implements Serializable to save the state of ExamType object and re-create it
 * as needed.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version April 2021
 */

public enum ExamType implements Serializable {
	MANUAL {
		public String toString() {
			return "manual";
		}
	},
	COMPUTERIZED {
		public String toString() {
			return "computerized";
		}
	};

}
//End of ExamType class