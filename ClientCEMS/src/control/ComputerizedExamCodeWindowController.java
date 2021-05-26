package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import logic.Exam;
import logic.Message;

/**
 * This is controller class (boundary) for window ComputerizedExamCode in
 * Student. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Sharon Vaknin
 * @author Bat-El Gardin
 * @version May 2021
 */

public class ComputerizedExamCodeWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * static instance for Exam object. Will be create only once for each
	 * computerized exam. the object initialize by the info that return from DB.
	 */
	public static Exam compExam;

	/**
	 * The code that is entered.
	 */
	public static String code;

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private TextField txtCode;
	@FXML
	private Label lblErrCode;
	@FXML
	private Button btnNext;

	// Instance methods ************************************************

	/**
	 * @return the code from window.
	 */
	private String getCode() {
		return txtCode.getText();
	}

	/**
	 * This is FXML event handler. Handles the action of press on enter key.
	 *
	 * @param event The action event.
	 */
	@FXML
	void inputPass(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			btnNext.fire();
	}

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ComputerizedExamCodeWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Enter Code");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Next' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void nextAction(ActionEvent event) {
		if (getCode().trim().isEmpty())
			lblErrCode.setText("enter code");
		else {
			lblErrCode.setText("");
			Message messageToServer = new Message();
			messageToServer.setMsg(getCode() + " computerized");
			messageToServer.setOperation("CheckCodeExists");
			messageToServer.setControllerName("ExamController");
			compExam = (Exam) ClientUI.client.handleMessageFromClientUI(messageToServer);
			// code isn't exists
			if (compExam == null)
				lblErrCode.setText("invalid code");
			else {
				lblErrCode.setText("");
				code = getCode();
				// successes pop up
				ComputerizedExamEnterIDWindowController popUp = new ComputerizedExamEnterIDWindowController();
				try {
					popUp.start(new Stage());
					close(event);
				} catch (Exception e) {
					System.out.println("Exception: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * This method close the current stage.
	 */
	private void close(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		code = null;
		compExam = null;
		// set images
		Image img = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img);
	}

}
//End of ComputerizedExamCodeWindowController class