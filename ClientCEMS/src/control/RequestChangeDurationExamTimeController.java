package control;

import java.net.URL;
import java.util.ResourceBundle;

import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is controller class (boundary) for window RequestChangeDurationExamTime
 * in Teacher. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author
 * @version May 2021
 */

public class RequestChangeDurationExamTimeController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private Button btnRequest;
	@FXML
	private Label lblErrorPort;
	@FXML
	private Button btnCancel;
	@FXML
	private Label lblErrorPort21;
	@FXML
	private TextArea txtAreaExplanation;
	@FXML
	private TextField txtExamID;
	@FXML
	private TextField txtNewDuration;
	@FXML
	private Label lblErrorExamID;
	@FXML
	private Label lblErrorDuration;
	@FXML
	private Label lblErrorExplanation;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Cancel' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void cancelActionButton(ActionEvent event) {
		Navigator.instance().clearHistory("TeacherHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Request' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void requestActionButton(ActionEvent event) {
		Navigator.instance().clearHistory("TeacherHomeForm");
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set images
		Image img = new Image(this.getClass().getResource("teacherFrame.PNG").toString());
		imgBack.setImage(img);
	}

}
//End of RequestChangeDurationExamTimeController class