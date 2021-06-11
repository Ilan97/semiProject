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
 * This is controller class (boundary) for window ViewExams (first part). This
 * class handle all events related to this window. This class connect with
 * client.
 *
 * @author Sharon Vaknin
 * @version June 2021
 */

public class ViewExamsForm1Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * {@link Exam} details.
	 */
	public static Exam exam;
	/**
	 * {@link ArrayList} of {@link Exam} to show.
	 */
	public static ArrayList<Exam> listOfExams;

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private ImageView imgClock;
	@FXML
	private ComboBox<String> fieldCB;
	@FXML
	private ComboBox<String> courseCB;
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
		clearErrLbl(lblErrField);
		clearErrLbl(lblErrCourse);
		ArrayList<String> listOfCourse;
		Message messageToServer1 = new Message();
		Message messageToServer2 = new Message();
		String fid;
		String ChosenField = fieldCB.getSelectionModel().getSelectedItem();
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
		courseCB.setItems(FXCollections.observableArrayList(listOfCourse));
		courseCB.setDisable(false);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'course' comboBox.
	 * Get list of courses in order to the field that was chosen.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseCourseAction(ActionEvent event) {
		clearErrLbl(lblErrField);
		clearErrLbl(lblErrCourse);
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
	 * This is FXML event handler. Handles the action of click on 'View Exams'
	 * button.
	 *
	 * @param event The action event.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void viewExamsBtnAction(ActionEvent event) {
		// handle missing fields
		if (fieldCB.getSelectionModel().isEmpty()) {
			// field not chosen
			if (fieldCB.getSelectionModel().isEmpty())
				lblErrField.setText("choose field");
			// field chosen
			else
				clearErrLbl(lblErrField);
		}

		else if (courseCB.getSelectionModel().isEmpty()) {
			// course not chosen
			if (courseCB.getSelectionModel().isEmpty()) {
				lblErrCourse.setText("choose course");
				clearErrLbl(lblErrField);
			}
			// course chosen
			else
				clearErrLbl(lblErrCourse);
		}

		else {
			Exam e = new Exam();
			String Field = fieldCB.getSelectionModel().getSelectedItem();
			String Course = courseCB.getSelectionModel().getSelectedItem();
			e.setFname(Field);
			e.setCname(Course);
			exam = e;
			// check if there any exams from this field and course
			Message messageToServer = new Message();
			messageToServer.setMsg(exam.getFname() + " " + exam.getCname());
			messageToServer.setControllerName("ExamController");
			messageToServer.setOperation("ShowExamList");
			listOfExams = (ArrayList<Exam>) ClientUI.client.handleMessageFromClientUI(messageToServer);
			if (listOfExams == null)
				lblErrData.setText("there is no data to show!");
			else {
				clearErrLbl(lblErrData);
				// go to next page
				Navigator.instance().navigate("ViewExamsForm2");
			}
		}
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
	@SuppressWarnings({ "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listOfExams = null;
		exam = null;
		// cannot choose anything from that list
		courseCB.setDisable(true);
		// set images
		Image img1 = new Image(this.getClass().getResource("frame1PrincipalReport.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		// set the content in the comboBox 'field'
		ArrayList<String> listOfField = null;
		Message messageToServer = new Message();
		messageToServer.setControllerName("FieldOfStudyController");
		messageToServer.setOperation("ShowAllFields");
		listOfField = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		fieldCB.setItems(FXCollections.observableArrayList(listOfField));
	}
}
//End of ViewExamsForm1Controller class