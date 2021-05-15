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
 * This is controller class (boundary) for window AddNote in Teacher. This class
 * handle all events related to this window. This class connect with client.
 *
 * @author
 * @version May 2021
 */

public class AddNoteWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private Button btnAddScore;
	@FXML
	private Button btnCancel;
	@FXML
	private Label lblError;
	@FXML
	private TextArea txtTechNote;
	@FXML
	private TextArea txtStudNote;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Add' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void addNoteActionButton(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Cancel' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void cancelActionButton(ActionEvent event) {

	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("teacherFrame.PNG").toString());
		imgBack.setImage(img);
	}

}
//End of QuestionScoreWindowController class