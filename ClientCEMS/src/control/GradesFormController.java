package control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.ClientUI;
import gui.Navigator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.ExamOfStudent;
import logic.Message;
import logic.User;

/**
 * This is controller class (boundary) for window Grades in Student. This class
 * handle all events related to this window. This class connect with client.
 *
 * @author
 * @version May 2021
 */

public class GradesFormController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * static variables.
	 */
	public static ExamOfStudent chosenExam;
	
	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private Label lblGrade;
    @FXML
    private ListView<ExamOfStudent> examsList;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Show Exam' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void showExamAction(ActionEvent event) {
		checkValidExam();
		if(chosenExam == null)
			return;
		ShowExamWindowController showExam = new ShowExamWindowController();
		try {
			showExam.start(new Stage());
		} catch (IOException e) {}
	}
	
	private void checkValidExam() {
		if (examsList.getSelectionModel().isEmpty())
			chosenExam = null;
		chosenExam = examsList.getSelectionModel().getSelectedItem();
	}
	/**
	 * This is FXML event handler. Handles the action of click on 'Get Grade'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void getGradeAction(ActionEvent event) {
		checkValidExam();
		if(chosenExam == null)
			return;
		lblGrade.setText(String.valueOf(chosenExam.getGrade())); 
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Close' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void closeAction(ActionEvent event) {
		Navigator.instance().clearHistory("StudentHomeForm");
	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().clearHistory("StudentHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Computerized
	 * Exam' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void compExamAction(ActionEvent event) {
		//successes pop up
		ComputerizedExamCodeWindowController popUp = new ComputerizedExamCodeWindowController();
		try {
			popUp.start(new Stage());
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Manual Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void manualExamAction(ActionEvent event) {
		Navigator.instance().navigate("ManualExamForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Grades' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void gradesAction(ActionEvent event) {
		Navigator.instance().navigate("GradesForm");
		
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		chosenExam = null;
		// set images
		Image img = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		// set the exams list in order to the UserName
		ArrayList<ExamOfStudent> ExamsOfStudentList;
		User user = LoginController.user;
		Message messageToServer = new Message();
		messageToServer.setMsg(user.getUsername());
		messageToServer.setControllerName("StudentController");
		messageToServer.setOperation("ShowExamOfStudentList");
		System.out.println(messageToServer);
		ExamsOfStudentList = (ArrayList<ExamOfStudent>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		examsList.setItems(FXCollections.observableArrayList(ExamsOfStudentList));
	}

}
//End of GradesFormController class