package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Exam;
import logic.Message;

/**
 * This is controller class (boundary) for window SearchExam. This class handle
 * all events related to this window. This class connect with client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version April 2021
 */

public class SearchExamController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * static instance for Exam object. Will be create only once for each run. the
	 * object initialize by the info that return from DB.
	 */
	public static Exam exam;

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
//		Parent root = FXMLLoader.load(getClass().getResource("/gui/SearchExam.fxml"));
//		Scene scene = new Scene(root);
//		primaryStage.setTitle("CEMS");
		Pane p = new Pane();
		Navigator.init(p);
		primaryStage.setScene(new Scene(p));
		Navigator.instance().navigate("SearchExam");
		// close the client
		primaryStage.setOnCloseRequest((event) -> {
			try {
				ClientUI.client.closeConnection();
			} catch (IOException ex) {
				System.out.println("Fail to close client!");
			}
			System.exit(0);
		});
		primaryStage.show();
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
		//FXMLLoader loader = new FXMLLoader();
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
			exam = (Exam) ClientUI.client.handleMessageFromClientUI(messageToServer);
			// exam isn't exists in DB
			if (exam.getExamID().equals("Error")) {
				lblErrorID.setText("Exam ID not found");
			} else {
				display("Exam ID Found");
//				// hiding the current window
//				((Node) event.getSource()).getScene().getWindow().hide();
//				// load the next window
//				Stage primaryStage = new Stage();
//				Pane root = loader.load(getClass().getResource("/gui/ShowExam.fxml").openStream());
//				ShowExamController showExamController = loader.getController();
//				showExamController.loadExam();
//				// show the loaded scene
//				Scene scene = new Scene(root);
//				primaryStage.setTitle("CEMS");
//				// close the client
//				primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//					@Override
//					public void handle(WindowEvent e) {
//						Platform.exit();
//						try {
//							ClientUI.client.closeConnection();
//						} catch (IOException ex) {
//							display("Faild to exit client!");
//						}
//					}
//				});
//				primaryStage.setScene(scene);
//				primaryStage.show();
				Navigator.instance().navigate("showExam");
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
		try {
			ClientUI.client.closeConnection();
		} catch (IOException ex) {
			display("Faild to disconnect client!");
		}
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

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}
//End of SearchExamController class