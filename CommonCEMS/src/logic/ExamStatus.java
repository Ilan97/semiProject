package logic;

import java.io.Serializable;

/**
 * This is an enum class that defines the exam's optional status that it can be.
 * Implements Serilizable to save the state of ExamStatus object and re-create
 * it as needed.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version April 2021
 */

public enum ExamStatus implements Serializable {
	LOCKED {
		public String toString() {
			return "manual";
		}
	},
	OPEN {
		public String toString() {
			return "open";
		}
	},
	DONE {
		public String toString() {
			return "done";
		}
	};
}
//End of ExamStatus class