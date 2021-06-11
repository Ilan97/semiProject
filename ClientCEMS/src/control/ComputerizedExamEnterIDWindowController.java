package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import client.ClientUI;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Message;

/**
 * This is controller class (boundary) for window ComputerizedExamEnterID in
 * Student. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Ilan Meikler
 * @author Bat-El Gardin
 * @version May 2021
 */

public class ComputerizedExamEnterIDWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgGoodLuck;
	@FXML
	private TextField txtID;
	@FXML
	private Label lblErrID;
	@FXML
	private Button btnStart;
	@FXML
	private Text lblDur;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException if an I/O error occurs when opening.
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ComputerizedExamEnterIDWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Enter ID");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * @return the ID from user.
	 */
	private String getID() {
		return txtID.getText();
	}

	/**
	 * This is FXML event handler. Handles the action of press on enter key.
	 *
	 * @param event The action event - user pressed on 'Enter' key.
	 */
	@FXML
	void inputPass(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			btnStart.fire();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Start' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void startAction(ActionEvent event) {
		if (getID().isEmpty())
			lblErrID.setText("enter ID");
		// code isn't exists
		else if (!LoginController.user.getUid().equals(getID()))
			lblErrID.setText("invalid ID");
		else {
			lblErrID.setText("");
			// start the timer
			Message messageToServer = new Message();
			messageToServer.setControllerName("StudentController");
			messageToServer.setOperation("StartTimer");
			ClientUI.client.handleMessageFromClientUI(messageToServer);
			UsefulMethods.instance().close(event);
			Navigator.instance().clearHistory("ComputerizedExamForm");
		}
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// get the initial exam duration
		Message messageToServer = new Message();
		messageToServer.setControllerName("ExamController");
		messageToServer.setOperation("GetExamDuration");
		messageToServer.setMsg(ComputerizedExamCodeWindowController.code);
		double duration = (double) ClientUI.client.handleMessageFromClientUI(messageToServer);
		lblDur.setText("You have " + (int) duration + " minutes");
		// set images
		Image img = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img);
		Image img1 = new Image(this.getClass().getResource("smile.png").toString());
		imgGoodLuck.setImage(img1);
	}
}
//End of ComputerizedExamEnterIDWindowController class