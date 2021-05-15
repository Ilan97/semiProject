package control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is controller class (boundary) for window ApproveDuration in Principal.
 * This class handle all events related to this windows. This class connect with
 * client.
 *
 * @author
 * @version May 2021
 */

public class ApproveDurationWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private Button btnApprove;
	@FXML
	private Button btnDisapprove;
	@FXML
	private Label lblDuration;
	@FXML
	private TextArea txtAreaExplanation;
	@FXML
	private Label lblNewDuration;
	@FXML
	private Label lblExamID;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Approve' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void approveActionButton(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Disapprove'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void disapproveActionButton(ActionEvent event) {

	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("principalFrame.PNG").toString());
		imgBack.setImage(img);
	}

}
//End of ApproveDurationWindowController class