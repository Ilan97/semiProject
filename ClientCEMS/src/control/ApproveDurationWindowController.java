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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Message;
import logic.Request;

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
	 * True if request was handled, false otherwise.
	 */
	public boolean hasHandle;
	/**
	 * The chosen request from list.
	 */
	private Request req = PrincipalViewRequestFormController.chosenReq;

	@FXML
	private ImageView imgBack;
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
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ApproveDurationWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("View Request");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Approve' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void approveActionButton(ActionEvent event) {
		// change the duration
		boolean res = false;
		Message messageToServer = new Message();
		messageToServer.setMsg(req.getExamID() + " " + req.getNewDur());
		messageToServer.setOperation("updateExamDurationTime");
		messageToServer.setControllerName("ExamController");
		res = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
		if (!res) {
			// fail pop up
			FailWindowController popUp = new FailWindowController();
			try {
				popUp.start(new Stage());
			} catch (Exception e) {
				UsefulMethods.instance().printExeption(e);
			}
		} else
			deleteRequest();
		UsefulMethods.instance().close(event);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Disapprove'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void disapproveActionButton(ActionEvent event) {
		deleteRequest();
		UsefulMethods.instance().close(event);
	}

	/**
	 * This method ask from server to delete the request from the table.
	 *
	 */
	private void deleteRequest() {
		boolean res = false;
		Message messageToServer = new Message();
		messageToServer.setMsg(req.getEcode());
		messageToServer.setOperation("DeleteRequest");
		messageToServer.setControllerName("PrincipalController");
		res = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
		if (res) {
			// success pop up
			TheRequestWasHandledSuccessfullyWindowController popUp = new TheRequestWasHandledSuccessfullyWindowController();
			try {
				popUp.start(new Stage());
			} catch (Exception e) {
				UsefulMethods.instance().printExeption(e);
			}
		} else {
			// fail pop up
			FailWindowController popUp = new FailWindowController();
			try {
				popUp.start(new Stage());
			} catch (Exception e) {
				UsefulMethods.instance().printExeption(e);
			}
		}
	}

	/**
	 * This method shows request details in the window.
	 */
	private void showRequestDetails() {
		lblExamID.setText(req.getExamID());
		lblDuration.setText(String.valueOf(req.getOldDur()));
		lblNewDuration.setText(String.valueOf(req.getNewDur()));
		txtAreaExplanation.setText(req.getExplanation());
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showRequestDetails();
		// set images
		Image img = new Image(this.getClass().getResource("principalFrame.PNG").toString());
		imgBack.setImage(img);
	}
}
//End of ApproveDurationWindowController class