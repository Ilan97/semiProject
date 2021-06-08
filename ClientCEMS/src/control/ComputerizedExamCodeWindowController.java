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
	 * static instance for {@link Exam} object. Will be create only once for each
	 * computerized exam. the object initialize by the info that return from DB.
	 */
	public static Exam compExam;
	/**
	 * The code that is entered by user.
	 */
	public static String code;

	@FXML
	private ImageView imgBack;
	@FXML
	private TextField txtCode;
	@FXML
	private Label lblErrCode;
	@FXML
	private Button btnNext;
	private Object Object;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ComputerizedExamCodeWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Enter Code");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * @return the code from user.
	 */
	private String getCode() {
		return txtCode.getText();
	}

	/**
	 * This is FXML event handler. Handles the action of press on enter key.
	 *
	 * @param event The action event - user pressed on 'Enter' key.
	 */
	@FXML
	void inputPass(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			btnNext.fire();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Next' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void nextAction(ActionEvent event) {
		Object object = null;
		if (getCode().trim().isEmpty())
			lblErrCode.setText("enter code");
		else {
			lblErrCode.setText("");
			Message messageToServer = new Message();
			messageToServer.setMsg(getCode() + " computerized " + LoginController.user.getUsername());
			messageToServer.setOperation("StartComputerizedExam");
			messageToServer.setControllerName("ExamController");
			object =  ClientUI.client.handleMessageFromClientUI(messageToServer);
			// code isn't exists
			if (object == null)
				lblErrCode.setText("invalid code");
			else if (object instanceof Exam){
				compExam = (Exam)object;
				lblErrCode.setText("");
				code = getCode();
				// successes pop up
				ComputerizedExamEnterIDWindowController popUp = new ComputerizedExamEnterIDWindowController();
				try {
					popUp.start(new Stage());
					//update the countPerformers in data base to +1
					messageToServer.setMsg(getCode());
					messageToServer.setOperation("increaseCounter");
					messageToServer.setControllerName("ExamController");
					ClientUI.client.handleMessageFromClientUI(messageToServer);
					UsefulMethods.instance().close(event);
				} catch (Exception e) {
					UsefulMethods.instance().printException(e);
				}
			}
			else  {
				//Message temp = (Message) object;
				switch( (String) object ) {
				
				case "too late to get into the exam":
					lblErrCode.setText("too late..");
					break;
					
				case "student is already did the exam":
					lblErrCode.setText("already done");
					break;
				
				}
		}
	}
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