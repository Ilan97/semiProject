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
import logic.Message;
import logic.Question;

/**
 * This is controller class (boundary) for window ViewQuestions (first part).
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Sharon Vaknin
 * @version June 2021
 */

public class ViewQuestionsForm1Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	public static Question question;

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
	 * This method clear error label.
	 */
	private void clearErrLbl(Label err) {
		err.setText("");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'View Questions'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void viewQuestionsBtnAction(ActionEvent event) {
		// handle missing fields
		if (fieldCB.getSelectionModel().isEmpty()) {
			// field not chosen
			if (fieldCB.getSelectionModel().isEmpty())
				lblErrField.setText("choose field");
			// field chosen
			else
				clearErrLbl(lblErrField);
		}

		if (courseCB.getSelectionModel().isEmpty()) {
			// course not chosen
			if (courseCB.getSelectionModel().isEmpty())
				lblErrCourse.setText("choose course");
			// course chosen
			else
				clearErrLbl(lblErrCourse);
		}

		else {
			Question q = new Question();
			String Field = fieldCB.getSelectionModel().getSelectedItem();
			String Course = courseCB.getSelectionModel().getSelectedItem();
			q.setFieldName(Field);
			q.setCourseName(Course);
			question = q;

			// go to next page
			Navigator.instance().navigate("ViewQuestionsForm2");
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
		question = null;
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
// End of ViewQuestionsForm1Controller class