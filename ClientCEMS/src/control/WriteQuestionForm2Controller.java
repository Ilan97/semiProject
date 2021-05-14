package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.Message;
import logic.Question;

/**
 * This is controller class (boundary) for window WriteQuestion (second part) in
 * Teacher. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author
 * @version May 2021
 */

public class WriteQuestionForm2Controller implements GuiController, Initializable {

	

	// Instance variables **********************************************



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
	private Label lblField;
	@FXML
	private Label lblCourse;
	@FXML
	private TextArea questionView;
	@FXML
	private Label lblAuthor;
	@FXML
	private Label lblSerialNum;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'back' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void back(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Save' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void save(ActionEvent event) {

		Message messageToServer = new Message();
		messageToServer.setMsg(WriteQuestionForm1Controller.Question);
		messageToServer.setControllerName("QuestionController");
		messageToServer.setOperation("SaveQuestion");
		boolean result =(boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
		if(result == true)
			//pop up successfully
			;
		else 
			//pop up failed
			;
		
		
		
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
		Image img1 = new Image(this.getClass().getResource("frameWriteQuestion2.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("pencil.png").toString());
		imgPencil.setImage(img3);
		
		ShowQuestion();
	}
	/**
	 * This method display the question to user window.
	 */
	private void ShowQuestion() {
		 Question q = WriteQuestionForm1Controller.Question;
		 lblField.setText(q.getFieldName());
		 lblCourse.setText(q.getCourseName());
		 lblAuthor.setText(q.getAuthor());
		 String Cid = GetCid(q.getCourseName());
		 if (! Cid.equals("Course not found"))
			 q.setCid(Cid);
		 String Fid = GetFid(q.getFieldName());
		 if (! Fid.equals("Field not found"))
			 q.setFid(Fid);
		 int Qid = GetQid(q.getFieldName() ,q.getCourseName());
		 if ( Qid != -1 )
			q.setQid(""+(Qid+1));
		 else 
			 System.out.println("qid not found");
		 q.setQuestionID(Fid+Cid+(Qid+1));
		 
		 lblSerialNum.setText(q.getQuestionID());
		 String QuestionView = GetQuestionView(q);
		 questionView.setText(QuestionView);

		
	}

	/**
	 * This method prepares the string for questionView.
	 *
	 * @param question .
	 * @return  questionView that present the question.
	 */
	public String GetQuestionView(Question q) {
		String QuestionView = "";
		QuestionView += q.getContent()+"\n\n";
		QuestionView += q.getInstructions()+"\n\n";
		QuestionView += "1) " + q.getRightAnswer()+"\n";
		QuestionView += "2) " + q.getWrongAnswer1()+"\n";
		QuestionView += "3) " + q.getWrongAnswer2()+"\n";
		QuestionView += "4) " + q.getWrongAnswer3()+"\n";
		return QuestionView;
	}

	/**
	 * This method request from server to return the qid from DB.
	 *
	 * @param fieldName,courseName from client.
	 * @return return qid if found in dataBase else return -1
	 */	
	public int GetQid(String fieldName, String courseName) {
		int Qid;
		Message messageToServer = new Message();
		messageToServer.setMsg(fieldName+" "+courseName);
		messageToServer.setControllerName("QuestionController");
		messageToServer.setOperation("GetQid");
		Qid = (int) ClientUI.client.handleMessageFromClientUI(messageToServer);
		return Qid;
	}

	/**
	 * This method request from server to return the fid from DB.
	 *
	 * @param fieldName from client.
	 * @return return Fid if Field found in dataBase else return "Field not found"
	 */	
	public String GetFid(String FieldName) {
		String Fid;
		Message messageToServer = new Message();
		messageToServer.setMsg(FieldName);
		messageToServer.setControllerName("QuestionController");
		messageToServer.setOperation("GetCourseId");
		Fid = (String) ClientUI.client.handleMessageFromClientUI(messageToServer);
		if ( Fid == null)
			return "Field not found";
		return Fid;
	}

	 
		/**
		 * This method request from server to return the cid from DB.
		 *
		 * @param CourseName from client.
		 * @return return Cid if Course found in dataBase else return "Course not found"
		 */	
	public String GetCid(String CourseName) {
		String Cid;
		Message messageToServer = new Message();
		messageToServer.setMsg(CourseName);
		messageToServer.setControllerName("QuestionController");
		messageToServer.setOperation("GetCourseId");
		Cid = (String) ClientUI.client.handleMessageFromClientUI(messageToServer);
		if ( Cid == null)
			return "Course not found";
		return Cid;
	}
	


}
// End of WriteQuestionForm2Controller class
