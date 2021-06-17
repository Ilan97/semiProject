package clientGui;

/**
 * This is unitTest class for the client side. These test are checking the functionality of report action.
 *
 * @author Ilan Meikler
 * @author Bat-El Gardin
 * @author Ohad Shamir
 * @version June 2021
 */

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import control.PrincipalReportForm1Controller;
import control.PrincipalReportForm2Controller;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;

class ReportTest {

	// Variables ************************************************

	/**
	 * This variable is declared for JavaFX to run.
	 */
	@SuppressWarnings("unused")
	private JFXPanel panel = new JFXPanel();
	/**
	 * This is the grades list.
	 */
	private List<Integer> ret;
	/**
	 * This array is illustrates the graph that on the screen.
	 */
	private int[] histCnt;
	/**
	 * This is the first controller we are testing.
	 */
	PrincipalReportForm1Controller rep1 = new PrincipalReportForm1Controller();
	/**
	 * This is the second controller we are testing.
	 */
	PrincipalReportForm2Controller rep2 = new PrincipalReportForm2Controller();

	/**
	 * This class is set up the variables we are going to test.
	 */
	@BeforeEach
	void setUp() throws Exception {
		ret = new ArrayList<Integer>();
		ret.add(80);
		ret.add(80);
		ret.add(90);
		ret.add(100);
		ret.add(100);
		histCnt = new int[9];
		histCnt[5] = 2;
		histCnt[7] = 1;
		histCnt[8] = 2;
		rep2.lblAvg = new Label();
		rep2.lblMed = new Label();
		rep2.lblAvgTitle = new Label();
		rep2.lblMedTitle = new Label();
	}

	// Tests ************************************************

	// checking all options of statistics (average, median, histogram) are shown on
	// screen.
	// input: all options are selected.
	// expected: labels: avg = "90.00", med = "90", histCnt with the appropriate
	// values.
	@Test
	void allStatisticsShownInReportTest() {
		String avg = "90.00";
		String med = "90";
		rep2.setReport(ret, false, true, true);
		assertEquals(avg, rep2.lblAvg.getText());
		assertEquals(med, rep2.lblMed.getText());
		assertArrayEquals(histCnt, rep2.histCnt);
	}

	// checking only average is shown on screen.
	// input: only average is selected.
	// expected: labels: avg = "90.00", med = "", histCnt with the appropriate
	// values.
	@Test
	void onlyAvgShownInReportTest() {
		String avg = "90.00";
		String med = "";
		rep2.setReport(ret, false, true, false);
		assertEquals(avg, rep2.lblAvg.getText());
		assertEquals(med, rep2.lblMed.getText());
		assertArrayEquals(histCnt, rep2.histCnt);
	}

	// checking only median is shown on screen.
	// input: only median is selected.
	// expected: labels: avg = "", med = "90", histCnt with the appropriate values.
	@Test
	void onlyMedShownInReportTest() {
		String avg = "";
		String med = "90";
		rep2.setReport(ret, false, false, true);
		assertEquals(avg, rep2.lblAvg.getText());
		assertEquals(med, rep2.lblMed.getText());
		assertArrayEquals(histCnt, rep2.histCnt);
	}

	// checking only histogram is shown on screen.
	// input: only histogram selected.
	// expected: labels: avg = "", med = "", histCnt with the appropriate values.
	@Test
	void onlyHistShownInReportTest() {
		String avg = "";
		String med = "";
		rep2.setReport(ret, false, false, false);
		assertEquals(avg, rep2.lblAvg.getText());
		assertEquals(med, rep2.lblMed.getText());
		assertArrayEquals(histCnt, rep2.histCnt);
	}

	// checking median and histogram are shown on screen.
	// input: average isn't selected.
	// expected: labels: avg = "", med = "90", histCnt with the appropriate values.
	@Test
	void medAndHistShownInReportTest() {
		String avg = "";
		String med = "90";
		rep2.setReport(ret, false, false, true);
		assertEquals(avg, rep2.lblAvg.getText());
		assertEquals(med, rep2.lblMed.getText());
		assertArrayEquals(histCnt, rep2.histCnt);
	}

	// checking average and histogram are shown on screen.
	// input: median isn't selected.
	// expected: labels: avg = "90.00", med = "", histCnt with the appropriate
	// values.
	@Test
	void avgAndHistShownInReportTest() {
		String avg = "90.00";
		String med = "";
		rep2.setReport(ret, false, true, false);
		assertEquals(avg, rep2.lblAvg.getText());
		assertEquals(med, rep2.lblMed.getText());
		assertArrayEquals(histCnt, rep2.histCnt);
	}
}
//End of ReportTest class