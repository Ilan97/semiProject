package gui;

import client.ClientController;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Exam;
import logic.Message;

/**
 * This is controller class (boundary) for window SearchExam. This class handle all
 * events related to this window. This class connect with client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version April 2021
 */

public class SearchExamController {

	// Instance variables **********************************************

	/**
	 * instance of the next window.
	 */
	private ShowExamController showExamController;
	/**
	 * FXML variables.
	 */
	@FXML
	private TextField txtExamID;
	@FXML
	private Label lblErrorID;

	// Instance methods ************************************************

	/**
	 * The client's first window and this window's first method. load and show this
	 * window.
	 *
	 * @param primaryStage The stage for window's scene.
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/SearchExam.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * @param exam The exam we want to show on screen.
	 */
	public void loadExam(Exam exam) {
		this.showExamController.loadExam(exam);
	}

	/**
	 * @return the exam's entire id
	 */
	private String getExamID() {
		return txtExamID.getText();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'search' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	public void searchActionButton(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		String id = getExamID();
		// check that user put an id
		if (id.trim().isEmpty())
			lblErrorID.setText("Please enter ID");
		// trim isn't empty
		else {
			// create new Message object with the request
			Message messageToServer = new Message();
			messageToServer.setMsg(id);
			messageToServer.setOperation("viewTableExam");
			messageToServer.setControllerName("ExamController");
			// execute the query and create instance of Exam
			ClientUI.client.handleMessageFromClientUI(messageToServer);
			// exam isn't exsits in DB
			if (ClientController.exam.getExamID().equals("Error")) {
				lblErrorID.setText("Exam ID not found");
			} else {
				display("Exam ID Found");
				// hiding the current window
				((Node) event.getSource()).getScene().getWindow().hide();
				// load the next window
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/gui/ShowExam.fxml").openStream());
				ShowExamController showExamController = loader.getController();
				showExamController.loadExam(ClientController.exam);
				// show the loaded scene
				Scene scene = new Scene(root);
				primaryStage.setTitle("CEMS");
				primaryStage.setScene(scene);
				primaryStage.show();
			}
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'exit' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	public void getExitButton(ActionEvent event) {
		display("exit client");
		System.exit(0);
	}

	/**
	 * This method displays a message into the console.
	 *
	 * @param message The string to be displayed.
	 */
	public static void display(String message) {
		System.out.println("> " + message);
	}
}
//End of SearchExamController class