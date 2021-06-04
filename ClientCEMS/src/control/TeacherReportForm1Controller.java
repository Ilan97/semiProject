package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.ClientUI;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.Message;

/**
 * This is controller class (boundary) for window TeacherReport (first part).
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Ilan Meikler
 * @author Moran Davidov
 * @version June 2021
 */

public class TeacherReportForm1Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgRep;
	@FXML
	private ImageView imgLogo;
	@FXML
	private CheckBox clickAvg;
	@FXML
	private CheckBox clickMed;
	@FXML
	private CheckBox clickHist;
	@FXML
	private Label lblErrStat;
	@FXML
	private Label lblErrData;

	// Instance methods ************************************************

	/**
	 * This method clear error label.
	 */
	private void clearErrLbl(Label err) {
		err.setText("");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'next' button.
	 *
	 * @param event The action event.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void next(ActionEvent event) {
		clearErrLbl(lblErrData);
		// handle missing fields
		if (clickAvg.isPressed() || clickMed.isPressed() || clickHist.isPressed())
			lblErrStat.setText("choose statistics");
		// at least one statistics chosen
		else {
			clearErrLbl(lblErrStat);
			Message messageToServer = new Message();
			ArrayList<Integer> list = new ArrayList<Integer>();
			messageToServer.setMsg(LoginController.user.getFirstName() + " " + LoginController.user.getLastName());
			messageToServer.setControllerName("TeacherController");
			messageToServer.setOperation("GetGradeList");
			list = (ArrayList<Integer>) ClientUI.client.handleMessageFromClientUI(messageToServer);
			if (list != null) {
				TeacherReportForm2Controller cont = (TeacherReportForm2Controller) Navigator.instance()
						.navigate("TeacherReportForm2");
				// set the report
				cont.setReport(list, clickHist.isSelected(), clickAvg.isSelected(), clickMed.isSelected());
			} else
				lblErrData.setText("there is no data to show!");
		}
	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().navigate("TeacherHomeForm");
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
		/* TODO Navigator.instance().navigate("checkExamForm"); */
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
		Image img1 = new Image(this.getClass().getResource("frameWriteQuestion1.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("report.png").toString());
		imgRep.setImage(img3);
	}
}
//End of TeacherReportForm1Controller class