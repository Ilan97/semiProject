package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import logic.Question;

/**
 * This is controller class (boundary) for window ComputerizedExamInner in
 * Student. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @version May 2021
 */

public class ComputerizedExamInnerFormController implements Initializable {

	// Instance variables **********************************************

	/**
	 * List of indexes (0-3).
	 */
	private List<Integer> a = new ArrayList<>();
	/**
	 * The {@link Question} to show in this window.
	 */
	private Question q;
	/**
	 * The final score.
	 */
	private int score;

	@FXML
	private Label lblNumQuestion;
	@FXML
	private Label lblScore;
	@FXML
	private Label lblCourse;
	@FXML
	private Label lblField;
	@FXML
	private TextArea question;
	@FXML
	private RadioButton btnAnswer1;
	@FXML
	private RadioButton btnAnswer2;
	@FXML
	private RadioButton btnAnswer3;
	@FXML
	private RadioButton btnAnswer4;
	@FXML
	private ToggleGroup answers;
	@FXML
	private Label lblTeacherNote;

	// Instance methods ************************************************

	/**
	 * This method get answers (0 - 3).
	 * 
	 * @return the answer number: 0 is right, 1 is wrong1 2 is wrong2, 3 is wrong3.
	 */
	public int getAnswerOfStudent() {
		return (int) answers.getSelectedToggle().getUserData();
	}

	/**
	 * This method get the final score.
	 * 
	 * @return the score.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 * 
	 * @param q     {@link Question} the question to set.
	 * @param index to set on.
	 * @param score of this question.
	 */
	public void setQuestion(Question q, int index, int score) {
		this.q = q;
		String toShow = q.getContent() + "\n\n" + q.getInstructions();
		if (q.getStudentNote() != null)
			toShow += "\n\n" + q.getStudentNote();
		lblNumQuestion.setText("" + index + "/" + ComputerizedExamFormController.qSize);
		lblScore.setText("(" + score + " points)");
		question.setText(toShow);
		this.score = score;
		setAns(btnAnswer1, 0);
		setAns(btnAnswer2, 1);
		setAns(btnAnswer3, 2);
		setAns(btnAnswer4, 3);
	}

	/**
	 * This method set the answers to the radio buttons.
	 * 
	 * @param btn   {@link RadioButton} to set the answer to.
	 * @param index of the answer.
	 */
	private void setAns(RadioButton btn, int index) {
		int ans = a.get(index);
		btn.setUserData(ans);
		switch (ans) {
		case 0:
			btn.setText(q.getRightAnswer());
			break;
		case 1:
			btn.setText(q.getWrongAnswer1());
			break;
		case 2:
			btn.setText(q.getWrongAnswer2());
			break;
		case 3:
			btn.setText(q.getWrongAnswer3());
			break;
		}
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set field and course
		lblField.setText(ComputerizedExamCodeWindowController.compExam.getFname());
		lblCourse.setText(ComputerizedExamCodeWindowController.compExam.getCname());
		a.add(0);
		a.add(1);
		a.add(2);
		a.add(3);
		Collections.shuffle(a);
	}
}
//End of ComputerizedExamInnerFormController class