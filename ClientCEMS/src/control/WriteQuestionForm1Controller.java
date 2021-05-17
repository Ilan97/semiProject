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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.Message;
import logic.Question;

/**
 * This is controller class (boundary) for window WriteQuestion (first part) in
 * Teacher. This class handle all events related to this window. This class
 * connect with client.
 * 
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version May 2021
 */

public class WriteQuestionForm1Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * static instance for Question object. Will be create each time teacher decide
	 * to write new question. the object initialize by the info that teacher puts in
	 * the screen, then will be save in DB.
	 */
	public static Question Question;

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
	private ComboBox<String> field;
	@FXML
	private ComboBox<String> course;
	@FXML
	private TextArea questionCon;
	@FXML
	private TextArea rightAns;
	@FXML
	private TextArea wrongAns1;
	@FXML
	private TextArea wrongAns2;
	@FXML
	private TextArea wrongAns3;
	@FXML
	private TextArea instructions;
	@FXML
	private Label lblErrField;
	@FXML
	private Label lblErrCourse;
	@FXML
	private Label lblErrQues;
	@FXML
	private Label lblErrInstr;
	@FXML
	private Label lblErrRightAns;
	@FXML
	private Label lblErrWrongAns;

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
		String QuestionCon = questionCon.getText();
		String Instructions = instructions.getText();
		String RightAns = rightAns.getText();
		String WrongAns1 = wrongAns1.getText();
		String WrongAns2 = wrongAns2.getText();
		String WrongAns3 = wrongAns3.getText();
		String Field = field.getSelectionModel().getSelectedItem();
		String Course = course.getSelectionModel().getSelectedItem();
		// handle missing fields
		if (field.getSelectionModel().isEmpty() || course.getSelectionModel().isEmpty() || QuestionCon.trim().isEmpty()
				|| Instructions.trim().isEmpty() || RightAns.trim().isEmpty() || WrongAns1.trim().isEmpty()
				|| WrongAns2.trim().isEmpty() || WrongAns3.trim().isEmpty()) {
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

			// no content
			if (QuestionCon.trim().isEmpty())
				lblErrQues.setText("enter content");
			// there is content
			else
				clearErrLbl(lblErrQues);

			// no instructions
			if (Instructions.trim().isEmpty())
				lblErrInstr.setText("enter instructions");
			// there are instructions
			else
				clearErrLbl(lblErrInstr);

			// no right answer
			if (RightAns.trim().isEmpty())
				lblErrRightAns.setText("enter answer");
			// there is right answer
			else
				clearErrLbl(lblErrRightAns);

			// one or more wrong answers are missing
			if (WrongAns1.trim().isEmpty() || WrongAns2.trim().isEmpty() || WrongAns3.trim().isEmpty())
				lblErrWrongAns.setText("enter 3 answers");
			// there are 3 wrong answers
			else
				clearErrLbl(lblErrWrongAns);
		} else {
			// build the Question object
			Question q = new Question();
			q.setAuthor(LoginController.user.getFirstName() + " " + LoginController.user.getLastName());
			q.setFieldName(Field);
			q.setCourseName(Course);
			q.setContent(QuestionCon);
			q.setInstructions(Instructions);
			q.setRightAnswer(RightAns);
			q.setWrongAnswer1(WrongAns1);
			q.setWrongAnswer2(WrongAns2);
			q.setWrongAnswer3(WrongAns3);
			WriteQuestionForm1Controller.Question = q;
			// go to next page
			Navigator.instance().navigate("WriteQuestionForm2");
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
		// set images
		Image img1 = new Image(this.getClass().getResource("frameWriteQuestion1.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("pencil.png").toString());
		imgPencil.setImage(img3);
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
	}
}
// End of WriteQuestionForm1Controller class