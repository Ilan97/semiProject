package control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is controller class (boundary) for window SubmissionAgreement in
 * Student. This class handle all events related to this windows. This class
 * connect with client.
 *
 * @author
 * @version May 2021
 */

public class SubmissionAgreementWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Cancel' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void cancelActionButton(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Submit' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void submitActionButton(ActionEvent event) {

	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image img = new Image(this.getClass().getResource("serverCrushed.PNG").toString());
		imgBack.setImage(img);
	}

}
//End of SubmissionAgreementWindowController class