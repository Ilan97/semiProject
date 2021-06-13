package clientGui;

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
	@SuppressWarnings("unused")
	private JFXPanel panel = new JFXPanel();// for javafx to run
	private List<Integer> ret;
	private int[] histCnt;

	PrincipalReportForm2Controller rep2 = new PrincipalReportForm2Controller();
	PrincipalReportForm1Controller rep1 = new PrincipalReportForm1Controller();

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

	@Test
	void allStatisticsShownInReportTest() {
		String avg = "90.00";
		String med = "90";
		rep2.setReport(ret, false, true, true);
		assertEquals(avg, rep2.lblAvg.getText());
		assertEquals(med, rep2.lblMed.getText());
		assertArrayEquals(histCnt, rep2.histCnt);
	}

	@Test
	void onlyAvgShownInReportTest() {
		String avg = "90.00";
		String med = "";
		rep2.setReport(ret, false, true, false);
		assertEquals(avg, rep2.lblAvg.getText());
		assertEquals(med, rep2.lblMed.getText());
		assertArrayEquals(histCnt, rep2.histCnt);
	}

	@Test
	void onlyMedShownInReportTest() {
		String avg = "";
		String med = "90";
		rep2.setReport(ret, false, false, true);
		assertEquals(avg, rep2.lblAvg.getText());
		assertEquals(med, rep2.lblMed.getText());
		assertArrayEquals(histCnt, rep2.histCnt);
	}

	@Test
	void onlyHistShownInReportTest() {
		String avg = "";
		String med = "";
		rep2.setReport(ret, false, false, false);
		assertEquals(avg, rep2.lblAvg.getText());
		assertEquals(med, rep2.lblMed.getText());
		assertArrayEquals(histCnt, rep2.histCnt);
	}

	@Test
	void medAndHistShownInReportTest() {
		String avg = "";
		String med = "90";
		rep2.setReport(ret, false, false, true);
		assertEquals(avg, rep2.lblAvg.getText());
		assertEquals(med, rep2.lblMed.getText());
		assertArrayEquals(histCnt, rep2.histCnt);
	}

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