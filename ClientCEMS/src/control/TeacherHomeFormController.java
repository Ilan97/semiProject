package control;

import java.net.URL;
import java.util.ResourceBundle;
import client.ClientUI;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Message;

/**
 * This is controller class (boundary) for window TeacherHome. This class handle
 * all events related to this window. This class connect with client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @version May 2021
 */

public class TeacherHomeFormController implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private Label lblName;

	// Instance methods ************************************************

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
	 * This is FXML event handler. Handles the action of click on 'Exam Settings'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void changeSettings(ActionEvent event) {
		ExamSettingsWindowController popUp = new ExamSettingsWindowController();
		try {
			popUp.start(new Stage());
		} catch (Exception e) {
			UsefulMethods.instance().printException(e);
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Check Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void checkExamAction(ActionEvent event) {
		Navigator.instance().navigate("CheckExamForm");
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
	 * This is FXML event handler. Handles the action of click on 'Log out' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void logoutAction(ActionEvent event) {
		Message messageToServer = new Message();
		messageToServer.setMsg(LoginController.user.getUsername());
		messageToServer.setOperation("updateConnectionStatus");
		messageToServer.setControllerName("UserController");
		ClientUI.client.handleMessageFromClientUI(messageToServer);
		LoginController.user = null;
		Navigator.instance().clearHistory();
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// user's name
		lblName.setText("Welcome " + LoginController.user.getFirstName() + " " + LoginController.user.getLastName());
		// set images
		Image img1 = new Image(this.getClass().getResource("teacherHomeForm.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
	}
}
//End of TeacherHomeFormController class