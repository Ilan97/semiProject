package control;

import java.net.URL;
import java.util.ResourceBundle;

import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is controller class (boundary) for window TeacherReport (first part).
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Moran Davidov
 * @author Bat-El Gardin
 * @version May 2021
 */

public class TeacherReportForm1Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgRep;
	@FXML
	private ImageView imgLogo;
	@FXML
	private MenuButton chooseField;
	@FXML
	private MenuButton chooseCode;
	@FXML
	private MenuButton chooseCourse;
	@FXML
	private Label lblErrStat;
	@FXML
	private Label lblErrField;
	@FXML
	private Label lblErrCourse;
	@FXML
	private Label lblErrCode;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'next' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void next(ActionEvent event) {
		Navigator.instance().navigate("TeacherReportForm2");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Choose Field'
	 * menu button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseFieldAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Choose Course'
	 * menu button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseCourseAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Choose Exam Code'
	 * menu button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseCodeAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Average' check
	 * box.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseAvgAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Median' check
	 * box.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseMedAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Histogram
	 * Distribution' check box.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseHistAction(ActionEvent event) {

	}

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
	 * This is FXML event handler. Handles the action of click on 'Change Exam
	 * Duration' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void changeDurAction(ActionEvent event) {
		Navigator.instance().navigate("RequestChangeExamDurationTimeWindow");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Check Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void checkExamAction(ActionEvent event) {
		//Navigator.instance().navigate(" ");///????
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
		Image img1 = new Image(this.getClass().getResource("frameWriteQuestion1.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("report.png").toString());
		imgRep.setImage(img3);
	}

}
//End of TeacherReportForm1Controller class