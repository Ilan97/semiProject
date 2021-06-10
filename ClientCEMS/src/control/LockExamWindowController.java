package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import client.ClientUI;
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
import javafx.stage.Stage;
import logic.Message;

/**
 * This is controller class (boundary) for window LockExam in Teacher. This
 * class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @version June 2021
 */

public class LockExamWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private ImageView imgBack;
	@FXML
	private Button btnLock;
	@FXML
	private Label lblErrorExamID;
	@FXML
	private TextField txtExamID;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/LockExamWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Lock Exam");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * This is FXML event handler. Handles the action of press on enter key.
	 *
	 * @param event The action event - user pressed on 'Enter' key.
	 */
	@FXML
	void inputCode(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			btnLock.fire();
	}

	/**
	 * @return the code from user.
	 */
	private String getCode() {
		return txtExamID.getText();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Cancel' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void cancelActionButton(ActionEvent event) {
		UsefulMethods.instance().close(event);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Lock' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void lockActionButton(ActionEvent event) {
		if (getCode().trim().isEmpty())
			lblErrorExamID.setText("enter code");
		// trim isn't empty
		else {
			Message messageToServer = new Message();
			// check if code exists in DB
			boolean isExists;
			messageToServer.setMsg(getCode());
			messageToServer.setOperation("CheckCodeExistsForRequest");
			messageToServer.setControllerName("ExamController");
			isExists = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
			// code isn't exists
			if (!isExists)
				lblErrorExamID.setText("invalid code");
			// code is valid
			else {
				lblErrorExamID.setText("");
				boolean isLocked;
				messageToServer.setMsg(getCode());
				messageToServer.setOperation("LockExam");
				messageToServer.setControllerName("ExamController");
				isLocked = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
				if (isLocked) {
					// success pop up
					ExamLockedSuccessfullyWindowController popUp = new ExamLockedSuccessfullyWindowController();
					try {
						popUp.start(new Stage());
					} catch (Exception e) {
						UsefulMethods.instance().printException(e);
					}
				} else {
					UsefulMethods.instance().display("error in lock!");
				}
				UsefulMethods.instance().close(event);
			}
		}
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
//End of LockExamWindowController class