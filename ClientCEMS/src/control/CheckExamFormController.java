package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import gui.Navigator;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import logic.ExamOfStudent;
import logic.Message;

/**
 * This is controller class (boundary) for window CheckExam in Teacher. This
 * class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @version June 2021
 */

public class CheckExamFormController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The exam of the chosen student.
	 */
	public static ExamOfStudent chosenExam;

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private Button btnNext;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnFinish;
	@FXML
	private ListView<String> studentsList;
	@FXML
	private TextField txtCode;
	@FXML
	private Label lblErrorExamID;
	@FXML
	private Label lblSusp;
	@FXML
	private TextArea txtSusp;

	/**
	 * This method check that there is no selected values in the form.
	 *
	 * @return true if form isn't empty, false otherwise.
	 */
	private boolean formIsNotEmpty() {
		if (!studentsList.getSelectionModel().isEmpty())
			return true;
		return false;
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
	@SuppressWarnings("unchecked")
	@FXML
	void inputCode(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			if (getCode().trim().isEmpty()) {
				lblErrorExamID.setText("enter code");
				lblSusp.setVisible(false);
				txtSusp.setVisible(false);
				studentsList.setVisible(false);
			}
			// trim isn't empty
			else {
				Message messageToServer = new Message();
				// check if code exists in DB
				boolean isExists;
				messageToServer.setMsg(getCode());
				messageToServer.setOperation("CheckCodeExistsForCheckExam");
				messageToServer.setControllerName("ExamController");
				isExists = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
				// code isn't exists
				if (!isExists) {
					lblErrorExamID.setText("invalid code");
					lblSusp.setVisible(false);
					txtSusp.setVisible(false);
					studentsList.setVisible(false);
				}
				// code is valid
				else {
					// check if the teacher checks all exams
					boolean isChecked;
					messageToServer.setMsg(getCode());
					messageToServer.setOperation("AllExamsChecked");
					messageToServer.setControllerName("TeacherController");
					isChecked = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
					if (isChecked) {
						lblErrorExamID.setText("all exams are checked!");
						lblSusp.setVisible(false);
						txtSusp.setVisible(false);
						studentsList.setVisible(false);
					}
					// all terms are good
					else {
						lblSusp.setVisible(false);
						txtSusp.setVisible(false);
						studentsList.setVisible(false);
						lblErrorExamID.setText("");
						// get list of student names that suspected of copying
						ArrayList<String> listOfNames = null;
						messageToServer.setMsg(getCode());
						messageToServer.setControllerName("ExamController");
						messageToServer.setOperation("CheckStudentsCopies");
						listOfNames = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
						if (listOfNames != null) {
							lblSusp.setVisible(true);
							txtSusp.setVisible(true);
							txtSusp.setText(studentsNames(listOfNames));
						}
						// set the names of students that did this exam and the teacher didn't check it
						ArrayList<String> listOfStudents = null;
						messageToServer.setMsg(getCode());
						messageToServer.setControllerName("StudentController");
						messageToServer.setOperation("GetAllStudents");
						listOfStudents = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
						studentsList.setItems(FXCollections.observableArrayList(listOfStudents));
						studentsList.setVisible(true);
					}
				}
			}
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param listOfNames {@link ArrayList} names of students that are suspected for
	 *                    coping.
	 * @return toString of this list.
	 */
	private String studentsNames(ArrayList<String> listOfNames) {
		String names = "";
		for (String name : listOfNames)
			names += name + "\n";
		return names;
	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		if (formIsNotEmpty())
			Navigator.instance().alertPopUp("TeacherHomeForm");
		else
			Navigator.instance().clearHistory("TeacherHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write Question'.
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeQuestionAction(ActionEvent event) {
		if (formIsNotEmpty())
			Navigator.instance().alertPopUp("WriteQuestionForm1");
		else
			Navigator.instance().navigate("WriteQuestionForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write an Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeExamAction(ActionEvent event) {
		if (formIsNotEmpty())
			Navigator.instance().alertPopUp("WriteAnExamForm1");
		else
			Navigator.instance().navigate("WriteAnExamForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Get Report'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void getReportAction(ActionEvent event) {
		if (formIsNotEmpty())
			Navigator.instance().alertPopUp("TeacherReportForm1");
		else
			Navigator.instance().navigate("TeacherReportForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Check Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void checkExamAction(ActionEvent event) {
		if (formIsNotEmpty())
			Navigator.instance().alertPopUp("checkExamForm");
		else
			Navigator.instance().navigate("checkExamForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Exam Stock'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void examSearchAction(ActionEvent event) {
		if (formIsNotEmpty())
			Navigator.instance().alertPopUp("ExamStockForm1");
		else
			Navigator.instance().navigate("ExamStockForm1");
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lblSusp.setVisible(false);
		txtSusp.setVisible(false);
		studentsList.setVisible(false);
		chosenExam = null;
		// set images
		Image img = new Image(this.getClass().getResource("teacherFrame.PNG").toString());
		imgBack.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		// show the exam of chosen student (clicked)
		studentsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// student chosen
				if (newValue != null) {
					Platform.runLater(() -> {
						if (studentsList.getSelectionModel().isEmpty())
							return;
						else {
							Message messageToServer = new Message();
							// get this student's user name
							messageToServer.setMsg(studentsList.getSelectionModel().getSelectedItem());
							messageToServer.setControllerName("StudentController");
							messageToServer.setOperation("GetUserName");
							String userName = (String) ClientUI.client.handleMessageFromClientUI(messageToServer);
							// get this student's exam
							messageToServer.setMsg(userName + " " + getCode());
							messageToServer.setControllerName("StudentController");
							messageToServer.setOperation("GetExamOfStudent");
							chosenExam = (ExamOfStudent) ClientUI.client.handleMessageFromClientUI(messageToServer);
							CheckExamOfStudentFormController check = new CheckExamOfStudentFormController();
							try {
								check.start(new Stage());
								studentsList.getSelectionModel().clearSelection();
								// refresh the list
								KeyEvent press = new KeyEvent(KeyEvent.KEY_PRESSED, txtCode, KeyEvent.KEY_PRESSED, "",
										"", KeyCode.ENTER, false, false, false, false);
								txtCode.fireEvent(press);
							} catch (Exception e) {
								UsefulMethods.instance().printException(e);
							}
						}
					});
				}
			}
		});
	}
}
//End of checkExamFormController class