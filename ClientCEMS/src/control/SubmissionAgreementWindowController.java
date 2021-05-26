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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Message;

/**
 * This is controller class (boundary) for window SubmissionAgreement in
 * Student. This class handle all events related to this windows. This class
 * connect with client.
 *
 * @author Ilan Meikler
 * @author Bat-El Gardin
 * @version May 2021
 */

public class SubmissionAgreementWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	private String callerController;
	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 */
	public void start(Stage primaryStage, String controllerName) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/SubmissionAgreementWindow.fxml"));
		Parent root = loader.load();
		SubmissionAgreementWindowController cont = loader.getController();
		cont.callerController = controllerName;
		Scene scene = new Scene(root);
		primaryStage.setTitle("Are you Sure?");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Cancel' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void cancelActionButton(ActionEvent event) {
		close(event);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Submit' button.
	 *
	 * @param event The action event.
	 * @throws IOException
	 */
	@FXML
	void submitActionButton(ActionEvent event) throws IOException {
		boolean res;
		// message to server
		Message messageToServer = new Message();
		switch (callerController) {
		case "ManualExamCodeWindowController":
			messageToServer.setMsg(ManualExamFormController.examToUpload);
			break;
		case "ComputerizedExamInnerFormController":
			messageToServer.setMsg(ComputerizedExamFormController.examToSubmit);
			break;
		}
		messageToServer.setControllerName("StudentController");
		messageToServer.setOperation("SubmitExam");
		res = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
		close(event);
		// upload success
		if (res) {
			TheSubmissionWasSuccessfulWindowController success = new TheSubmissionWasSuccessfulWindowController();
			success.start(new Stage());
		}
		// upload fail
		else
			;
		Navigator.instance().clearHistory("StudentHomeForm");
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
		// set images
		Image img = new Image(this.getClass().getResource("serverCrushed.PNG").toString());
		imgBack.setImage(img);
	}
}
//End of SubmissionAgreementWindowController class