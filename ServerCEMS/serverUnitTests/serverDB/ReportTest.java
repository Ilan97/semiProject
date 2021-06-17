package serverDB;

/**
 * This is unitTest class for the server side. These test are checking the functionality of report action.
 *
 * @author Ilan Meikler
 * @author Bat-El Gardin
 * @author Ohad Shamir
 * @version June 2021
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import control.CourseController;
import control.DBconnector;
import logic.Message;

class ReportTest {

	// Variables ************************************************

	/**
	 * This is the grades list that returned from the DB.
	 */
	ArrayList<Integer> grades = new ArrayList<>();

	/**
	 * This class is set up the variables we are going to test.
	 */
	@BeforeEach
	void setUp() throws Exception {
		DBconnector.connectToDB();
		grades.add(22);
	}

	// Tests ************************************************

	// checking getting the right grades list from DB.
	// input: course name: "Hedva2".
	// expected: grades list.
	@SuppressWarnings("unchecked")
	@Test
	void getGradesListTest() {
		Message msg = new Message();
		msg.setMsg("Hedva2");
		msg.setOperation("GetGradeList");
		assertEquals(grades, (ArrayList<Integer>) (CourseController.handleRequest(msg).getMsg()));
	}
}
//End of ReportTest class