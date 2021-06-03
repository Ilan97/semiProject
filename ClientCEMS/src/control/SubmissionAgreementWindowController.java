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

	/**
	 * The controller that called this window.
	 */
	private String callerController;

    @FXML
    public Button submitbtn;
	@FXML
	private ImageView imgBack;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException
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
		UsefulMethods.instance().close(event);
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
		double dur = 0.0;
		// message to server
		Message messageToServer = new Message();
		switch (callerController) {
		case "ManualExamCodeWindowController":
			messageToServer.setMsg(ManualExamFormController.examToUpload);
			dur = ManualExamFormController.examToUpload.getRealTimeDuration();
			break;
		case "ComputerizedExamInnerFormController":
			messageToServer.setMsg(ComputerizedExamFormController.examToSubmit);
			dur = ComputerizedExamFormController.examToSubmit.getRealTimeDuration();
			break;
		}
		messageToServer.setControllerName("StudentController");
		messageToServer.setOperation("SubmitExam");
		res = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
		UsefulMethods.instance().close(event);
		// upload success
		if (res && dur != -1) {
			TheSubmissionWasSuccessfulWindowController success = new TheSubmissionWasSuccessfulWindowController();
			success.start(new Stage());
		} else if (dur == -1) {
			StudentDidNotMakeItWindowController pop = new StudentDidNotMakeItWindowController();
			pop.start(new Stage());
		} else {
			// Failed pop up
			FailWindowController popUp = new FailWindowController();
			try {
				popUp.start(new Stage());
			} catch (Exception e) {
				UsefulMethods.instance().printException(e);
			}
		}
		Navigator.instance().clearHistory("StudentHomeForm");
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