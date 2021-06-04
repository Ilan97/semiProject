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
	 * Static instance for {@link Exam} object. Will be create each time teacher
	 * decide to write new exam. the object initialize by the info that teacher puts
	 * in the screen, then will be save in DB.
	 */
	public static Exam Exam;
	/**
	 * new {@link Exam} object. Created only if user goes back and changes something
	 * that isn't field or course.
	 */
	private Exam e = null;
	/**
	 * This is how we know if user goes back or not.
	 */
	private boolean nextInit;

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
	 * @return the duration from window.
	 */
	private String getDuration() {
		return txtDuration.getText();
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
		// new exam object if the field and course was changed
		if (!nextInit) {
			e = new Exam();
		}
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
			if (getDuration().isEmpty())
				lblErrDur.setText("enter content");
			// there is duration
			else
				clearErrLbl(lblErrDur);

		} else if (!durationIsValid())
			lblErrDur.setText("invalid duration");
		else {
			// user return back but didn't change the field and course
			if (nextInit && Exam.getCname().equals(Course) && Exam.getFname().equals(Field)) {
				// set the exam type
				switch (Type) {
				case "Computerized":
					e.setEtype(ExamType.COMPUTERIZED);
					break;
				case "Manual":
					e.setEtype(ExamType.MANUAL);
					break;
				}
				e.setDuration(Double.parseDouble(Duration));
				Navigator.instance().next();
				return;
			}
			clearErrLbl(lblErrDur);
			// build new Exam object
			e.setAuthor(LoginController.user.getFirstName() + " " + LoginController.user.getLastName());
			e.setFname(Field);
			e.setCname(Course);
			e.setDuration(Double.parseDouble(Duration));
			// set the exam type
			switch (Type) {
			case "Computerized":
				e.setEtype(ExamType.COMPUTERIZED);
				break;
			case "Manual":
				e.setEtype(ExamType.MANUAL);
				break;
			}
			Exam = e;
			nextInit = true;
			// go to next page
			Navigator.instance().navigate("WriteAnExamForm2");
		}
	}

	/**
	 * This method check if duration is valid.
	 * 
	 * @return true if duration is valid, false otherwise.
	 */
	private boolean durationIsValid() {
		String durationString = getDuration();
		try {
			double duration = Double.parseDouble(durationString);
			// check if score is valid number
			if (duration < 0.1)
				return false;
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
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

	/**
	 * This method check that there is no selected values in the form.
	 *
	 * @return true if form isn't empty, false otherwise.
	 */
	private boolean formIsNotEmpty() {
		if (!field.getSelectionModel().isEmpty() || !type.getSelectionModel().isEmpty()
				|| !txtDuration.getText().trim().isEmpty())
			return true;
		return false;
	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 * @throws IOException
	 */
	@FXML
	void goHome(ActionEvent event) throws IOException {
		if (formIsNotEmpty())
			Navigator.instance().alertPopUp("TeacherHomeForm");
		else
			Navigator.instance().navigate("TeacherHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write Question'
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
		/* TODO Navigator.instance().navigate("checkExamForm"); */
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
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Exam = null;
		nextInit = false;
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