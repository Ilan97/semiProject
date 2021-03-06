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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.Exam;
import logic.Message;

/**
 * This is controller class (boundary) for window ExamStock (first part). This
 * class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @version May 2021
 */

public class ExamStockForm1Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The chosen {@link Exam}.
	 */
	public static Exam Exam;
	/**
	 * {@link Exam} {@link ArrayList} all exams from specific course and field.
	 */
	public static ArrayList<Exam> listOfExams;

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private ComboBox<String> field;
	@FXML
	private ComboBox<String> course;
	@FXML
	private Label lblErrField;
	@FXML
	private Label lblErrCourse;
	@FXML
	private Label lblErrData;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'field' comboBox.
	 * Get list of courses in order to the field that was chosen.
	 *
	 * @param event The action event.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void chooseFieldAction(ActionEvent event) {
		ArrayList<String> listOfCourse;
		Message messageToServer1 = new Message();
		Message messageToServer2 = new Message();
		String fid;
		String ChosenField = field.getSelectionModel().getSelectedItem();
		// get chosen field id
		messageToServer1.setMsg(ChosenField);
		messageToServer1.setControllerName("FieldOfStudyController");
		messageToServer1.setOperation("GetFieldId");
		fid = (String) ClientUI.client.handleMessageFromClientUI(messageToServer1);
		// get this field's course list
		messageToServer2.setMsg(fid);
		messageToServer2.setControllerName("CourseController");
		messageToServer2.setOperation("ShowCourseList");
		listOfCourse = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer2);
		course.setItems(FXCollections.observableArrayList(listOfCourse));
		course.setDisable(false);
	}

	/**
	 * This method clear error label.
	 * 
	 * @param err the label to clear.
	 */
	private void clearErrLbl(Label err) {
		err.setText("");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Search' button.
	 *
	 * @param event The action event.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void searchAction(ActionEvent event) {
		// get details from the screen
		String Field = field.getSelectionModel().getSelectedItem();
		String Course = course.getSelectionModel().getSelectedItem();
		// handle missing fields
		if (field.getSelectionModel().isEmpty()) {
			// field not chosen
			if (field.getSelectionModel().isEmpty())
				lblErrField.setText("choose field");
			// field chosen
			else
				clearErrLbl(lblErrField);

		} else {
			// build the Exam object
			Exam e = new Exam();
			e.setFname(Field);
			e.setCname(Course);
			Exam = e;
			// get all exams
			Message messageToServer = new Message();
			messageToServer.setMsg(Exam.getFname() + " " + Exam.getCname());
			messageToServer.setControllerName("ExamController");
			messageToServer.setOperation("ShowExamList");
			listOfExams = (ArrayList<Exam>) ClientUI.client.handleMessageFromClientUI(messageToServer);
			if (listOfExams == null)
				lblErrData.setText("there is no data to show!");
			else {
				clearErrLbl(lblErrData);
				// go to next page
				Navigator.instance().navigate("ExamStockForm2");
			}
		}
	}

	/**
	 * This method check that there is no selected values in the form
	 *
	 * @return true if form isn't empty, false otherwise.
	 */
	private boolean formIsNotEmpty() {
		if (!field.getSelectionModel().isEmpty())
			return true;
		return false;
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
		if (formIsNotEmpty())
			Navigator.instance().alertPopUp("CheckExamForm");
		else
			Navigator.instance().navigate("CheckExamForm");
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
	@SuppressWarnings({ "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listOfExams = null;
		Exam = null;
		course.setDisable(true);
		// set images
		Image img1 = new Image(this.getClass().getResource("frameWriteQuestion1.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		// set the content in the comboBox 'field'
		ArrayList<String> listOfField = null;
		Message messageToServer = new Message();
		messageToServer.setControllerName("FieldOfStudyController");
		messageToServer.setOperation("ShowAllFields");
		listOfField = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		field.setItems(FXCollections.observableArrayList(listOfField));
	}
}
// End of ExamStockForm1Controller class