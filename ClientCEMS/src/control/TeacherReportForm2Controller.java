package control;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is controller class (boundary) for window TeacherReport (second part).
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Ilan Meikler
 * @author Moran Davidov
 * @version June 2021
 */

public class TeacherReportForm2Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgRep;
	@FXML
	private ImageView imgLogo;
	@FXML
	private Label lblAvgTitle;
	@FXML
	private Label lblAvg;
	@FXML
	private Label lblMedTitle;
	@FXML
	private Label lblMed;
	@FXML
	private BarChart<String, Integer> graph;;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'back' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void back(ActionEvent event) {
		Navigator.instance().back();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Close' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void closeAction(ActionEvent event) {
		Navigator.instance().clearHistory("TeacherHomeForm");
	}

	/**
	 * This method set the report to show on the screen.
	 *
	 * @param li   The list of grades.
	 * @param hist The indication to show the histogram distribution.
	 * @param avg  The indication to show the average.
	 * @param med  The indication to show the median.
	 */
	public void setReport(List<Integer> li, boolean hist, boolean avg, boolean med) {
		int histCnt[] = new int[9];
		int sum = 0;
		lblAvgTitle.setVisible(avg);
		lblMedTitle.setVisible(med);
		graph.setVisible(hist);

		if (med) {
			Collections.sort(li);
			lblMed.setText(li.get(li.size() / 2) + "");
		}
		for (Integer grade : li) {
			sum += grade;
			if (grade < 55)
				histCnt[0]++;
			else if (grade < 65)
				histCnt[1]++;
			else if (grade < 70)
				histCnt[2]++;
			else if (grade < 75)
				histCnt[3]++;
			else if (grade < 80)
				histCnt[4]++;
			else if (grade < 85)
				histCnt[5]++;
			else if (grade < 90)
				histCnt[6]++;
			else if (grade < 95)
				histCnt[7]++;
			else
				histCnt[8]++;
		}
		if (avg)
			lblAvg.setText(String.format("%.2f", (double) sum / li.size()));
		if (hist) {
			XYChart.Series<String, Integer> seriesToAdd = new XYChart.Series<String, Integer>();
			for (int i = 0; i < 9; i++) {

				seriesToAdd.getData()
						.add(new XYChart.Data<String, Integer>(names[i] + "\n  [" + histCnt[i] + "]", histCnt[i]));

			}
			graph.getData().add(seriesToAdd);
		}
	}

	static final String[] names = { "0-54", "55-64", "65-69", "70-74", "75-79", "80-84", "85-89", "90-94", "95-100" };

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().clearHistory("TeacherHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write Question'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeQuestionAction(ActionEvent event) {
		Navigator.instance().navigate("WriteQuestionForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write an Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeExamAction(ActionEvent event) {
		Navigator.instance().navigate("WriteAnExamForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Get Report'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void getReportAction(ActionEvent event) {
		Navigator.instance().navigate("TeacherReportForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Check Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void checkExamAction(ActionEvent event) {
		Navigator.instance().navigate("checkExamForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Exam Stock'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void examSearchAction(ActionEvent event) {
		Navigator.instance().navigate("ExamStockForm1");
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set images
		Image img1 = new Image(this.getClass().getResource("frameWriteQuestion2.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("report.png").toString());
		imgRep.setImage(img3);
		// set the style of the graph
		graph.getStylesheets().add(Navigator.class.getResource("style.css").toString());
		graph.setId("teacher");
	}
}
//End of TeacherReportForm2Controller class