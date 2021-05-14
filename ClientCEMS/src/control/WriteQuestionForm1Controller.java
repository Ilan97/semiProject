package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.Exam;
import logic.Message;
import logic.Question;

/**
 * This is controller class (boundary) for window WriteQuestion (first part) in
 * Teacher. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author
 * @version May 2021
 */

public class WriteQuestionForm1Controller implements GuiController, Initializable {

	// Instance variables **********************************************
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
	private MenuButton filed;
    @FXML
    private MenuButton course;
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
	 * This is FXML event handler. Handles the action of click on 'next' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void next(ActionEvent event) {
		
		String QuestionCon = questionCon.getText();
		String Instructions = instructions.getText();
		String RightAns= rightAns.getText();
		String WrongAns1 = wrongAns1.getText();
		String WrongAns2 = wrongAns2.getText();
		String WrongAns3 = wrongAns3.getText();
		String Field = filed.getText();
		String Course = course.getText(); 
		
		if( QuestionCon == null || Instructions == null || RightAns == null ||
				WrongAns1 == null || WrongAns2 == null || WrongAns3 == null ||
				Field == null || Course == null)
		//Pop up - missing details ya zain !//
		;
		else {
			Question q = new Question();
			q.setAuthor(LoginController.user.getFirstName()+" "+LoginController.user.getLastName());
			q.setFieldName(Field);
			q.setCourseName(Course);
			q.setContent(QuestionCon);
			q.setInstructions(Instructions);
			q.setRightAnswer(RightAns);
			q.setWrongAnswer1(WrongAns1);
			q.setWrongAnswer2(WrongAns2);
			q.setWrongAnswer3(WrongAns2);
			
			//go to next page --> 
			
			Navigator.instance().navigate("WriteQuestionForm2");
	}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Choose Field'
	 * menu button.
	 *
	 * @param event The action event.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void chooseFieldAction(ActionEvent event) {
		ArrayList<String> listOfField;
		String UserName = LoginController.user.getUsername();
		Message messageToServer = new Message();
		messageToServer.setMsg(UserName);
		messageToServer.setControllerName("QuestionController");
		messageToServer.setOperation("ShowFieldList");
		listOfField = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		for (String field : listOfField) 
			filed.setText(field);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Choose Course'
	 * menu button.
	 *
	 * @param event The action event.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void chooseCourseAction(ActionEvent event) {
		ArrayList<String> listOfCourse;
		Message messageToServer = new Message();
		
		if ( filed.getText() == null ) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("CEMS");
			a.setResizable(true);
			a.setHeaderText("First Choose Field !");
			Label label = new Label();
			label.setPrefSize(100, 100);
			label.setPadding(new Insets(10, 10, 10, 10));
			a.setGraphic(label);
			a.showAndWait();
		}
		else {
			String UserName = LoginController.user.getUsername();
			String ChosenField = filed.getText();
			messageToServer.setMsg(UserName+" "+ChosenField);
			messageToServer.setControllerName("QuestionController");
			messageToServer.setOperation("ShowCourseList");
			listOfCourse = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
			for (String Course : listOfCourse) 
				course.setText(Course);
			}
			
	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goToHome(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write Question'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeQuestionAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write an Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeExamAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Get Report'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void getReportAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Change Exam
	 * Duration' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void changeDurAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Check Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void checkExamAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Exam Stock'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void examSearchAction(ActionEvent event) {

	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image img1 = new Image(this.getClass().getResource("frameWriteQuestion1.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("pencil.png").toString());
		imgPencil.setImage(img3);
	}

}
// End of WriteQuestionForm1Controller class