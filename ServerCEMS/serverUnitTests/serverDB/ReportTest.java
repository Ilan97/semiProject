package serverDB;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import control.CourseController;
import control.DBconnector;
import logic.Message;

class ReportTest {
	ArrayList<Integer> grades = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		DBconnector.connectToDB();
		grades.add(0);
		grades.add(100);
		grades.add(0);
	}

	@SuppressWarnings("unchecked")
	@Test
	void getGreadsListTest() {
		Message msg = new Message();
		msg.setMsg("Hedva2");
		msg.setOperation("GetGradeList");
		assertEquals(grades, (ArrayList<Integer>) (CourseController.handleRequest(msg).getMsg()));
	}
}