package control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is controller class (boundary) for window TheSubmissionWasSuccessful.
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author
 * @version May 2021
 */

public class TheSubmissionWasSuccessfulWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgOk;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Ok' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void okAction(ActionEvent event) {

	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image img1 = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("ok.png").toString());
		imgOk.setImage(img2);
	}

}
// End of TheSubmissionWasSuccessfulWindowController class