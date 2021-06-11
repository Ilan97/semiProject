package control;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import logic.Question;

/**
 * This is controller class (boundary) for window CheckExamOfStudentInner in
 * Teacher. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Bat-El Gardin
 * @version June 2021
 */

public class CheckExamOfStudentInnerFormController implements GuiController, Initializable {

	@FXML
	private Label lblNumQuestion;
	@FXML
	private TextArea question;
	@FXML
	private Label lblScore;
	@FXML
	private Label lblAns3Wrong;
	@FXML
	private Label lblAns2Wrong;
	@FXML
	private Label lblAns1Correct;
	@FXML
	private Label lblAns4Wrong;
	@FXML
	private Label lblAns2;
	@FXML
	private Label lblAns4;
	@FXML
	private Label lblAns3;
	@FXML
	private Label lblField;
	@FXML
	private Label lblCourse;

	// Instance methods ************************************************

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 * 
	 * @param q     {@link Question} the question to set.
	 * @param index to set on.
	 * @param score of this question.
	 * @param ans   the answers for this question.
	 */
	public void setQuestion(Question q, int index, int score, String ans) {
		String toShow = q.getContent() + "\n\n" + q.getInstructions();
		if (q.getStudentNote() != null)
			toShow += "\n\n" + q.getStudentNote();
		if (q.getTeacherNote() != null)
			toShow += "\n\n" + q.getTeacherNote();
		lblNumQuestion.setText("" + index + "/" + CheckExamOfStudentFormController.qSize);
		lblScore.setText("(" + score + " points)");
		question.setText(toShow);
		lblAns1Correct.setText("1. " + q.getRightAnswer());
		switch (ans) {
		case "0":
			lblAns2.setText("2. " + q.getWrongAnswer1());
			lblAns3.setText("3. " + q.getWrongAnswer2());
			lblAns4.setText("4. " + q.getWrongAnswer3());
			break;

		case "1":
			lblAns2Wrong.setText("2. " + q.getWrongAnswer1());
			lblAns3.setText("3. " + q.getWrongAnswer2());
			lblAns4.setText("4. " + q.getWrongAnswer3());
			break;

		case "2":
			lblAns2.setText("2. " + q.getWrongAnswer1());
			lblAns3Wrong.setText("3. " + q.getWrongAnswer2());
			lblAns4.setText("4. " + q.getWrongAnswer3());
			break;

		case "3":
			lblAns2.setText("2. " + q.getWrongAnswer1());
			lblAns3.setText("3. " + q.getWrongAnswer2());
			lblAns4Wrong.setText("4. " + q.getWrongAnswer3());
			break;
		}
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lblField.setText(CheckExamOfStudentFormController.exam.getFname());
		lblCourse.setText(CheckExamOfStudentFormController.exam.getCname());
	}
}
//End of CheckExamOfStudentInnerFormController class