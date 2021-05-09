package control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is controller class (boundary) for window ComputerizedExam (middle) in
 * Student. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author
 * @version May 2021
 */

public class ComputerizedExamFormMiddleController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private Label lblNumQuestion;
	@FXML
	private TextArea question;
	@FXML
	private Label lblError;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'back' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void back(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'next' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void next(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Answer 1' radio
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseAns1(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Answer 2' radio
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseAns2(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Answer 3' radio
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseAns3(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Answer 4' radio
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseAns4(ActionEvent event) {

	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Computerized
	 * Exam' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void compExamAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Manual Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void manualExamAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Grades' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void gradesAction(ActionEvent event) {

	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image img = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
	}

}
//End of ComputerizedExamFormMiddleController class
