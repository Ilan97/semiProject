package gui;

import java.util.HashMap;
import java.util.Set;
import client.ClientController;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import logic.Exam;
import logic.Message;
import logic.Question;

/**
 * This is controller class (boundary) for window ShowExam. This class handle all
 * events related to this window. This class connect with client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version April 2021
 */

public class ShowExamController {

	// Instance variables **********************************************

	/**
	 * the shown Exam instance.
	 */
	private Exam exam;
	/**
	 * FXML variables.
	 */
	@FXML
	private TextArea txtScrollQuestions;
	@FXML
	private Label lblFieldName;
	@FXML
	private Label lblCourseName;
	@FXML
	private Label lblExamID;
	@FXML
	private Label lblSuccess;
	@FXML
	private Label lblError;
	@FXML
	private TextField txtDuration;

	/**
	 * This method set the ShowExam form.
	 * 
	 * @param exam The exam we want to show on screen.
	 */
	public void loadExam(Exam exam) {
		this.exam = exam;
		this.lblFieldName.setText(exam.getFname());
		this.lblCourseName.setText(exam.getCname());
		this.lblExamID.setText(exam.getExamID());
		this.txtDuration.setText("" + exam.getDuration());
		this.txtScrollQuestions.setText(setQuestions());
	}

	/**
	 * @return the duration time user puts in
	 */
	private String getNewDuration() {
		return txtDuration.getText();
	}

	/**
	 * This method build string that contain the question's number and score in this
	 * exam.
	 * 
	 * @return the build string.
	 */
	private String setQuestions() {
		StringBuilder builder = new StringBuilder();
		HashMap<Question, Integer> questionsInExam = exam.getQuestionsInExam();
		Set<Question> questionSet = questionsInExam.keySet();

		for (Question question : questionSet)
			builder.append("Question num." + question.getQid() + ": score " + questionsInExam.get(question) + "\n");

		return builder.toString();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'update' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	public void updateDuration(ActionEvent event) throws Exception {
		String newDuration = getNewDuration();
		// get the current duration from DB
		int oldDuration = ClientController.exam.getDuration();
		// check if user put duration time
		if (newDuration.trim().isEmpty()) {
			lblError.setText("empty field");
			lblSuccess.setText("");
		}
		// trim isn't empty
		else {
			// the new duration is the same as the current one (no updates)
			if (oldDuration == Integer.parseInt(newDuration)) {
				lblError.setText("duration time is the same");
				lblSuccess.setText("");
			}
			// create new Message object with the request
			else {
				Message messageToServer = new Message();
				messageToServer.setMsg(newDuration + " " + exam.getExamID());
				messageToServer.setOperation("updateExamDurationTime");
				messageToServer.setControllerName("ExamController");
				// execute the query and update Exam's instance
				ClientUI.client.handleMessageFromClientUI(messageToServer);
				// check if the update in DB successed
				if (oldDuration != ClientController.exam.getDuration()) {
					lblSuccess.setText("duration time updated successfully !");
					lblError.setText("");
				}
			}
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'exit' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	public void getExitButton(ActionEvent event) {
		display("exit client");
		System.exit(0);
	}

	/**
	 * This method displays a message into the console.
	 *
	 * @param message The string to be displayed.
	 */
	public static void display(String message) {
		System.out.println("> " + message);
	}
}
//End of ShowExamController class