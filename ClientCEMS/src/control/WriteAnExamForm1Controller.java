package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import gui.Navigator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.Exam;
import logic.ExamType;
import logic.Message;

/**
 * This is controller class (boundary) for window WriteAnExam (first part) in
 * Teacher. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @version May 2021
 */

public class WriteAnExamForm1Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * static instance for Exam object. Will be create each time teacher decide to
	 * write new exam. the object initialize by the info that teacher puts in the
	 * screen, then will be save in DB.
	 */
	public static Exam Exam;

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private ImageView imgPencil;
	@FXML
	private Label lblErrorPort21;
	@FXML
	private ComboBox<String> field;
	@FXML
	private ComboBox<String> course;
	@FXML
	private ComboBox<String> type;
	@FXML
	private TextField txtDuration;
	@FXML
	private Label lblErrField;
	@FXML
	private Label lblErrCourse;
	@FXML
	private Label lblErrType;
	@FXML
	private Label lblErrDur;

	// Instance methods ************************************************

	/**
	 * This method clear error label.
	 */
	private void clearErrLbl(Label err) {
		err.setText("");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'next' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void next(ActionEvent event) {
		// get details from the screen
		String Field = field.getSelectionModel().getSelectedItem();
		String Course = course.getSelectionModel().getSelectedItem();
		String Type = type.getSelectionModel().getSelectedItem();
		String Duration = txtDuration.getText();
		// handle missing fields
		if (field.getSelectionModel().isEmpty() || course.getSelectionModel().isEmpty()
				|| type.getSelectionModel().isEmpty() || Duration.trim().isEmpty()) {
			// field not chosen
			if (field.getSelectionModel().isEmpty())
				lblErrField.setText("choose field");
			// field chosen
			else
				clearErrLbl(lblErrField);

			// course not chosen
			if (course.getSelectionModel().isEmpty())
				lblErrCourse.setText("choose course");
			// course chosen
			else
				clearErrLbl(lblErrCourse);

			// type not chosen
			if (type.getSelectionModel().isEmpty())
				lblErrType.setText("choose type");
			// type chosen
			else
				clearErrLbl(lblErrType);

			// no duration
			if (Duration.trim().isEmpty())
				lblErrDur.setText("enter content");
			// there is duration
			else
				clearErrLbl(lblErrDur);

		} else {
			// build the Exam object
			Exam e = new Exam();
			e.setAuthor(LoginController.user.getFirstName() + " " + LoginController.user.getLastName());
			e.setFname(Field);
			e.setCname(Course);
			e.setDuration(Integer.parseInt(Duration));
			// set the exam type
			switch (Type) {
			case "Computerized":
				e.setEtype(ExamType.COMPUTERIZED);
				break;
			case "Manual":
				e.setEtype(ExamType.MANUAL);
				break;
			}
			WriteAnExamForm1Controller.Exam = e;
			// go to next page
			Navigator.instance().navigate("WriteAnExamForm2");
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'field' comboBox.
	 * Get list of courses that teacher teach, in order to the field that was
	 * chosen.
	 *
	 * @param event The action event.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void chooseFieldAction(ActionEvent event) {
		ArrayList<String> listOfCourse;
		Message messageToServer = new Message();
		String UserName = LoginController.user.getUsername();
		String ChosenField = field.getSelectionModel().getSelectedItem();
		messageToServer.setMsg(UserName + " " + ChosenField);
		messageToServer.setControllerName("TeacherController");
		messageToServer.setOperation("ShowCourseList");
		listOfCourse = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		course.setItems(FXCollections.observableArrayList(listOfCourse));
		course.setDisable(false);
	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().clearHistory("TeacherHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write Question'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeQuestionAction(ActionEvent event) {
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
		Navigator.instance().navigate("TeacherReportForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Change Exam
	 * Duration' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void changeDurAction(ActionEvent event) {
		Navigator.instance().navigate("RequestChangeExamDurationTimeWindow");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Check Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void checkExamAction(ActionEvent event) {
		// Navigator.instance().navigate(" ");///????
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Exam Stock'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void examSearchAction(ActionEvent event) {
		Navigator.instance().navigate("ExamStockForm1");
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Exam = null;
		// set images
		Image img1 = new Image(this.getClass().getResource("frame1WriteAnExam.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("pencil.png").toString());
		imgPencil.setImage(img3);
		// cannot choose anything from that list
		course.setDisable(true);
		// set the content (list of fields that teacher is teaching) in the comboBox
		// 'field'
		ArrayList<String> listOfField;
		String UserName = LoginController.user.getUsername();
		Message messageToServer = new Message();
		messageToServer.setMsg(UserName);
		messageToServer.setControllerName("TeacherController");
		messageToServer.setOperation("ShowFieldList");
		listOfField = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		field.setItems(FXCollections.observableArrayList(listOfField));
		// set the content in the comboBox 'type'
		ArrayList<String> examTypes = new ArrayList<>();
		examTypes.add("Manual");
		examTypes.add("Computerized");
		type.setItems(FXCollections.observableArrayList(examTypes));
	}

}
//End of WriteAnExamForm1Controller class