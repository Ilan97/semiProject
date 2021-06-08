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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.ExamOfStudent;
import logic.Message;

/**
 * This is controller class (boundary) for window ViewGrades for principal.
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Sharon Vaknin
 * @version June 2021
 */

public class ViewGradesFormController implements GuiController, Initializable {

	public static ExamOfStudent chosenExam;

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private Label lblGrade;
	@FXML
	private ListView<ExamOfStudent> examsList;
	@FXML
	private ComboBox<String> studentCB;
	@FXML
	private Button btnGetGrade;
	@FXML
	private Button btnShowExam;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'field' comboBox.
	 * Get list of courses in order to the field that was chosen.
	 *
	 * @param event The action event.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void chooseStudentAction(ActionEvent event) {
		lblGrade.setText("");
		// set the exams list in order to the UserName
		ArrayList<ExamOfStudent> ExamsOfStudentList;
		Message messageToServer = new Message();
		// get this student's exams list

		String studentName = studentCB.getSelectionModel().getSelectedItem();
		messageToServer.setMsg(studentName);
		messageToServer.setControllerName("StudentController");
		messageToServer.setOperation("GetUesrName");
		String userName = (String) ClientUI.client.handleMessageFromClientUI(messageToServer);
		messageToServer.setMsg(userName);
		messageToServer.setControllerName("StudentController");
		messageToServer.setOperation("ShowExamOfStudentList");
		ExamsOfStudentList = (ArrayList<ExamOfStudent>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		examsList.setItems(FXCollections.observableArrayList(ExamsOfStudentList));
		examsList.setDisable(false);
		btnShowExam.setDisable(false);
		btnGetGrade.setDisable(false);
	}

	/**
	 * This method check an exam is valid 
	 */
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
	 * This is FXML event handler. Handles the action of click on 'Show Exam' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void showExamAction(ActionEvent event) {
		checkValidExam();
		if(chosenExam == null)
			return;
		PrincipalExamOfStudentViewWindowController showExam = new PrincipalExamOfStudentViewWindowController();
		try {
			showExam.start(new Stage());
		} catch (IOException e) {}
	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().clearHistory("PrincipalHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Get Report'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void getReportAction(ActionEvent event) {
		Navigator.instance().navigate("PrincipalReportForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'View Request'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void viewRequestsAction(ActionEvent event) {
		Navigator.instance().navigate("PrincipalViewRequestForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'View Questions'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void viewQuestionsAction(ActionEvent event) {
		Navigator.instance().navigate("ViewQuestionsForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'View Exams'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void viewExamsAction(ActionEvent event) {
		Navigator.instance().navigate("ViewExamsForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'View Grades'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void viewGradesAction(ActionEvent event) {
		Navigator.instance().navigate("ViewGradesForm");
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
		Image img = new Image(this.getClass().getResource("principalFrame.PNG").toString());
		imgBack.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);

		// set list of all students
		ArrayList<String> listOfStudents = null;
		// get chosen student
		Message messageToServer = new Message();
		messageToServer.setControllerName("UserController");
		messageToServer.setOperation("ShowAllStudents");
		listOfStudents = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		studentCB.setItems(FXCollections.observableArrayList(listOfStudents));
		examsList.setDisable(true);
		btnShowExam.setDisable(true);
		btnGetGrade.setDisable(true);
	}
}
// End of ViewGradesFormController class