package control;

import java.net.URL;
import java.util.ResourceBundle;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is controller class (boundary) for window ComputerizedExam (end) in
 * Student. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @version May 2021
 */

public class ComputerizedExamFormEndController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private TextArea question;
	@FXML
	private Label lblNumQuestion;
	@FXML
	private Label lblError;
    @FXML
    private Label lblScore;
    @FXML
    private RadioButton btnAnswer1;
    @FXML
    private RadioButton btnAnswer2;
    @FXML
    private RadioButton btnAnswer3;
    @FXML
    private RadioButton btnAnswer4;

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
	 * This is FXML event handler. Handles the action of click on 'Finish' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void finishAction(ActionEvent event) {
//		// successes pop up
//		SubmissionAgreementWindowController popUp = new SubmissionAgreementWindowController();
//		try {
//			popUp.start(new Stage());
//		} catch (Exception e) {
//		}
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

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
	}

}
//End of ComputerizedExamFormEndController class