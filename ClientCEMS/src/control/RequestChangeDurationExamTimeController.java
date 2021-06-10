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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Message;
import logic.Request;

/**
 * This is controller class (boundary) for window RequestChangeDurationExamTime
 * in Teacher. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Bat-El Gardin
 * @version June 2021
 */

public class RequestChangeDurationExamTimeController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The origin duration of the exam with the given code.
	 */
	private static double originDur;
	/**
	 * The new duration for the exam that teacher put.
	 */
	private static double newDur;

	@FXML
	private ImageView imgBack;
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
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/RequestChangeExamDurationTimeWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Request Change Duration");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * @return the code from user.
	 */
	private String getCode() {
		return txtExamID.getText();
	}

	/**
	 * @return the duration from window.
	 */
	private String getDuration() {
		return txtNewDuration.getText();
	}

	/**
	 * @return the explanation from window.
	 */
	private String getExplanation() {
		return txtAreaExplanation.getText();
	}

	/**
	 * This method clear error label.
	 */
	private void clearErrLbl(Label err) {
		err.setText("");
	}

	/**
	 * This method check if duration is valid.
	 * 
	 * @return true if duration is valid, false otherwise.
	 */
	private boolean durationIsValid() {
		String durationString = getDuration();
		try {
			newDur = Double.parseDouble(durationString);
			// check if isn't shorter than the original duration
			Message messageToServer = new Message();
			messageToServer.setMsg(getCode());
			messageToServer.setOperation("GetExamDuration");
			messageToServer.setControllerName("ExamController");
			originDur = (double) ClientUI.client.handleMessageFromClientUI(messageToServer);
			// code isn't exists
			if (originDur > newDur)
				return false;
			// check if score is valid number
			else if (newDur < 0.1)
				return false;
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
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
	 * This is FXML event handler. Handles the action of click on 'Request' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void requestActionButton(ActionEvent event) {
		Message messageToServer = new Message();
		// handle missing fields
		if (getCode().trim().isEmpty() || getDuration().trim().isEmpty() || getExplanation().trim().isEmpty()) {
			// no code
			if (getCode().trim().isEmpty())
				lblErrorExamID.setText("enter code");
			// there is code
			else
				clearErrLbl(lblErrorExamID);
			// no duration
			if (getDuration().trim().isEmpty())
				lblErrorDuration.setText("enter duration");
			// there is duration
			else
				clearErrLbl(lblErrorDuration);
			// no explanation
			if (getExplanation().trim().isEmpty())
				lblErrorExplanation.setText("enter explanation");
			// there is explanation
			else
				clearErrLbl(lblErrorExplanation);
		}
		// duration isn't valid
		else if (!durationIsValid()) {
			clearErrLbl(lblErrorExamID);
			clearErrLbl(lblErrorExplanation);
			lblErrorDuration.setText("invalid duration");
		} else {
			clearErrLbl(lblErrorExamID);
			clearErrLbl(lblErrorDuration);
			clearErrLbl(lblErrorExplanation);
			// check if code exists in DB (table examToPerform)
			boolean isExists;
			messageToServer.setMsg(getCode());
			messageToServer.setOperation("CheckCodeExistsForRequest");
			messageToServer.setControllerName("ExamController");
			isExists = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
			// code isn't exists
			if (!isExists)
				lblErrorExamID.setText("invalid code");
			// check if code already exists in the request table
			boolean requestIsExists = false;
			messageToServer.setMsg(getCode());
			messageToServer.setOperation("CheckRequestExists");
			messageToServer.setControllerName("PrincipalController");
			requestIsExists = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
			// code isn't valid
			if (requestIsExists)
				lblErrorExamID.setText("request already exists");
			// code exists - all fields are good!
			else {
				clearErrLbl(lblErrorExamID);
				clearErrLbl(lblErrorDuration);
				clearErrLbl(lblErrorExplanation);
				boolean success;
				Request newReq = new Request(getCode(), originDur, newDur, getExplanation());
				messageToServer.setMsg(newReq);
				messageToServer.setOperation("AddRequest");
				messageToServer.setControllerName("PrincipalController");
				success = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
				if (success) {
					// success pop up
					RequestSendSuccessfullyWindowController popUp = new RequestSendSuccessfullyWindowController();
					try {
						popUp.start(new Stage());
					} catch (Exception e) {
						UsefulMethods.instance().printException(e);
					}
				} else {
					UsefulMethods.instance().display("error in request change duration time !");
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
//End of RequestChangeDurationExamTimeController class