package control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is controller class (boundary) for window ComputerizedExamCode in
 * Student. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author
 * @version May 2021
 */

public class ComputerizedExamCodeWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private TextField txtCode;
	@FXML
	private TextField txtID;
	@FXML
	private Label lblErrCode;
	@FXML
	private Label lblErrID;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Start' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void start(ActionEvent event) {

	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image img = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img);
	}

}
//End of ComputerizedExamCodeWindowController class