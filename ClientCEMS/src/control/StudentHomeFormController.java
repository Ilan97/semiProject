package control;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Message;

/**
 * This is controller class (boundary) for window StudentHome. This class handle
 * all events related to this window. This class connect with client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @version May 2021
 */

public class StudentHomeFormController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().clearHistory("StudentHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Computerized
	 * Exam' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void compExamAction(ActionEvent event) {
		//successes pop up
		ComputerizedExamCodeWindowController popUp = new ComputerizedExamCodeWindowController();
		try {
			popUp.start(new Stage());
		} catch (Exception e) {
			
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Manual Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void manualExamAction(ActionEvent event) {
		Navigator.instance().navigate("ManualExamForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Grades' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void gradesAction(ActionEvent event) {
		Navigator.instance().navigate("GradesForm");
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
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("studentHomeForm.PNG").toString());
		imgBack.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
	}
}
//End of StudentHomeFormController class